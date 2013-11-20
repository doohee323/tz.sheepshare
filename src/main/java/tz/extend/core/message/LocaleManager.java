package tz.extend.core.message;

import java.util.Locale;

import org.springframework.util.StringUtils;

import tz.extend.iam.UserInfo;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : Locale 처리를 위한 편의 클래스. 세션 범위에 존재하는 인증객체에서 사용자의 로케일 정보를 조회하여 반환한다.
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
public class LocaleManager {

	/**
	 *
	 * <pre>
	 *  인증된 사용자의 국가코드를 반환한다.
	 * </pre>
	 *
	 * @return locale
	 */
	public static Locale getUserLocale()	{
		Locale locale = null;

		if(!StringUtils.hasText(UserInfo.getLoclCd())) {
            locale = Locale.getDefault();
        } else {
            String loclCd = UserInfo.getLoclCd();
            if(loclCd.indexOf("_") > -1) {
                locale = new Locale(loclCd.substring(0, loclCd.indexOf("_")), loclCd.substring(loclCd.indexOf("_")+1, loclCd.length()));
            } else {
                locale = new Locale(loclCd);
            }
        }

		return locale;
	}
}
