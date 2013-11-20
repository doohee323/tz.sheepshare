package tz.extend.core.persistence.file.policy;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 정책 관리를 정의하기 위한 기본 인터페이스
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
public interface FilePolicy {

	public boolean getIsCurrDateFolder();

	public void setCurrDateFolder(boolean isCurrDateFolder);

	public String getEndDirectory();

	public void setEndDirectory(String endDirectory);

	/**
	 * <pre>
	 * 파일의 Rename 패턴을 정의한다.
	 * </pre>
	 *
	 * @return Rename 패턴
	 */
	String getRenamePattern();

	/**
	 * <pre>
	 * 접근(승인) 거부된 파일인지를 판별한다.
	 * </pre>
	 *
	 * @param fileName
	 * @return true 일때 거부
	 */
	boolean isDenied(String fileName);

	/**
	 * <pre>
	 * 파일 업로드 거부 정책 정의 표현식 반환
	 * </pre>
	 *
	 * @return  파일 업로드 거부 정책 정의 표현식
	 */
	String getDeniedPattern();

	/**
	 * <pre>
	 * 접근이 허용된 파일인지를 판별한다. (접근 거부가 우선함)
	 * </pre>
	 *
	 * @param fileName 업로드된 파일이름
	 * @return true 일때 허용
	 */
	boolean isAllowed(String fileName);

	/**
	 * <pre>
	 * 파일 업로드 승인 정책 정의 표현식 반환
	 * </pre>
	 *
	 * @return 파일 업로드 승인 표현식
	 */
	String getAllowedPattern();

	/**
	 * <pre>
	 * 동일한 파일이 존재할 경우 덮어쓸 것인지를 판별함.
	 * </pre>
	 *
	 * @return true 일때 덮어씀
	 */
	boolean overriding();

	/**
	 * <pre>
	 * 허용 가능한 파일의 크기를 정의한다.
	 * </pre>
	 *
	 * @return 업로드 가능한 파일의 크기
	 */
	long getMaxSize();

	/**
	 * <pre>
	 * 파일이 저장될 경로를 지정한다.
	 * 실제 파일이 저장될 물리적인 경로는 FilePersistenceManager.baseDirectory를 기준으로 결정됨
	 * </pre>
	 *
	 * @return 파일이 저장되는 상대경로
	 */
	String getSubDirectory();

}
