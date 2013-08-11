package tz.basis.query.exception;

import org.springframework.core.NestedRuntimeException;

/**
 *
 * 실패시 발생하는 예외
 *
 * @author TZ
 *
 */
@SuppressWarnings("serial")
public class QueryException extends NestedRuntimeException {

	public QueryException(String msg) {
		super(msg);
	}

	public QueryException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
