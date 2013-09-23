package tz.basis.query.core;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import tz.basis.query.core.evaluator.CallMetaData;
import tz.basis.query.core.evaluator.QueryEvaluator;
import tz.basis.query.core.evaluator.QueryMetaData;
import tz.basis.query.core.evaluator.SqlParameterSourceBuilder;
import tz.basis.query.core.mapper.CamelCaseRowMapper;
import tz.basis.query.core.mapper.ParamRowMapper;
import tz.basis.query.core.mapper.PropertyUtils;
import tz.basis.query.exception.QueryException;
import tz.basis.query.util.QueryUtils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.CallableStatementCreatorFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.metadata.CallMetaDataContext;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * 
 * @author TZ
 * 
 */
public class QueryExecutor implements InitializingBean {

	private static final Logger logger = LoggerFactory
			.getLogger(QueryExecutor.class);

	private DataSource dataSource;

	private SimpleJdbcTemplate jdbcTemplate;

	private SqlMapClient sqlMapClient;

	private boolean userPropertyName = true;

	private Map<String, CallMetaDataContext> procedureCache = new HashMap<String, CallMetaDataContext>();

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new SimpleJdbcTemplate(dataSource);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	/**
	 * 
	 * Template Engine 을 이용하여 쿼리를 생성한다.
	 * 
	 * @param statementTemplate
	 * @param param
	 * @return
	 */
	public QueryMetaData getQuery(Object statementTemplate, Object param) {
		QueryMetaData queryMetadata = getQueryEvaluator().evaluate(
				statementTemplate, param);

		logger.trace("[Generated Query] {}", queryMetadata.getStatement());

		return queryMetadata;
	}

	private QueryEvaluator getQueryEvaluator() {
		QueryEvaluator queryEvaluator = new QueryEvaluator();
		queryEvaluator.setSqlMapClient(sqlMapClient);
		return queryEvaluator;
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(Object statementTemplate, Object parameter,
			Class<T> clazz) {
		List<T> list = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			list = (List<T>) jdbcTemplate.getNamedParameterJdbcOperations()
					.query(metadata.getStatement(),
							metadata.getSqlParameterSource(),
							getRowMapper(clazz));
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	// @Deprecated
	public List<Object> queryForList(Object statementTemplate, Object parameter) {
		List<Object> list = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			list = (List<Object>) jdbcTemplate
					.getNamedParameterJdbcOperations().query(
							metadata.getStatement(),
							metadata.getSqlParameterSource(),
							getRowMapper(metadata.getResultClass()));
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param skipRows
	 * @param maxRows
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> queryForList(Object statementTemplate, Object parameter,
			final int skipRows, final int maxRows, Class<T> clazz) {

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		final List<T> list = new ArrayList<T>();

		try {
			final RowMapper<T> rowMapper = (RowMapper<T>) getRowMapper(clazz);

			jdbcTemplate.getNamedParameterJdbcOperations().query(
					metadata.getStatement(), metadata.getSqlParameterSource(),
					new RowCallbackHandler() {

						int rowCount = 0;

						public void processRow(ResultSet rs)
								throws SQLException {
							if (rowCount >= skipRows
									&& rowCount - skipRows < maxRows) {
								list.add(rowMapper.mapRow(rs, rowCount));
							}

							rowCount++;
						}
					});
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param skipRows
	 * @param maxRows
	 * @param clazz
	 * @return
	 */
	// @Deprecated
	public List<Object> queryForList(Object statementTemplate,
			Object parameter, final int skipRows, final int maxRows) {

		final List<Object> list = new ArrayList<Object>();

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			final RowMapper<?> rowMapper = getRowMapper(metadata
					.getResultClass());

			jdbcTemplate.getNamedParameterJdbcOperations().query(
					metadata.getStatement(), metadata.getSqlParameterSource(),
					new RowCallbackHandler() {

						int rowCount = 0;

						public void processRow(ResultSet rs)
								throws SQLException {
							if (rowCount > skipRows
									&& rowCount - skipRows < maxRows) {
								list.add(rowMapper.mapRow(rs, rowCount));
							}

							rowCount++;
						}
					});
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public List<Map<String, Object>> queryForMapList(Object statementTemplate,
			Object parameter) {
		return queryForMapList(statementTemplate, parameter, userPropertyName);
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @param usePropertyName
	 * @return
	 */
	// @Deprecated
	public List<Map<String, Object>> queryForMapList(Object statementTemplate,
			Object parameter, boolean usePropertyName) {

		List<Map<String, Object>> list = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			list = jdbcTemplate.getNamedParameterJdbcOperations().query(
					metadata.getStatement(), metadata.getSqlParameterSource(),
					getRowMapper(usePropertyName));
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> queryForList(Object statementTemplate, Object parameter,
			RowMapper<T> rowMapper) {

		List<T> list = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			list = jdbcTemplate.getNamedParameterJdbcOperations().query(
					metadata.getStatement(), metadata.getSqlParameterSource(),
					rowMapper);
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @param skipRows
	 * @param maxRows
	 * @param usePropertyName
	 * @return
	 */
	// @Deprecated
	public List<Map<String, Object>> queryForMapList(Object statementTemplate,
			Object parameter, final int skipRows, final int maxRows,
			boolean usePropertyName) {

		final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			final RowMapper<Map<String, Object>> rowMapper = getRowMapper(usePropertyName);

			jdbcTemplate.getNamedParameterJdbcOperations().query(
					metadata.getStatement(), metadata.getSqlParameterSource(),
					new RowCallbackHandler() {

						int rowCount = 0;

						public void processRow(ResultSet rs)
								throws SQLException {
							if (rowCount > skipRows
									&& rowCount - skipRows <= maxRows) {
								list.add(rowMapper.mapRow(rs, rowCount));
							}

							rowCount++;
						}
					});
		} catch (DataAccessException e) {
			throw e;
		}

		return list;
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T queryForObject(Object statementTemplate, Object parameter,
			Class<T> clazz) {

		Object result = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			result = (T) jdbcTemplate.getNamedParameterJdbcOperations()
					.queryForObject(metadata.getStatement(),
							metadata.getSqlParameterSource(),
							getRowMapper(clazz));
			if (result instanceof java.math.BigDecimal) {
				result = Integer.parseInt(result.toString());
			}
		} catch (EmptyResultDataAccessException e) {
			logger.debug("조회된 데이터가 없습니다. - {}",
					ExceptionUtils.getRootCauseMessage(e));
		} catch (DataAccessException e) {
			throw e;
		}

		return (T) result;
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param rowMapper
	 * @return
	 */
	public <T> T queryForObject(Object statementTemplate, Object parameter,
			RowMapper<T> rowMapper) {

		T result = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			result = (T) jdbcTemplate.getNamedParameterJdbcOperations()
					.queryForObject(metadata.getStatement(),
							metadata.getSqlParameterSource(), rowMapper);
		} catch (EmptyResultDataAccessException e) {
			logger.debug("조회된 데이터가 없습니다. - {}",
					ExceptionUtils.getRootCauseMessage(e));
		} catch (DataAccessException e) {
			throw e;
		}

		return result;
	}

	/**
	 * 
	 * iBatis의 ResultMap을 사용하여 조회함
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	// @Deprecated
	public Object queryForObject(Object statementTemplate, Object parameter) {

		Object result = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			result = jdbcTemplate.getNamedParameterJdbcOperations()
					.queryForObject(metadata.getStatement(),
							metadata.getSqlParameterSource(),
							getRowMapper(metadata.getResultClass()));
		} catch (EmptyResultDataAccessException e) {
			logger.debug("조회된 데이터가 없습니다. - {}",
					ExceptionUtils.getRootCauseMessage(e));
		} catch (DataAccessException e) {
			throw e;
		}

		return result;
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> queryForMap(Object statementTemplate,
			Object parameter) {
		return queryForMap(statementTemplate, parameter, userPropertyName);
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @param usePropertyName
	 * @return
	 */
	// @Deprecated
	public Map<String, Object> queryForMap(Object statementTemplate,
			Object parameter, boolean usePropertyName) {

		Map<String, Object> result = null;

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			result = jdbcTemplate.getNamedParameterJdbcOperations()
					.queryForObject(metadata.getStatement(),
							metadata.getSqlParameterSource(),
							getRowMapper(usePropertyName));
		} catch (EmptyResultDataAccessException e) {
			logger.debug("조회된 데이터가 없습니다. - {}",
					ExceptionUtils.getRootCauseMessage(e));
		} catch (DataAccessException e) {
			throw e;
		}

		return result;
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public SqlRowSet queryForRowSet(Object statementTemplate, Object parameter) {

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			return jdbcTemplate.getNamedParameterJdbcOperations()
					.queryForRowSet(metadata.getStatement(),
							metadata.getSqlParameterSource());
		} catch (DataAccessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Integer queryForInt(Object statementTemplate, Object parameter) {

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			return jdbcTemplate.queryForInt(metadata.getStatement(),
					metadata.getSqlParameterSource());
		} catch (DataAccessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Long queryForLong(Object statementTemplate, Object parameter) {

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			return jdbcTemplate.queryForLong(metadata.getStatement(),
					metadata.getSqlParameterSource());
		} catch (DataAccessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Integer update(Object statementTemplate, Object parameter) {

		QueryMetaData metadata = getQuery(statementTemplate, parameter);

		try {
			return jdbcTemplate.update(metadata.getStatement(),
					metadata.getSqlParameterSource());
		} catch (DataAccessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameters
	 * @return
	 */
	public int[] batchUpdate(Object statementTemplate, List<?> parameters) {

		SqlParameterSource[] parameterSources = new SqlParameterSource[parameters
				.size()];
		QueryMetaData metadata = null;

		for (int i = 0; i < parameters.size(); ++i) {
			if (i == 0) {
				metadata = getQuery(statementTemplate, parameters.get(i));
			}

			parameterSources[i] = SqlParameterSourceBuilder
					.getSqlParameterSource(parameters.get(i));
		}

		try {
			return jdbcTemplate.batchUpdate(metadata.getStatement(),
					parameterSources);
		} catch (DataAccessException e) {
			throw e;
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param action
	 * @return
	 * @throws DataAccessException
	 */
	// @Deprecated
	public <T> T execute(Object statementTemplate, Object parameter,
			PreparedStatementCallback<T> action) throws DataAccessException {
		QueryMetaData metadata = getQuery(statementTemplate, parameter);
		return jdbcTemplate.getNamedParameterJdbcOperations().execute(
				metadata.getStatement(), metadata.getSqlParameterSource(),
				action);
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> executeCallStatement(Object statementTemplate,
			Object parameter) {

		QueryMetaData metadata = getQuery(statementTemplate, parameter);
		CallMetaData callMetaData = metadata.getCallMetaData();

		if (callMetaData == null) {
			throw new QueryException(
					"Stored procedure compile error. Call String is "
							+ metadata.getStatement());
		}

		CallMetaDataContext callMetaDataContext = null;

		synchronized (CallMetaDataContext.class) {
			if (procedureCache.containsKey(callMetaData.getSchemaName()
					+ callMetaData.getProcedureName())) {
				callMetaDataContext = procedureCache.get(callMetaData
						.getSchemaName() + callMetaData.getProcedureName());

			} else {
				callMetaDataContext = new CallMetaDataContext();
				callMetaDataContext.setCatalogName(callMetaData.getCatalogName());
				callMetaDataContext.setSchemaName(callMetaData.getSchemaName());
				callMetaDataContext.setProcedureName(callMetaData
						.getProcedureName());
				callMetaDataContext.setFunction(callMetaData.isFunction());
				callMetaDataContext.initializeMetaData(dataSource);
				callMetaDataContext
						.processParameters(new ArrayList<SqlParameter>());

				procedureCache.put(
						callMetaData.getSchemaName()
								+ callMetaData.getProcedureName(),
						callMetaDataContext);
			}
		}

		String callString = callMetaDataContext.createCallString();

		if (logger.isDebugEnabled()) {
			logger.debug("Compiled stored procedure. Call string is ["
					+ callString + "]");
		}

		CallableStatementCreatorFactory callableStatementFactory = new CallableStatementCreatorFactory(
				callString, callMetaDataContext.getCallParameters());
		callableStatementFactory
				.setNativeJdbcExtractor(((JdbcTemplate) jdbcTemplate
						.getJdbcOperations()).getNativeJdbcExtractor());

		Map<String, Object> matchedCallParams = callMetaDataContext
				.matchInParameterValuesWithCallParameters(SqlParameterSourceBuilder
						.getSqlParameterSource(parameter));

		CallableStatementCreator csc = callableStatementFactory
				.newCallableStatementCreator(matchedCallParams);

		if (logger.isDebugEnabled()) {
			logger.debug("The following parameters are used for call "
					+ callString + " with: " + matchedCallParams);
			int i = 1;

			for (SqlParameter p : callMetaDataContext.getCallParameters()) {
				logger.debug(i++ + ": " + p.getName() + " SQL Type "
						+ p.getSqlType() + " Type Name " + p.getTypeName()
						+ " " + p.getClass().getName());
			}
		}

		return jdbcTemplate.getJdbcOperations().call(csc,
				callMetaDataContext.getCallParameters());
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param clazz
	 * @return
	 */
	public <T> T executeProcedure(Object statementTemplate, Object parameter,
			Class<T> clazz) {
		return new SimpleJdbcCall(dataSource).withProcedureName(
				(String) statementTemplate).executeObject(clazz,
				SqlParameterSourceBuilder.getSqlParameterSource(parameter));
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> executeProcedure(Object statementTemplate,
			Object parameter) {
		return new SimpleJdbcCall(dataSource).withProcedureName(
				(String) statementTemplate).execute(
				SqlParameterSourceBuilder.getSqlParameterSource(parameter));
	}

	/**
	 * 
	 * @param <T>
	 * @param statementTemplate
	 * @param parameter
	 * @param clazz
	 * @return
	 */
	public <T> T executeFunction(Object statementTemplate, Object parameter,
			Class<T> clazz) {
		return new SimpleJdbcCall(dataSource).withFunctionName(
				(String) statementTemplate).executeObject(clazz,
				SqlParameterSourceBuilder.getSqlParameterSource(parameter));
	}

	/**
	 * 
	 * @param statementTemplate
	 * @param parameter
	 * @return
	 */
	public Map<String, Object> executeFunction(Object statementTemplate,
			Object parameter) {
		return new SimpleJdbcCall(dataSource).withFunctionName(
				(String) statementTemplate).execute(
				SqlParameterSourceBuilder.getSqlParameterSource(parameter));
	}

	private <T> RowMapper<?> getRowMapper(Class<T> clazz) {
		RowMapper<?> rowMapper = null;

		if (QueryUtils.isAssignableFromMap(clazz)) {
			rowMapper = new CamelCaseRowMapper();
		} else {
			if (QueryUtils.isPrimitiveType(clazz)) {
				rowMapper = new RowMapper<T>() {
					public T mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						T result = (T) rs.getObject(1);
						if (logger.isDebugEnabled()) {
							logger.debug(PropertyUtils
									.primitiveToString(result));
						}
						return result;
					}
				};
			} else {
				rowMapper = ParamRowMapper.newInstance(clazz);
			}
		}

		return rowMapper;
	}

	private RowMapper<Map<String, Object>> getRowMapper(boolean usePropertyName) {
		RowMapper<Map<String, Object>> rowMapper = null;

		if (usePropertyName) {
			rowMapper = new CamelCaseRowMapper();
		} else {
			rowMapper = new ColumnMapRowMapper();
		}

		return rowMapper;
	}

	public void afterPropertiesSet() throws Exception {
		if (jdbcTemplate == null) {
			throw new BeanInitializationException("");
		}
	}
}