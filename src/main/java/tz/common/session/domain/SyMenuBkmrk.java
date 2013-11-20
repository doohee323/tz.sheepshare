package tz.common.session.domain;

import java.math.BigDecimal;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 설    명 : 메뉴즐겨찾기 처리를 위한 domain 클래스
 * 작 성 자 : TZ
 * 작성일자 : 2013-12-01
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */

public class SyMenuBkmrk {

  	/**
 	 * 시스템코드
 	 */
	private String sysCd;

	/**
 	 * 메뉴명
 	 */
	private String menuNm;

  	/**
 	 * 사용자ID
 	 */
 	private String userId;
  	/**
 	 * 메뉴ID
 	 */
 	private BigDecimal menuId;
  	/**
 	 * 정렬순서
 	 */
 	private BigDecimal arrayOrd;
  	/**
 	 * 사용여부
 	 */
 	private String useYn;
  	/**
 	 * 최초등록일시
 	 */
 	private String fstRegDt;
  	/**
 	 * 최초등록사용자ID
 	 */
 	private String fstRegUserId;
  	/**
 	 * 최종수정일시
 	 */
 	private String fnlEditDt;
  	/**
 	 * 최종수정사용자ID
 	 */
 	private String fnlEditUserId;

	/**
	* oggCd
	*/
	private String oggCd;

	/**
	* oggTime
	*/
	private String oggTime;

 	/**
 	 * 시스템코드 getter
 	 * @return sysCd
 	 */
  	public String getSysCd() {
		return sysCd;
	}

 	/**
 	 * 시스템코드 setter
 	 * @return sysCd
 	 */
	public void setSysCd(String sysCd) {
		this.sysCd = sysCd;
	}
 	/**
 	 * 사용자ID getter
 	 * @return userId
 	 */
    public String getUserId() {
       return userId;
    }

 	/**
 	 * 사용자ID setter
 	 * @param userId
 	 */
    public void setUserId(String userId) {
       this.userId = userId;
    }

 	/**
 	 * 메뉴ID getter
 	 * @return menuId
 	 */
    public BigDecimal getMenuId() {
       return menuId;
    }

 	/**
 	 * 메뉴ID setter
 	 * @param menuId
 	 */
    public void setMenuId(BigDecimal menuId) {
       this.menuId = menuId;
    }

 	/**
 	 * 정렬순서 getter
 	 * @return arrayOrd
 	 */
    public BigDecimal getArrayOrd() {
       return arrayOrd;
    }

 	/**
 	 * 정렬순서 setter
 	 * @param arrayOrd
 	 */
    public void setArrayOrd(BigDecimal arrayOrd) {
       this.arrayOrd = arrayOrd;
    }

 	/**
 	 * 사용여부 getter
 	 * @return useYn
 	 */
    public String getUseYn() {
       return useYn;
    }

 	/**
 	 * 사용여부 setter
 	 * @param useYn
 	 */
    public void setUseYn(String useYn) {
       this.useYn = useYn;
    }

 	/**
 	 * 최초등록일시 getter
 	 * @return fstRegDt
 	 */
    public String getFstRegDt() {
       return fstRegDt;
    }

 	/**
 	 * 최초등록일시 setter
 	 * @param fstRegDt
 	 */
    public void setFstRegDt(String fstRegDt) {
       this.fstRegDt = fstRegDt;
    }

 	/**
 	 * 최초등록사용자ID getter
 	 * @return fstRegUserId
 	 */
    public String getFstRegUserId() {
       return fstRegUserId;
    }

 	/**
 	 * 최초등록사용자ID setter
 	 * @param fstRegUserId
 	 */
    public void setFstRegUserId(String fstRegUserId) {
       this.fstRegUserId = fstRegUserId;
    }

 	/**
 	 * 최종수정일시 getter
 	 * @return fnlEditDt
 	 */
    public String getFnlEditDt() {
       return fnlEditDt;
    }

 	/**
 	 * 최종수정일시 setter
 	 * @param fnlEditDt
 	 */
    public void setFnlEditDt(String fnlEditDt) {
       this.fnlEditDt = fnlEditDt;
    }

 	/**
 	 * 최종수정사용자ID getter
 	 * @return fnlEditUserId
 	 */
    public String getFnlEditUserId() {
       return fnlEditUserId;
    }

 	/**
 	 * 최종수정사용자ID setter
 	 * @param fnlEditUserId
 	 */
    public void setFnlEditUserId(String fnlEditUserId) {
       this.fnlEditUserId = fnlEditUserId;
    }
    /**
 	 * 메뉴명 getter
 	 * @param fnlEditUserId
 	 */
    public String getMenuNm() {
		return menuNm;
	}
    /**
 	 * 메뉴명 setter
 	 * @param fnlEditUserId
 	 */
	public void setMenuNm(String menuNm) {
		this.menuNm = menuNm;
	}



	/**
	* oggCd getter
	* @return oggCd
	*/
	public String getOggCd() {
		return oggCd;
	}

	/**
	* oggCd setter
	* @param oggCd
	*/
	public void setOggCd(String oggCd) {
		this.oggCd = oggCd;
	}

	/**
	* oggTime getter
	* @return oggTime
	*/
	public String getOggTime() {
		return oggTime;
	}

	/**
	* oggTime setter
	* @param oggTime
	*/
	public void setOggTime(String oggTime) {
		this.oggTime = oggTime;
	}
}
