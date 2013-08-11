package tz.basis.query.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author TZ
 *
 * @param <T>
 */
public class ObjectCopyRowMapper<T> implements RowMapper<T> {

	private static final Logger logger = LoggerFactory.getLogger(ObjectCopyRowMapper.class);

	private ObjectCopyMapper mapper = new ObjectCopyMapper();
	private Class<T> clazz;

	public ObjectCopyRowMapper(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T result =  mapper.mapper(rs, clazz);

		if(logger.isDebugEnabled())	{
			logger.debug(PropertyUtils.beanToString(result));
		}

		return result;
	}
}
