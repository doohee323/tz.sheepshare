package tz.extend.core.persistence.file.policy;

import tz.basis.core.exception.CoreBizException;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 정책관리 위반과 관련하여 발생하는 예외를 던지는 경우 사용하는 예외 클래스
 *         {@link CoreBizException} 을 상속하여 UI와의 효율적인 연계를 지원한다.
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
public class FilePolicyViolationException extends CoreBizException {

	public FilePolicyViolationException(String message) {
		super(message);
	}

}
