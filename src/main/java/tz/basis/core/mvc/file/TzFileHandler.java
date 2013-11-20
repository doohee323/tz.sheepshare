package tz.basis.core.mvc.file;

import java.util.HashMap;
import java.util.Map;

import tz.basis.core.CoreChnls;
import tz.basis.core.mvc.file.operator.FileOperator;

/**
 *
 * @author TZ
 *
 */
public class TzFileHandler implements FileHandler {

	private Map<CoreChnls, FileOperator> fileOperators = new HashMap<CoreChnls, FileOperator>();

	public FileOperator getFileOperator(CoreChnls chnl) {
		return fileOperators.get(chnl);
	}

	/**
	 *
	 * 표준웹 파일 처리를 위한 Operator 정의
	 *
	 * @param fileOperator
	 */
	public void setWebFileOperator(FileOperator fileOperator) {
		fileOperators.put(CoreChnls.COMWEB, fileOperator);
	}

}
