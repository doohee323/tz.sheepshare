package tz.extend.iam.authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

import tz.basis.iam.core.jdbc.authentication.UserMapping;
import tz.extend.util.MapUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 사용자 정보 정의 (SY_USER 사용자 정보를 참조 한다.)
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

public class UserDefinition extends UserMapping {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String userId;

    private String password;

    private String useYn;

    private String orgCd;
    
    private String orgCdChg;
    
    private String userTpCd;
     
    private String userNm;
  
    private String userEnm;
    
    private Date pwdChgDt;
    
    private String email;
   
    private String phone;
    
    private String mPhone;
    
    private String faxno;
   
    private String zipcd;
    
    private String basAddr;
    
    private String dtlAddr;
    
    private String natCd;
    
    private String loclCd;
    
    private String retirYmd;
    
    private String ctCls;

    private String userPositCd;

    private String userRpswrkCd ;

    private String userJobspCd;

    private String userDutyCd;

    private String groupId;

    private String empNo;

    private String localEmpNo;

    private String cprCode; 
    
    private String cprCodeChg;
    
    private String deptCode;
    
    private String sysCd;

    public String getSysCd(){
        return sysCd;
    }

    
    public void setSysCd(String sysCd){
        this.sysCd = sysCd;
    }

    /**
     * 부서코드
     */
    public String getDeptCode(){
        return deptCode;
    }
    
    public void setDeptCode(String deptCode){
        this.deptCode = deptCode;
    }

    /**
     * 사원번호
     */
   public String getEmpNo(){
        return empNo;
    }
   /**
    * 사원번호
    */
    public void setEmpNo(String empNo){
        this.empNo = empNo;
    }
    /**
     * Local 사원번호
     */
   public String getLocalEmpNo(){
        return localEmpNo;
    }
   /**
    * Local 사원번호
    */
    public void setLocalEmpNo(String localEmpNo){
        this.localEmpNo = localEmpNo;
    }
    /**
     * 법인코드
     */
    public String getCprCode(){
        return cprCode;
    }
    /**
     * 법인코드
     */
    public void setCprCode(String cprCode){
        this.cprCode = cprCode;
    }
    /**
     * 법인코드변경
     */
    public String getCprCodeChg(){
        return cprCodeChg;
    }
    /**
     * 법인코드변경
     */
    public void setCprCodeChg(String cprCodeChg){
        this.cprCodeChg = cprCodeChg;
    }
    /**
     * 그룹ID
     */

    public void setGroupId(String groupId){
        this.groupId = groupId;
    }
    /**
     * 그룹ID
     */
    public String getGroupId(){
        return groupId;
    }
    /**
     * 사용자직무코드
     */

    public void setUserDutyCd(String userDutyCd){
        this.userDutyCd = userDutyCd;
    }
    /**
     * 사용자직무코드
     */
    public String getUserDutyCd(){
        return userDutyCd;
    }
    /**
     * 사용자직종코드
     */

    public void setUserJobspCd(String userJobspCd){
        this.userJobspCd = userJobspCd;
    }
    /**
     * 사용자직종코드
     */
    public String getUserJobspCd(){
        return userJobspCd;
    }
    /**
     * 사용자직책코드
     */

    public void setUserRpswrkCd(String userRpswrkCd){
        this.userRpswrkCd = userRpswrkCd;
    }
    /**
     * 사용자직책코드
     */
    public String getUserRpswrkCd(){
        return userRpswrkCd;
    }
    /**
     * 사용자직위코드
     */

    public void setUserPositCd(String userPositCd){
        this.userPositCd = userPositCd;
    }
    /**
     * 사용자직위코드
     */
    public String getUserPositCd(){
        return userPositCd;
    }
    /**
     * 계약구분
     */

    public void setCtCls(String ctCls){
        this.ctCls = ctCls;
    }
    /**
     * 계약구분
     */
    public String getCtCls(){
        return ctCls;
    }
    /**
     * 퇴사일자
     */

    public void setuRetirYmd(String retirYmd){
        this.retirYmd = retirYmd;
    }
    /**
     * 퇴사일자
     */
    public String getRetirYmd(){
        return retirYmd;
    }

    /**
     * @TODO UserData와의 호환성을 위해 추가함.
     */
    private String curPriv;

    private List<String> privs;

    private String menuCd;

    private String menuNm;

    private String source; // 외부시스템으로부터의 로그인(SSO, EP)

    private String ipAddress;

    private String accessTm;

    private String guid;

    private List<String> siteWhere;

    private List<String> deptWhere;
    
    private List<String> privWhere;

    private List<?> curSite;

    private List<?> curDept;

    private List<?> orgInfo;

    private List<?> orgInfoChg;
    
    private List<?> deptInfo;

    private List<?> natInfo;

    private List<?> etcInfo;

    private Object loginInfo;

    @Override
    public String getPassword(){
        return password;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }
    
    public String getUsername(){
        return null;
    }
    
    /**
     * 사용여부
     */
    @Override
    public boolean isEnabled(){
        return StringUtils.hasText(useYn) && useYn.equals("Y");
    }

    /**
     * 프로퍼티코드
     */
    public String getOrgCd(){
        return orgCd;
    }

    /**
     * 기본주소
     */
    public String getBasAddr(){
        return basAddr;
    }

    /**
     * 상세주소
     */
    public String getDtlAddr(){
        return dtlAddr;
    }

    /**
     * 이메일
     */
    public String getEmail(){
        return email;
    }

    /**
     * 팩스번호
     */
    public String getFaxno(){
        return faxno;
    }

    /**
     * 비밀번호 변경일시
     */
    public Date getPwdChgDt(){
        return pwdChgDt;
    }

    /**
     * 사용자 영문명
     */
    public String getUserEnm(){
        return userEnm;
    }

    /**
     * 사용자 유형코드
     */
    public String getUserTpCd(){
        return userTpCd;
    }

    /**
     * 우편번호
     */
    public String getZipcd(){
        return zipcd;
    }

    public String getAccessTm(){
        return accessTm;
    }

    public void setAccessTm(String accessTm){
        this.accessTm = accessTm;
    }

    public String getGuid(){
        return guid;
    }

    public void setGuid(String guid){
        this.guid = guid;
    }

    public String getIpAddress(){
        return ipAddress;
    }

    public void setIpAddress(String ipAddress){
        this.ipAddress = ipAddress;
    }

    public String getSource(){
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }

    @SuppressWarnings("rawtypes")
    public HashMap getOrgInfo(){
        return MapUtil.toHashMap(orgInfo);
    }

    @SuppressWarnings("rawtypes")
    public void setOrgInfo(List<Map<String, Object>> mData){
        orgInfo = new ArrayList();
        orgInfo = MapUtil.mapToList(orgInfo, mData);
    }

    @SuppressWarnings("rawtypes")
    public HashMap getOrgInfoChg(){
        return MapUtil.toHashMap(orgInfoChg);
    }

    @SuppressWarnings("rawtypes")
    public void setOrgInfoChg(List<Map<String, Object>> mData){
        orgInfoChg = new ArrayList();
        orgInfoChg = MapUtil.mapToList(orgInfoChg, mData);
    }
    @SuppressWarnings("rawtypes")
    public HashMap getDeptInfo(){
        return MapUtil.toHashMap(deptInfo);
    }

    @SuppressWarnings("rawtypes")
    public void setDeptInfo(List<Map<String, Object>> mData){
        deptInfo = new ArrayList();
        deptInfo = MapUtil.mapToList(deptInfo, mData);
    }

    @SuppressWarnings("rawtypes")
    public HashMap getNatInfo(){
        return MapUtil.toHashMap(natInfo);
    }

    @SuppressWarnings("rawtypes")
    public void setNatInfo(List<Map<String, Object>> mData){
        natInfo = new ArrayList();
        natInfo = MapUtil.mapToList(natInfo, mData);
    }

    @SuppressWarnings("rawtypes")
    public HashMap getEtcInfo(){
        return MapUtil.toHashMap(etcInfo);
    }

    @SuppressWarnings("rawtypes")
    public void setEtcInfo(Map aEtcInfo){
        etcInfo = new ArrayList();
        etcInfo = MapUtil.toList(etcInfo, aEtcInfo);
    }

    @SuppressWarnings("rawtypes")
    public HashMap getCurSite(){
      return MapUtil.toHashMap(curSite);
    }

    @SuppressWarnings("rawtypes")
    public HashMap getCurDept(){
      return MapUtil.toHashMap(curDept);
    }

    @SuppressWarnings("rawtypes")
    public void setCurSite(HashMap input){
      curSite = new ArrayList();
      curSite = MapUtil.toList(curSite, input);
    }
    
    @SuppressWarnings("rawtypes")
    public void setCurDept(HashMap input){
      curDept = new ArrayList();
      curDept = MapUtil.toList(curDept, input);
    }

    public void addPriv(String privCd){
        if(privs == null){
            privs = new ArrayList<String>();
            curPriv = privCd;
        }
        privs.add(privCd);
    }

    public String getCurPriv(){
        if(curPriv == null)
            curPriv = "";
        return curPriv;
    }

    public void setCurPriv(String curPriv){
        this.curPriv = curPriv;
    }

    public boolean hasPriv(String targetPriv){
        if(privs == null){
            return false;
        }
        return privs.contains(targetPriv);
    }

    public boolean hasLoclCdPriv(String targetPriv){
        if(privs == null){
            return false;
        }
        if(privs.contains(targetPriv)){
            return true;
        }
        return false;
    }

    public List<String> getPrivs(){
        return privs;
    }

    public void setPrivs(List<String> privs){
        this.privs = privs;
    }

    public List<String> getSiteWhere(){
        return siteWhere;
    }

    public List<String> getDeptWhere(){
        return deptWhere;
    }

    public List<String> getPrivWhere(){
        return privWhere;
    }

    public void resetSiteWhere(){
        siteWhere = null;
        siteWhere = new ArrayList<String>();
    }

    public void resetDeptWhere(){
        deptWhere = null;
        deptWhere = new ArrayList<String>();
    }

    public void addSiteWhere(String siteCd){
        if(siteWhere == null){
            siteWhere = new ArrayList<String>();
        }
        siteWhere.add(siteCd);
    }

    public void addDeptWhere(String deptCode){
        if(deptWhere == null){
            deptWhere = new ArrayList<String>();
        }
        deptWhere.add(deptCode);
    }


    public void resetPrivWhere(){
        privWhere = null;
        privWhere = new ArrayList<String>();
    }

    public void addPrivWhere(String privCd){
        if(privWhere == null){
            privWhere = new ArrayList<String>();
        }
        privWhere.add(privCd);
    }

    public String getMenuCd(){
        return menuCd;
    }

    public String getMenuNm(){
        return menuNm;
    }

    public void setMenuNm(String menuNm){
        this.menuNm = menuNm;
    }

    public String getLoclCd(){
        if(loclCd == null)
            loclCd = "ko_KR";
        return loclCd;
    }

    public void setLoclCd(String aLoclCd){
        this.loclCd = aLoclCd;
    }

    public String getLanguage(){
        if(this.loclCd == null)
            this.loclCd = "ko_KR";
        String strLangCd = new Locale(this.loclCd).getLanguage();
        if(strLangCd.indexOf("_") > 0)
            return strLangCd.substring(0, strLangCd.indexOf("_"));
        else
            return strLangCd;
    }

    public String getUserNm(){
        return userNm;
    }

    public void setUserNm(String userNm){
        this.userNm = userNm;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getmPhone(){
        return mPhone;
    }

    public void setmPhone(String mPhone){
        this.mPhone = mPhone;
    }

    public String getUseYn(){
        return useYn;
    }

    public void setUseYn(String useYn){
        this.useYn = useYn;
    }

    public String getPasswd(){
        return password;
    }

    public void setPasswd(String passwd){
        this.password = passwd;
    }
    
    public String getNatCd(){
        return natCd;
    }

    public void setNatCd(String natCd){
        this.natCd = natCd;
    }

    public String getPriv(){
        if(curPriv == null)
            curPriv = "";
        return curPriv;
    }

    public void setOrgCd(String orgCd){
        this.orgCd = orgCd;
    }

    public void setUserTpCd(String userTpCd){
        this.userTpCd = userTpCd;
    }

    public void setUserEnm(String userEnm){
        this.userEnm = userEnm;
    }

    public void setPwdChgDt(Date pwdChgDt){
        this.pwdChgDt = pwdChgDt;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFaxno(String faxno){
        this.faxno = faxno;
    }

    public void setZipcd(String zipcd){
        this.zipcd = zipcd;
    }

    public void setBasAddr(String basAddr){
        this.basAddr = basAddr;
    }

    public void setDtlAddr(String dtlAddr){
        this.dtlAddr = dtlAddr;
    }

    public Object getLoginInfo(){
        return loginInfo;
    }

    public void setLoginInfo(Object loginInfo){
        this.loginInfo = loginInfo;
    }
    public String getOrgCdChg(){
        return orgCdChg;
    }

    public void setOrgCdChg(String orgCdChg){
        this.orgCdChg = orgCdChg;
    }
}
