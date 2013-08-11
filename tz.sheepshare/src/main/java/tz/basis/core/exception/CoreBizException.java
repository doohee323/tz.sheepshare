package tz.basis.core.exception;

/**
 *
 * @author TZ
 *
 */
@SuppressWarnings("serial")
public class CoreBizException extends RuntimeException {

	protected String code;
	protected String defaultMessage;
	protected Object[] arguments;
	protected Exception exception;

	public CoreBizException(String code) {
		this(code, "", null, null);
	}

	public CoreBizException(String code, Exception ex) {
		this(code, "", null, ex);
	}

	public CoreBizException(String code, Object[] arguments) {
		this(code, "", arguments, null);
	}

	public CoreBizException(String code, Object[] arguments, Exception ex) {
		this(code, "", arguments, ex);
	}

	public CoreBizException(String code, String defaultMessage, Object[] arguments) {
		this(code, defaultMessage, arguments, null);
	}

	public CoreBizException(String code, String defaultMessage, Object[] arguments, Exception ex) {
		super(code, ex);

		this.code = code;
		this.defaultMessage = defaultMessage;
		this.arguments = arguments;
		this.exception = ex;
	}

	/**
	 *
	 * @return
	 */
	public String getCode() {
		return code;
	}

	/**
	 *
	 * @return
	 */
	public String getDefaultMessage() {
		return defaultMessage;
	}

	/**
	 *
	 * @return
	 */
	public Object[] getArgs() {
		return arguments;
	}

	/**
	 *
	 * @return
	 */
	public Exception getException() {
		return exception;
	}
}
