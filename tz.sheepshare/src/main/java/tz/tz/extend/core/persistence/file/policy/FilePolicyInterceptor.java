package tz.extend.core.persistence.file.policy;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 정책 적용을 위한 패턴을 정의한다.
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
public class FilePolicyInterceptor {

	private String pattern;

	private FilePolicy policy;

	public FilePolicyInterceptor(String pattern, FilePolicy policy) {
		this.pattern = pattern;
		this.policy = policy;
	}

	/**
	 *
	 * <pre>
	 *  관리 정책이 적용되어야 하는 패턴을 정의한다.
	 * </pre>
	 *
	 * @return 관리 정책이 적용되어야 하는 패턴
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 *
	 * <pre>
	 *  매칭되는 패턴에 적용할 관리 정책을 정의한다.
	 * </pre>
	 *
	 * @return 매칭되는 패턴에 적용할 관리 정책
	 */
	public FilePolicy getPolicy() {
		return policy;
	}

}
