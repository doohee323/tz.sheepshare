package tz.extend.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import tz.basis.core.exception.CoreBizException;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 일반적인 비즈니스 예외를 던지는 경우 사용하는 예외 클래스
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-02-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
@SuppressWarnings("serial")
@ResponseStatus(value = HttpStatus.OK)
public class BizException extends CoreBizException {

	/**
	 * <pre>
	 *  Constructor
	 * </pre>
	 *
	 * @param code message code
	 */
	public BizException(String code) {
		super(code);
	}

	/**
	 * <pre>
	 *  Constructor
	 * </pre>
	 *
	 * @param code message code
	 * @param ex 예외
	 */
	public BizException(String code, Exception ex) {
		super(code, ex);
	}

	/**
	 * <pre>
	 *  Constructor
	 * </pre>
	 *
	 * @param code message code
	 * @param arguments message bundle에 전달될 argument
	 */
	public BizException(String code, Object[] arguments) {
		super(code, arguments);
	}

	/**
	 * <pre>
	 *  Constructor
	 * </pre>
	 *
	 * @param code message code
	 * @param arguments message bundle에 전달될 argument
	 * @param ex 예외
	 */
	public BizException(String code, Object[] arguments, Exception ex) {
		super(code, arguments, ex);
	}

	/**
	 * <pre>
	 *  Constructor
	 * </pre>
	 *
	 * @param code message code
	 * @param defaultMessage message bundle에서 code에 해당되는 메세지를 찾지 못한 경우 표현되어야 할 기본 메세지
	 * @param arguments message bundle에 전달될 argument
	 */
	public BizException(String code, String defaultMessage, Object[] arguments) {
		super(code, defaultMessage, arguments);
	}

	/**
	 * <pre>
	 *  Constructor
	 * </pre>
	 *
	 * @param code message code
	 * @param defaultMessage message bundle에서 code에 해당되는 메세지를 찾지 못한 경우 표현되어야 할 기본 메세지
	 * @param arguments message bundle에 전달될 argument
	 * @param ex 예외
	 */
	public BizException(String code, String defaultMessage, Object[] arguments, Exception ex) {
		super(code, defaultMessage, arguments, ex);
	}

	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDefaultMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object[] getArgs() {
		// TODO Auto-generated method stub
		return null;
	}

}
