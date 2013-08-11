package tz.basis.query.core.evaluator;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;

/**
 *
 * @author TZ
 *
 */
public class QueryMetaData {

    private String statement;

    private SqlParameterSource sqlParameterSource;

    private Class<?> resultClass;

    private CallMetaData callMetaData;

    public QueryMetaData(String statement, SqlParameterSource sqlParameterSource, CallMetaData callMetaData) {
        this(statement, sqlParameterSource, null, null);
    }

    public QueryMetaData(String statement, SqlParameterSource sqlParameterSource, Class<?> resultClass,
            CallMetaData callMetaData) {
        this.statement = statement;
        this.sqlParameterSource = sqlParameterSource;
        this.resultClass = resultClass;
        this.callMetaData = callMetaData;
    }

    /**
     *
     * @return
     */
    public String getStatement(){
        return statement;
    }

    /**
     *
     * @return
     */
    public CallMetaData getCallMetaData(){
        return callMetaData;
    }

    /**
     *
     * @return
     */
    public SqlParameterSource getSqlParameterSource(){
        return sqlParameterSource;
    }

    /**
     *
     * @return
     */
    public Class<?> getResultClass(){
        return resultClass;
    }
}
