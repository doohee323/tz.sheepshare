package tz.extend.query;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import tz.basis.iam.core.common.util.UserInfoHolder;
import tz.basis.query.core.QueryExecutor;
import tz.extend.config.Constants;
import tz.extend.iam.UserInfo;
import tz.extend.iam.authentication.UserDefinition;
import tz.extend.query.callback.BatchTransactionCallback;
import tz.extend.util.SpringUtil;
import tz.extend.util.SqlInjectionUtil;
import tz.extend.util.StringUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 :
 * 설    명 : {@link CommonDao}의 구현체
 * 작 성 자 :
 * 작성일자 : 2013-12-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class CommonDaoImpl implements CommonDao {

    private QueryExecutor queryExecutor;

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForObject(Object statementId, Object parameter, Class<T> clazz)
     */
    public <T> T queryForObject(Object statementId, Object parameter, Class<T> clazz){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForObject(statementId, parameter, clazz);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForMap(Object statementId, Object parameter)
     */
    public Map<String, Object> queryForMap(Object statementId, Object parameter){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForMap(statementId, parameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForList(Object statementId, Object parameter, Class<T> clazz)
     */
    public <T> List<T> queryForList(Object statementId, Object parameter, Class<T> clazz){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForList(statementId, parameter, clazz);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForList(Object statementId, Object parameter, RowMapper<T> rowMapper)
     */
    public <T> List<T> queryForList(Object statementId, Object parameter, RowMapper<T> rowMapper){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForList(statementId, parameter, rowMapper);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForList(Object statementId, Object parameter, int skipRows, int maxRows, Class<T> clazz)
     */
    public <T> List<T> queryForList(Object statementId, Object parameter, int skipRows, int maxRows, Class<T> clazz){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForList(statementId, parameter, skipRows, maxRows, clazz);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForMapList(Object statementId, Object parameter)
     */
    public List<Map<String, Object>> queryForMapList(Object statementId, Object parameter){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForMapList(statementId, parameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForMapList(Object statementId, Object parameter, int skipRows, int maxRows)
     */
    public List<Map<String, Object>> queryForMapList(Object statementId, Object parameter, int skipRows, int maxRows){
        SqlInjectionUtil.checkInjectionString(parameter);
        parameter = setAddParam(parameter);
        return queryExecutor.queryForMapList(statementId, parameter, skipRows, maxRows, true);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForInt(Object statementId, Object parameter)
     */
    public Integer queryForInt(Object statementId, Object parameter){
        SqlInjectionUtil.checkInjectionString(parameter);
        try{
            parameter = setAddParam(parameter);
            return queryExecutor.queryForInt(statementId, parameter);
        }catch(EmptyResultDataAccessException e){
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryForLong(Object statementId, Object parameter)
     */
    public Long queryForLong(Object statementId, Object parameter){
        SqlInjectionUtil.checkInjectionString(parameter);
        try{
            parameter = setAddParam(parameter);
            return queryExecutor.queryForLong(statementId, parameter);
        }catch(EmptyResultDataAccessException e){
            return Long.parseLong("0");
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#update(Object statementId, Object parameter)
     */
    public Integer update(Object statementId, Object parameter){
        parameter = setAddParam(parameter);
        return queryExecutor.update(statementId, parameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#batchUpdate(Object statementId, List<?> parameter)
     */
    public int[] batchUpdate(Object statementId, List<?> parameter){
        parameter = setAddParams(parameter);
        return queryExecutor.batchUpdate(statementId, parameter);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#batchUpdate(List<T> parameter, BatchTransactionCallback<T> callback)
     */
    public <T> int[] batchUpdate(List<T> parameter, BatchTransactionCallback<T> callback){
        parameter = setAddParams(parameter);
        List<Object> seeds = new ArrayList<Object>(callback.getBatchSize());
        int[] batchResult = new int[parameter.size()];
        int loop = 0;
        for(T record : parameter){
            seeds.add(callback.doInTransaction(record));
            if(seeds.size() == callback.getBatchSize()){
                int[] local = queryExecutor.batchUpdate(callback.getStatementId(), seeds);
                System.arraycopy(local, 0, batchResult, loop++ * callback.getBatchSize(), callback.getBatchSize());
                seeds.clear();
            }
        }
        if(seeds.size() > 0){
            System.arraycopy(queryExecutor.batchUpdate(callback.getStatementId(), seeds), 0, batchResult, loop
                    * callback.getBatchSize(), callback.getBatchSize());
        }
        return batchResult;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#queryAndBatchUpdate(String statementId, Object parameter, Class<T> clazz, final BatchTransactionCallback<T> callback)
     */
    public <T> void queryAndBatchUpdate(String statementId, Object parameter, Class<T> clazz,
            final BatchTransactionCallback<T> callback){
        //        parameter = setAddParam(parameter);
        //
        //        final BeanRowMapper<T> mapper = new BeanRowMapper<T>(clazz);
        //        final List<Object> seeds = new ArrayList<Object>(callback.getBatchSize());
        //
        //        queryExecutor.queryForList(statementId, parameter, new RowMapper<T>() {
        //
        //            public T mapRow(ResultSet rs, int rowNum) throws SQLException{
        //
        //                seeds.add(callback.doInTransaction(mapper.mapRow(rs, rowNum)));
        //
        //                if(seeds.size() == callback.getBatchSize()){
        //                    queryExecutor.batchUpdate(callback.getStatementId(), seeds);
        //                    seeds.clear();
        //                }
        //
        //                return null;
        //            }
        //        });
        //
        //        if(seeds.size() > 0){
        //            queryExecutor.batchUpdate(callback.getStatementId(), seeds);
        //        }
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * tz.query.CommonDao#executeCallStatement(Object statementId, Object parameter)
     */
    public Map<String, Object> executeCallStatement(Object statementId, Object parameter){
        parameter = setAddParam(parameter);
        return queryExecutor.executeCallStatement(statementId, parameter);
    }

    public void setQueryExecutor(QueryExecutor queryExecutor){
        this.queryExecutor = queryExecutor;
    }

    public Object setAddParam(Object parameter){
        if(parameter == null)
            return parameter;
        try{
            Class<?> clz = parameter.getClass();
            String oggTime = "";
            if(SpringUtil.getApplicationContext() != null) {
            }

            // 세션이 없을 경우에 default groupId를 추출
            String defaultGroupId = Constants.groupId;
            String groupId = "";

            if(parameter instanceof java.util.HashMap){
                groupId = StringUtil.getText(((HashMap)parameter).get("groupId"));
                if(StringUtil.getText(groupId).equals("")) {
                    // 세션에서 groupId를 추출
                    UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);
                    if(user != null){
                        groupId = StringUtil.getText(UserInfo.getUserInfo().getGroupId());
                    }else{
                        if(!groupId.equals(""))
                            groupId = defaultGroupId;
                    }
                    ((HashMap)parameter).put("groupId", groupId);
                    ((HashMap)parameter).put("oggCd", groupId);     // 임의 처리
                    ((HashMap)parameter).put("oggTime", oggTime);
                }
            }else{
                BeanInfo beanInfo = Introspector.getBeanInfo(clz);
                PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
                
                UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);
                if(user != null){
                    groupId = StringUtil.getText(UserInfo.getUserInfo().getGroupId());
                }else{
                    if(!groupId.equals(""))
                        groupId = defaultGroupId;
                }
                for(PropertyDescriptor prop : props){
                    if(prop.getName().equals("groupId")){
                        groupId = (String)prop.getReadMethod().invoke(parameter, (Object[]) null);
                        if(groupId.equals("")) {
                            Method getMethod = prop.getWriteMethod();
                            getMethod.invoke(parameter, groupId);
                        }
                    }                    
                    if(prop.getName().equals("oggCd")){
                        Method getMethod = prop.getWriteMethod();
                        getMethod.invoke(parameter, groupId);
                    }
                    if(prop.getName().equals("oggTime")){
                        Method getMethod = prop.getWriteMethod();
                        getMethod.invoke(parameter, oggTime);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return parameter;
    }

    public <T> List<T> setAddParams(List<T> parameter){
        try{
            for(Object obj : parameter){
                setAddParam(obj);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return parameter;
    }

    /*
     * (non-Javadoc)
     * @see tz.query.CommonDao#queryForMapStringValueList(java.lang.Object, java.lang.Object)
     */
    public List<Map<String, String>> queryForMapStringValueList(Object statementId, Object parameter){
        parameter = setAddParam(parameter);
        return queryExecutor.queryForList(statementId, parameter, new RowMapper<Map<String, String>>() {
            @Override
            public Map<String, String> mapRow(ResultSet rs, int rowNum) throws SQLException{
                Map<String, String> map = new HashMap<String, String>();
                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();
                for(int i = 1; i <= columnCount; i++){
                    map.put(JdbcUtils.convertUnderscoreNameToPropertyName(rsmd.getColumnName(i)), rs.getString(i));
                }
                return map;
            }
        });
    }
}
