package tz.common.locale.domain;


/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 :
 * 설    명 : 사용자 정보  q정의 (SY_USER 사용자 정보를 참조 한다.)
 * 작 성 자 :
 * 작성일자 : 2013-06-25
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-06-25             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class LoclInfo {

    private String userId; //사용자ID

    private String loclCd; //로케일 CD

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoclCd() {
		return loclCd;
	}

	public void setLoclCd(String loclCd) {
		this.loclCd = loclCd;
	}

}
