package tz.extend.core.persistence.file.policy;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 처리 정책을 정의하기 위해 확장하여 정의한다.
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
public abstract class AbstractFilePolicy implements FilePolicy {

	protected String renamePattern;
	protected String denied;
	protected String allowed;
	protected boolean overriding;
	protected long maxSize;
	protected String subDirectory;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#getRenamePattern()
	 */
	public String getRenamePattern() {
		return renamePattern;
	}

	public void setRenamePattern(String renamePattern) {
		this.renamePattern = renamePattern;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#isDenied(String fileName)
	 */
	public abstract boolean isDenied(String fileName);

	public void setDenied(String denied) {
		this.denied = denied;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#getDeniedPattern()
	 */
	public String getDeniedPattern() {
		return denied;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#isAllowed(String fileName)
	 */
	public abstract boolean isAllowed(String fileName);

	public void setAllowed(String allowed) {
		this.allowed = allowed;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#getAllowedPattern()
	 */
	public String getAllowedPattern() {
		return allowed;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#overriding()
	 */
	public boolean overriding() {
		return overriding;
	}

	public void setOverriding(boolean overriding) {
		this.overriding = overriding;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#getMaxSize()
	 */
	public long getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(long maxSize) {
		this.maxSize = maxSize;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#getSubDirectory()
	 */
	public String getSubDirectory() {
		return subDirectory;
	}

	public void setSubDirectory(String subDirectory) {
		this.subDirectory = subDirectory;
	}
}
