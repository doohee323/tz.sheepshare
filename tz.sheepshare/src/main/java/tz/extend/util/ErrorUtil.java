package tz.extend.util;

import tz.extend.core.message.MessageCodes;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : ErrorUtil
 * 설      명 : Error Code 처리를 위한 Utility Class.
 * 작 성 자 : TZ
 * 작성일자 : 2013-12-05
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-07             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class ErrorUtil {

    /**
     * <pre>
     * 에러 발생시 에러 코드를 리턴함
     * </pre>
     * @param se
     * @return errorCode
     */
	public static String getDetailErrorCode(Exception se) {
        String errorCause = se.getMessage();
        return getDetailErrorCode(errorCause);
	}

	/**
	 * <pre>
	 * 에러 발생시 에러 코드를 리턴함
	 * </pre>
	 * @param errorCause
	 * @return errorCode
	 */
    public static String getDetailErrorCode(String errorCause) {
        String errorCode = "";
        try {
            if (errorCause.indexOf("ORA-02292") != -1) {
                errorCode = "자식 레코드가 발견되었습니다";
            } else if (errorCause.indexOf("ORA-00001") != -1) {
                errorCode = MessageCodes.MSG_INFO_DUPLICATE;
            } else  if (errorCause.indexOf("FRM_NAV_001") != -1) {
                errorCode = "sy.err.tz.nav001"; //duplicate action found (@) for spec (@) \r\n@\r\n@
            } else if (errorCause.indexOf("FRM_MSG_008") != -1) {
                errorCode = "sy.err.tz.msg008"; //Can't find such a Message Code in Message File.
            } else if (errorCause.indexOf("FRM_QRY_006") != -1) {
                errorCode = "sy.err.tz.qry006"; //Can't process xml file(@).
            } else if (errorCause.indexOf("FRM_QRY_007") != -1) {
                errorCode = "sy.err.tz.qry007"; //Can't load the file(@).
            } else if (errorCause.indexOf("FRM_QRY_009") != -1) {
                errorCode = "sy.err.tz.qry009"; //Can't make a query : @ \r\nStatement[@] type=@, condition=@, id=@, value=@, iterate=@, startswith=@, endswith=@, conjunction=@
            }
        } catch (Exception e) {
            return errorCode;
        }
        return errorCode;
    }

//    /**
//     * 에러 발생시 상세 에러 메시지를 리턴함
//     * @param se
//     * @param req
//     * @return
//     */
//	public static String getDetailErrorMsg(Exception se) {
//        StringBuffer sb = new StringBuffer();
//        try {
//            sb.append("\n[").append(Message.getGroup().getMessage(getDetailErrorCode(se))).append("]");
//        } catch (Exception e) {
//            return "";
//        }
//        return sb.toString();
//	}
}


