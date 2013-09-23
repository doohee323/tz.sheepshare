package tz.basis.query.core;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author TZ
 *
 */
public class QueryExecutorDelegator {

	@Autowired
	private QueryExecutor queryExecutor;

	public void setQueryExecutor(QueryExecutor queryExecutor) {
		this.queryExecutor = queryExecutor;
	}

	public QueryExecutor getQueryExecutor() {
		return queryExecutor;
	}
}
