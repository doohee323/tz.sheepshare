package tz.extend.config;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import tz.extend.util.config.ConfigUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : Constants
 * 설    명 :
 * 작 성 자 : TZ
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class Constants {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(Constants.class);

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    // 환경 변수
    public static String drive;
    public static String sysCd;
    public static String systemNm;
    public static String docRoot;
    public static String appRoot;
    public static String workingDir;
    public static String uploadDefault;
    public static String groupId;
    public static String defaultGroupId;
    public static String stage;
    public static String accessLogIpForward;
    public static String initPageEncodingType;
    public static String xpAdlFile;
    public static String xpCabVer;
    
    /**
     * <pre>
     * init
     * </pre>
     */
    @PostConstruct
    public void init(){
        appProperties = ConfigUtil.init(appProperties, this);
    }

    /**
     * 상수 정의
     */
    public static final String MSG_PREFIX = "sy.";

    /**
     *
     */
    public static final String SERVLET_ERROR_REQUEST_URI_KEY = MSG_PREFIX + "servlet.error.request_uri";

    /**
     *
     */
    public static final String SERVLET_ERROR_EXCEPTION_KEY = MSG_PREFIX + "servlet.error.exception";

    /**
     *
     */
    public static final String SERVLET_ERROR_CODE_KEY = MSG_PREFIX + "servlet.error.code";

    /**
     * stopwatch 키 상수
     */
    public static final String SERVLET_STOPWATCH_KEY = MSG_PREFIX + "servlet.stopwatch";

    /**
    *
    */
    public static final String ERROR_MESSAGE_KEY = MSG_PREFIX + "servlet.error.message";

    /**
     * 메시지 상수 : 저장(CUD) 성공
     */
    public static final String MSG_SUCCESS_SAVE = MSG_PREFIX + "suc.save";

    /**
     * 메시지 상수 : 조회 성공
     */
    public static final String MSG_SUCCESS_SELECT = MSG_PREFIX + "suc.retrieve";

    /**
     * 메시지 상수 : 삭제 성공
     */
    public static final String MSG_SUCCESS_DELETE = MSG_PREFIX + "suc.delete";

    /**
     * 메시지 상수 : 업데이트 성공
     */
    public static final String MSG_SUCCESS_UPDATE = MSG_PREFIX + "suc.update";

    /**
     * 메시지 상수 : 검색된 정보가 없음
     */
    public static final String MSG_INFO_NODATA = MSG_PREFIX + "inf.nodata";

    /**
     * 메시지 상수 : 저장할 정보가 없음
     */
    public static final String MSG_INFO_NOSAVE = MSG_PREFIX + "inf.nosave";

    /**
     * 메시지 상수 : 삭제할 정보가 없음
     */
    public static final String MSG_INFO_NODELETE = MSG_PREFIX + "inf.nodelete";

    /**
     * 메시지 상수 : 선택된 항목 없음
     */
    public static final String MSG_INFO_NOSELET = MSG_PREFIX + "inf.noselect";

    /**
     * 메시지 상수 : 조회 오류
     */
    public static final String MSG_ERROR_RETRIEVE = MSG_PREFIX + "err.retrieve";

    /**
     * 메시지 상수 : 생성 오류
     */
    public static final String MSG_ERROR_CREATE = MSG_PREFIX + "err.create";

    /**
     * 메시지 상수 : 업데이트 오류
     */
    public static final String MSG_ERROR_UPDATE = MSG_PREFIX + "err.update";

    /**
     * 메시지 상수 : 삭제 오류
     */
    public static final String MSG_ERROR_DELETE = MSG_PREFIX + "err.delete";

    /**
     * 메시지 상수 : 저장(CUD) 오류
     */
    public static final String MSG_ERROR_SAVE = MSG_PREFIX + "err.save";

    /**
     * 메시지 상수 : 중복키 오류
     */
    public static final String MSG_ERROR_DUPLICATE = MSG_PREFIX + "warn.dupkey1";

    /**
     * 메시지 상수 : 알수 없는 오류
     */
    public static final String MSG_ERROR_DEFAULT = MSG_PREFIX + "err.desc";

    /**
     * 메시지 상수 : 저장 확인
     */
    public static final String MSG_CONFIRM_SAVE = MSG_PREFIX + "cfm.save";

    /**
     * 메시지 상수 : 삭제 확인
     */
    public static final String MSG_CONFIRM_DELETE = MSG_PREFIX + "cfm.delete";

    /**
     * 메시지 상수 : 변경 확인
     */
    public static final String MSG_CONFIRM_UPDATE = MSG_PREFIX + "cfm.update";

    /**
     * 메시지 상수 : 필수 입력 경고
     */
    public static final String MSG_WARNING_INPUT = MSG_PREFIX + "warn.input";

    /**
     * 메시지 상수 : 변경 확인
     */
    public static final String MSG_WARNING_REQUIRED = MSG_PREFIX + "warn.required";

    /**
     * 메시지 상수 : 변경 확인
     */
    public static final String MSG_WARNING_SELECT = MSG_PREFIX + "warn.select";

    /**
     * 메시지 상수 : 변경 확인
     */
    public static final String MSG_WARNING_RETRIEVE = MSG_PREFIX + "warn.retrieve";

    /**
     * 메시지 상수 : 변경 확인
     */
    public static final String MSG_WARNING_REQUIRED1 = MSG_PREFIX + "warn.required1";

}
