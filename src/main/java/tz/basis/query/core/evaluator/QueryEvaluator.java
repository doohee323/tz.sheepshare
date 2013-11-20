package tz.basis.query.core.evaluator;

import java.util.HashMap;
import java.util.Map;

import tz.basis.query.exception.QueryException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapClientImpl;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMap;
import com.ibatis.sqlmap.engine.mapping.parameter.ParameterMapping;
import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.mapping.statement.ProcedureStatement;
import com.ibatis.sqlmap.engine.scope.SessionScope;
import com.ibatis.sqlmap.engine.scope.StatementScope;

/**
 *
 * @author TZ
 *
 */
public class QueryEvaluator {

    private static final Logger logger = LoggerFactory.getLogger(QueryEvaluator.class);

    private SqlMapClient sqlMapClient;

    public void setSqlMapClient(SqlMapClient sqlMapClient){
        this.sqlMapClient = sqlMapClient;
    }

    public QueryMetaData evaluate(Object statementTemplate, Object param){
        MappedStatement mappedStatement = null;

        try{
            if(((String)statementTemplate).indexOf(" ") > -1){ // 직접 쿼리를 넘겼을 경우
                return new QueryMetaData((String)statementTemplate,
                        SqlParameterSourceBuilder.getSqlParameterSource(param), null);
            }
            mappedStatement = ((SqlMapClientImpl)sqlMapClient).getMappedStatement((String)statementTemplate);
        }catch(Exception e){
            if(logger.isDebugEnabled()){
                logger.error("[iBATIS] 쿼리 생성 실패! - {}", e.getMessage());
            }

            throw new QueryException("iBatis 쿼리 생성 실패!", e);
        }

        SessionScope sessionScope = beginSessionScope();
        StatementScope statementScope = beginStatementScope(sessionScope, mappedStatement);

        /**
         * 
         */
        Sql mappedSql = mappedStatement.getSql();

        String queryTemplate = mappedSql.getSql(statementScope, param).replaceAll("\\t", " ");

        ParameterMap parameterMap = mappedSql.getParameterMap(statementScope, param);
        ParameterMapping[] parameterMappings = parameterMap.getParameterMappings();
        Object[] parameters = parameterMap.getDataExchange().getData(statementScope, parameterMap, param);

        StringBuilder statement = new StringBuilder();

        int fromIndex = 0;
        int toIndex = 0;

        for(ParameterMapping mapping : parameterMappings){
            if((toIndex = queryTemplate.indexOf("?", fromIndex)) >= fromIndex){
                statement.append(queryTemplate.substring(fromIndex, toIndex));
                statement.append(":");
                statement.append(mapping.getPropertyName());

                fromIndex = toIndex + 1;
            }else{
                break;
            }
        }

        if(fromIndex < queryTemplate.length()){
            statement.append(queryTemplate.substring(fromIndex));
        }

        endStatementScope(statementScope);
        endSessionScope(sessionScope);

        SqlParameterSource parameterSource = SqlParameterSourceBuilder.getSqlParameterSource(getSqlParameterMap(
                parameterMappings, parameters));
        Class<?> resultMapClass = null;

        if(mappedSql.getResultMap(statementScope, param) != null){
            resultMapClass = mappedSql.getResultMap(statementScope, param).getResultClass();
        }

        CallMetaData callMetaData = null;

        if(mappedStatement instanceof ProcedureStatement){
            callMetaData = CallMetaDataBuilder.buildCallMataData(queryTemplate.trim());
        }

        return new QueryMetaData(statement.toString(), parameterSource, resultMapClass, callMetaData);
    }

    protected Map<String, Object> getSqlParameterMap(ParameterMapping[] names, Object[] values){
        Map<String, Object> parameterMap = new HashMap<String, Object>();

        if(values != null && names.length > values.length){
            throw new QueryException("iBatis 쿼리 생성 실패! - Parameter의 개수와 Bind변수의 개수가 일치하지 않습니다.");
        }

        for(int i = 0; i < names.length; ++i){
            parameterMap.put(names[i].getPropertyName(), values[i]);
        }

        return parameterMap;
    }

    protected StatementScope beginStatementScope(SessionScope sessionScope, MappedStatement mappedStatement){
        StatementScope statementScope = new StatementScope(sessionScope);
        sessionScope.incrementRequestStackDepth();
        mappedStatement.initRequest(statementScope);
        return statementScope;
    }

    protected void endStatementScope(StatementScope statementScope){
        statementScope.getSession().decrementRequestStackDepth();
    }

    protected SessionScope beginSessionScope(){
        return new SessionScope();
    }

    protected void endSessionScope(SessionScope sessionScope){
        sessionScope.cleanup();
    }
}