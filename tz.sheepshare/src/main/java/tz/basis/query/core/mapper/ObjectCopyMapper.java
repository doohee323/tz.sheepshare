package tz.basis.query.core.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import tz.basis.query.exception.QueryException;
import tz.extend.util.ClassUtils;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.support.JdbcUtils;

/**
 * <pre>
 * 조회된 결과를 전달받은 클래스 객체에 담아서 반환하거나
 * 객체의 값을 다른 객체에 복사한다.
 * <pre>
 *
 * @see tz.basis.query.core.evaluator.ObjectCopyQueryEvaluator
 *
 * @author TZ
 *
 */
public class ObjectCopyMapper {

    private static final Logger logger = LoggerFactory.getLogger(ObjectCopyMapper.class);

    public <T> T mapper(ResultSet rs, Class<T> clazz) throws SQLException{
        T resultObject = BeanUtils.instantiate(clazz);
        try{
            find(rs, resultObject);
        }catch(Exception e){
            new QueryException("mapper() - 데이터 매핑 실패", e);
        }

        return resultObject;
    }

    private void find(ResultSet rs, Object resultObject) throws Exception{
        Field[] fields = resultObject.getClass().getDeclaredFields();

        for(Field field : fields){
            String columnName = field.getName();
            try{
                if(logger.isDebugEnabled()){
                    logger.debug("find() - MappingInfomation: Class={} columnName={} value={}", new Object[] {
                            resultObject.getClass().getName(), columnName, rs.getObject(columnName) });
                }
                setFieldValue(field, resultObject, rs.getObject(columnName));
            }catch(Exception e){
                logger.debug("find() - Exception: Class={} field={} 가 존재하지 않습니다.", new Object[] {
                        resultObject.getClass().getName(), columnName });
            }
        }
    }

    public <T> T mapperClass(Object rs, Class<T> clazz){
        T resultObject = BeanUtils.instantiate(clazz);
        try{
            findClass(rs, resultObject);
        }catch(Exception e){
            new QueryException("mapperClass() - 데이터 매핑 실패", e);
        }

        return resultObject;
    }

    private void findClass(Object rs, Object resultObject) throws Exception{
        Field[] fields = resultObject.getClass().getDeclaredFields();

        for(Field field : fields){
            String columnName = field.getName();
            try{
                if(logger.isDebugEnabled()){
                    logger.debug("findClass() - MappingInfomation: Class={} columnName={} value={}", new Object[] {
                            resultObject.getClass().getName(), columnName, getFieldValue(field, rs) });
                }
                setFieldValue(field, resultObject, getFieldValue(field, rs));
            }catch(Exception e){
                logger.debug("findClass() - Exception: Class={} field={} 가 존재하지 않습니다.", new Object[] {
                        resultObject.getClass().getName(), columnName });
            }
        }
    }


    public <T> T mapperMap(Map<String, Object> rs, Class<T> clazz){
        T resultObject = BeanUtils.instantiate(clazz);
        try{
            findMap(rs, resultObject);
        }catch(Exception e){
            new QueryException("mapperClass() - 데이터 매핑 실패", e);
        }

        return resultObject;
    }

    private void findMap(Map<String, Object> rs, Object resultObject) throws Exception{
        Field[] fields = resultObject.getClass().getDeclaredFields();

        for(Field field : fields){
            String columnName = field.getName();
            try{
                if(logger.isDebugEnabled()){
                    logger.debug("findClass() - MappingInfomation: Class={} columnName={} value={}", new Object[] {
                            resultObject.getClass().getName(), columnName, getFieldValue(field, rs) });
                }
                setFieldValue(field, resultObject, rs.get(columnName));
            }catch(Exception e){
                logger.debug("findClass() - Exception: Class={} field={} 가 존재하지 않습니다.", new Object[] {
                        resultObject.getClass().getName(), columnName });
            }
        }
    }

    
    private Object getFieldValue(Field field, Object object) throws Exception{
        String aMethodNm = "get" + StringUtil.upperFirst(field.getName());
        Method method = ClassUtils.getMethod(object, aMethodNm, false);
        if (method != null) {
            return ClassUtils.invokeSimple(object, aMethodNm);
        } else {
            return null;
        }
    }

    private void setFieldValue(Field field, Object object, Object value) throws Exception{
        field.setAccessible(true);
        if(field.toGenericString().indexOf(" static ") > -1 || field.toGenericString().indexOf(" final ") > -1) return;
        field.set(object, value);
        field.setAccessible(false);
    }

    protected String getColumnKey(String columnName){
        return JdbcUtils.convertUnderscoreNameToPropertyName(columnName);
    }
}
