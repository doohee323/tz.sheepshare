package tz.basis.query.core.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.support.JdbcUtils;

/**
 *
 * @author TZ
 *
 */
public class CamelCaseRowMapper extends ColumnMapRowMapper {

    private static final Logger logger = LoggerFactory.getLogger(CamelCaseRowMapper.class);

    @Override
    public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException{
        Map<String, Object> resultMap = super.mapRow(rs, rowNum);

        if(logger.isDebugEnabled()){
            logger.debug(PropertyUtils.mapToString(resultMap));
        }

        return resultMap;
    }

    protected String getColumnKey(String columnName){
        return JdbcUtils.convertUnderscoreNameToPropertyName(columnName);
    }

}