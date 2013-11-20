package tz.extend.iam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.basis.iam.core.common.util.UserInfoHolder;
import tz.extend.config.Constants;
import tz.extend.core.message.MessageCodes;
import tz.extend.util.StringUtil;
import tz.extend.core.exception.BizException;
import tz.extend.iam.authentication.UserDefinition;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : {@link UserDefinition}의 조회를 지원하는 편의 클래스.
 *           세션범위에 저장된 다양한 사용자 정보를 조회한다.
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-02-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
public class UserInfo {

    private static Logger logger = LoggerFactory.getLogger(UserInfo.class);

    /**
     * 로그인한 사용자 정보를 session 스코프에 담기 위한 키
     */
    public static final String USER_DATA_KEY = "userSession"; // 이 값을 절대 수정하지 말 것.

    /**
     * <pre>
     *  사용자ID 조회
     * </pre>
     *
     * @return 사용자 ID
     */
    public static String getUserId() {
        try {
            UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);
            return user.getUserId();
        } catch (Exception e) {
        }
        return "";
    }

    /**
     * <pre>
     *  사용자 한글명 조회
     * </pre>
     *
     * @return 사용자 한글명
     */
    public static String getUserKNm() {
        UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);
        return user.getUserNm();
    }

    /**
     * <pre>
     *  사용자 영문명 조회
     * </pre>
     *
     * @return 사용자 영문명
     */
    public static String getUserENm() {
        UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);

        if (user == null) {
            logger.debug("no user info");
            throw new BizException("sy.err.noSession");
        }

        return user.getUserEnm();
    }

    /**
     * <pre>
     *  로케일 코드 조회
     * </pre>
     *
     * @return 로케일코드
     */
    public static String getLoclCd() {
        UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);

        if (user == null) {
            logger.debug("no user info");
            throw new BizException("sy.err.noSession");
        }

        return user.getLoclCd();
    }

    public static UserDefinition getUserInfo() {
        UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);
        if (user == null) {
            throw new BizException(MessageCodes.MSG_ERROR_NOSESSION);
        }
        return user;
    }

    public static String getUserNm() {
        return getUserInfo().getUserNm();
    }

    public static List getPrivs() {
        return getUserInfo().getPrivs();
    }

    public static String getPasswd() {
        return getUserInfo().getPassword();
    }

    public static String getSource() {
        return getUserInfo().getSource();
    }

    public static String getUserTpCd() {
        return getUserInfo().getUserTpCd();
    }

    public static String getOrgCd() {
        return getUserInfo().getOrgCd();
    }

    public static String getOrgCdChg() {
        return getUserInfo().getOrgCd();
    }

    public static String getNatCd() {
        return getUserInfo().getNatCd();
    }

    public static void setOrgInfo(List<Map<String, Object>> mData) {
        getUserInfo().setOrgInfo(mData);
    }

    public static void setOrgInfoChg(List<Map<String, Object>> mData) {
        getUserInfo().setOrgInfoChg(mData);
    }

    public static void setNatInfo(List<Map<String, Object>> mData) {
        getUserInfo().setNatInfo(mData);
    }

    public static boolean hasPriv(String code) {
        return getUserInfo().hasPriv(code);
    }

    public static String getCurPriv() {
        return getUserInfo().getCurPriv();
    }

    public static HashMap getOrgInfo() {
        return getUserInfo().getOrgInfo();
    }

    public static HashMap getOrgInfoChg() {
        return getUserInfo().getOrgInfoChg();
    }

    public static HashMap getNatInfo() {
        return getUserInfo().getNatInfo();
    }

    public static String getEmpNo() {
        return getUserInfo().getEmpNo();
    }

    public static String getLocalEmpNo() {
        return getUserInfo().getLocalEmpNo();
    }

    public static String getCprCode() {
        return getUserInfo().getCprCode();
    }

    public static String getCprCodeChg() {
        return getUserInfo().getCprCodeChg();
    } 

    public static String getDeptCode() {
        return getUserInfo().getDeptCode();
    }

    public static String getSysCd() {
        return getUserInfo().getSysCd();
    }

    public static String getMenuCd() {
        return getUserInfo().getMenuCd();
    }

    public static String getGuid() {
        return getUserInfo().getGuid();
    }

    public static String getAccessTm() {
        return getUserInfo().getAccessTm();
    }

    public static String getMenuNm() {
        return getUserInfo().getMenuNm();
    }

    public static String getIpAddress() {
        return getUserInfo().getIpAddress();
    }

    public static String getGroupId() {
        try {
            UserDefinition user = UserInfoHolder.getUserInfo(UserDefinition.class);
            Object obj = user.getEtcInfo().get("groupId");
            return StringUtil.getText(obj);
        } catch (Exception e) {
            return Constants.defaultGroupId;
        }
    }

    public static Object getLoginInfo(){
        return getUserInfo().getLoginInfo();
    }

    public static void setLoginInfo(Object loginInfo){
        getUserInfo().setLoginInfo(loginInfo);
    }
}
