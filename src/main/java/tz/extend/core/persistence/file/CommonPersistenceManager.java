package tz.extend.core.persistence.file;

import java.io.File;

import tz.basis.core.mvc.file.DefaultTzPersistenceManager;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일처리 공통클래스
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
public class CommonPersistenceManager extends DefaultTzPersistenceManager	{

	/**
	 *
	 * <pre>
	 *  파일시스템에 주어진 파일이 존재하는지 검사한다.
	 * </pre>
	 *
	 * @param folder 파일이 저장된 디렉토리
	 * @param fileName 파일이름
	 * @return true 이면 파일이 존재함.
	 */
	public boolean isExists(String folder, String fileName) {
        File file = new File(baseDirectory, folder + File.separator + fileName);
		return file.exists();
	}

	public String getBaseDirectory() {
		return baseDirectory.getAbsolutePath();
	}

}
