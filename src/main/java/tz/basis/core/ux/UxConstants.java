package tz.basis.core.ux;

import java.util.HashMap;
import java.util.Map;

public class UxConstants {
	public static final String ROWSTATUS_NAME = "rowStatus";

	public static final String DEFAULT_CHARSET = "utf-8"; // 디폴트는 utf-8 이다.

	public static final String ERROR_CODE_NAME = "ErrorCode";  //에러 코드  이름  셋팅
	public static final String ERROR_CODE_SUCC_VALUE = "0";  // 기본 에러 코드  값  셋팅
	public static final String ERROR_CODE_FAIL_VALUE = "-1";  //에러  발생 시 에러 코드  값  셋팅

	public static final String ERROR_MSG_NAME = "ErrorMsg"; // 에러 메시지 셋팅
    public static final String ERROR_MSG_SUCC_VALUE = "SUCCESS"; // 에러 메시지 값  셋팅
    public static final String ERROR_MSG_FAIL_VALUE = "FAILURE"; // 에러 메시지 값  셋팅

	public static Map<String, Object> setMsg(boolean bChk) {
	    Map<String, Object> data = new HashMap<String, Object>();
	    if(bChk) {
	        data.put(UxConstants.ERROR_CODE_NAME, UxConstants.ERROR_CODE_FAIL_VALUE);
	        data.put(UxConstants.ERROR_MSG_NAME, UxConstants.ERROR_MSG_SUCC_VALUE);
	    } else {
	        data.put(UxConstants.ERROR_CODE_NAME, UxConstants.ERROR_CODE_SUCC_VALUE);
	        data.put(UxConstants.ERROR_MSG_NAME, UxConstants.ERROR_MSG_FAIL_VALUE);
	    }
	    return data;
	}
	
}
