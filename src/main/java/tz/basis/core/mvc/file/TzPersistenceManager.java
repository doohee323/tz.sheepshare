package tz.basis.core.mvc.file;

import tz.basis.upload.FileInfo;
import tz.basis.upload.persistence.PersistenceManager;


/**
 * 파일 저장소 관리.
 * <p>
 */
public interface TzPersistenceManager extends PersistenceManager {


	/**
	 * 파일 이동
	 *
	 * @param sourceFolder
	 * @param sourceFileName
	 * @param targetFolder
	 * @param targetFileName
	 * @return
	 */
	FileInfo moveFile(String sourceFolder, String sourceFileName, String targetFolder, String targetFileName);


}
