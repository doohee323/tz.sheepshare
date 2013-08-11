package tz.extend.core.message;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MessageCodes
 * 설    명 : 자주 사용되는 공통적인 메시지 코드
 * 작 성 자 :
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
public interface MessageCodes {

	/**
	 *
	 */
	public static final String MSG_PREFIX = "sy.";

	// 기본 에러 메시지 코드
	public static final String MSG_ERROR_DEFAULT = MSG_PREFIX + "err.err";

	// 확인 메시지 코드
	public static final String MSG_CFM_DELETE = MSG_PREFIX + "cfm.delete";

	// 성공, 정보 메시지 코드
	public static final String MSG_SUCCESS_SAVE = MSG_PREFIX + "suc.save";

	public static final String MSG_SUCCESS_RETRIEVE = MSG_PREFIX + "suc.retrieve";

	public static final String MSG_SUCCESS_DELETE = MSG_PREFIX + "suc.delete";

	public static final String MSG_SUCCESS_UPDATE = MSG_PREFIX + "suc.update";

	public static final String MSG_INFO_NODATA = MSG_PREFIX + "err.noData";

	public static final String MSG_INFO_DUPLICATE = MSG_PREFIX + "err.dup";

	// 보안관련/권한관련 에러 메시지 코드
	public static final String MSG_ERROR_AUTHENTICATE = MSG_PREFIX + "err.noPriv";

	public static final String MSG_ERROR_AUTHORIZE = MSG_PREFIX + "err.noPriv";

	public static final String MSG_ERROR_LOGIN = MSG_PREFIX + "err.login";

	public static final String MSG_ERROR_USER_ID = MSG_PREFIX + "err.noUserId";

	public static final String MSG_ERROR_PWD = MSG_PREFIX + "err.pwd";

	// 세션 에러 메시지 코드
	public static final String MSG_ERROR_NOSESSION = MSG_PREFIX + "err.noSession";

	// DB 작업관련 에러 메시지 코드
	public static final String MSG_ERROR_RETRIEVE = MSG_PREFIX + "err.retrieve";

	public static final String MSG_ERROR_UPDATE = MSG_PREFIX + "err.update";

	public static final String MSG_ERROR_DELETE = MSG_PREFIX + "err.delete";

	public static final String MSG_ERROR_SAVE = MSG_PREFIX + "err.save";

	public static final String MSG_ERROR_BATCH_PARTIAL = MSG_PREFIX + "err.batch.partial";

	public static final String MSG_ERROR_ADMINSSIGN = MSG_PREFIX + "err.sheepshareassign";

	public static final String MSG_ERROR_FILEDOWNLOAD = MSG_PREFIX + "err.filedownload";

	public static final String MSG_ERROR_FILEUPLOAD = MSG_PREFIX + "err.filedownload";

	// 경고 메시지
	public static final String MSG_WARN_PWD_EXPIRED = MSG_PREFIX + "warn.expiredpwd";

	public static final String MSG_WARN_PWD_INVALID = MSG_PREFIX + "warn.invalidpwd";

}
