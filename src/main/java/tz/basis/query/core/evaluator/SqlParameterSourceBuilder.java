package tz.basis.query.core.evaluator;

import java.lang.reflect.Field;
import java.util.Map;

import tz.basis.query.util.QueryUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author TZ
 *
 */
public class SqlParameterSourceBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SqlParameterSourceBuilder.class);

    @SuppressWarnings("unchecked")
    public static SqlParameterSource getSqlParameterSource(Object parameter){
        SqlParameterSource source = new MapSqlParameterSource();

        if(parameter != null){
            if(SqlParameterSource.class.isAssignableFrom(parameter.getClass())){
                source = (SqlParameterSource)parameter;
            }else{
                if(QueryUtils.isAssignableFromMap(parameter.getClass())){
                    source = new MapSqlParameterSource((Map<String, ?>)parameter);
                }else{
                    Field[] fields = parameter.getClass().getDeclaredFields();

                    for(Field field : fields){
                        try{
                            field.setAccessible(true);
                            logger.debug("[SqlParameterSource] Property Name={} Property Value={}", field.getName(),
                                    field.get(parameter));
                            ((MapSqlParameterSource)source).addValue(field.getName(), field.get(parameter));
                        }catch(Exception e){
                        }
                    }
                }
            }
        }

        return source;
    }

}
