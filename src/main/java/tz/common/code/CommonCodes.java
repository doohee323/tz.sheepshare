package tz.common.code;

import tz.extend.config.Constants;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : CommonCodes
 * 설    명 : 자주 사용되는 공통적인 코드
 * 작 성 자 :
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
public interface CommonCodes {

    public static interface Groups {

        /** 사용자 권한 */
        public static final String AUTHORITY = "AUTH000000";

        /** 국가 */
        public static final String COUNTRY = "CNTR000000";

        /** 사용 언어 */
        public static final String LANG = "LANG000000";

        /** JAVA LOCALE */
        public static final String JAVALOCALE = "JVLC000000";

        /** 로케일 => 국가_언어 */
        public static final String LOCL_CD = "CNLG000000";

        /** 검색 대상 */
        public static final String KIND = "KIND000000";

        /** 게시판 유형 */
        public static final String BOARD_TYPE = "BDTP000000";
    }

    public static interface Sessions {
        public static final String USER_ID    = "userId";
        public static final String USER_TYPE  = "userType";
        public static final String USER_NM    = "userNm";
        public static final String DIV_CD     = "divCd";
        public static final String ORG_ID     = "orgId";
        public static final String ENG_EMP_NM = "engEmpNm";
        public static final String PASSWD     = "passwd";
        public static final String EMP_NO     = "empNo";
        public static final String GRADE_CD   = "gradeCd";
        public static final String ORG_NM     = "orgNm";
        public static final String ORG_NM_EN  = "orgNmEn";
        public static final String EMAIL      = "email";
        public static final String HOME_TELNO = "homeTelno";
        public static final String ROLE_CD    = "roleCd";
        public static final String ROLE_NM    = "roleNm";
        public static final String LANG_CD    = "langCd";
    }

//    public static final String AUTHORITY_SYSTEM_ADMIN = Constants.privSystemAdmin; //시스템어드민
//
//    public static final String AUTHORITY_ADMIN = Constants.privAdmin; //시스템관리자
//
//    public static final String AUTHORITY_USER = Constants.privUser; //사용자
//
//    public static final String AUTHORITY_DEVELOPER = Constants.privDeveloper; //개발자
//
//    public static final String AUTHORITY_TEMPORARY = Constants.privTemporary; //임시사용자

    public static final String MENU_HOME = "home"; // 메뉴 홈

    public static final String MENU_LEVEL_1ST = "1"; // 메뉴 대분류

    public static final String MENU_LEVEL_2ND = "2"; // 메뉴 중분류

    public static final String MENU_LEVEL_3RD = "3"; // 메뉴 소분류

    public static final String CODE_MSG_HEAD = "code_msg_"; // 다국어 메세지 값 헤더, 이 뒤에 locale정보가 붙음
}