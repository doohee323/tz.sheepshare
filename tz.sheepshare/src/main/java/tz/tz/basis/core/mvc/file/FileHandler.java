package tz.basis.core.mvc.file;

import tz.basis.core.CoreChnls;
import tz.basis.core.mvc.file.operator.FileOperator;

/**
 *
 * @author TZ
 *
 */
public interface FileHandler {

	/**
	 *
	 * 채널별 File 처리를 위한 Operator를 반환한다.
	 *
	 * @param chnl
	 * @return
	 */
	FileOperator getFileOperator(CoreChnls chnl);

}
