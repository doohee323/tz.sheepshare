package tz.common.login.domain;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 :
 * 설    명 : 사용자 정보 정의 (SY_USER 사용자 정보를 참조 한다.)
 * 작 성 자 :
 * 작성일자 : 2013-05-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class LoginInfo {

    private String menuList;

    private String userPriv;

    private String menuPriv;

    private String pwdExpired;

//    private String loginYn;

    public void resetUserPriv(){
        userPriv = "";
    }

    public void resetMenuPriv(){
        menuPriv = "";
    }

    public void addUserPriv(String userPriv){
        this.userPriv = userPriv;
    }

    public void addMenuPriv(String menuPriv){
        this.menuPriv = menuPriv;
    }

    public void resetMenuList(){
        menuList = null;
    }

    public void addMenuList(String menuId){
        menuList = menuId;
    }

    public boolean hasMenuList(String menuId){
        if(menuList == null){
            return false;
        }
        return menuList.contains(menuId);
    }

    public String getUserPrivByMenuCd(String menuCd){
        if(menuList == null)
            return "";
        if(menuCd == null || menuCd.equals(""))
            return "";
        String menuListArry[] = menuList.split("|");
        String userPrivArry[] = userPriv.split("|");
        for(int i = 0; i < menuListArry.length; i++){
            if(menuListArry[i].equals(menuCd)){
                return userPrivArry[i];
            }
        }
        return "";
    }

    public String getMenuPrivByMenuCd(String menuCd){
        if(menuList == null)
            return "";
        if(menuCd == null || menuCd.equals(""))
            return "";
        String menuListArry[] = menuList.split("|");
        String menuPrivArry[] = userPriv.split("|");
        for(int i = 0; i < menuListArry.length; i++){
            if(menuListArry[i].equals(menuCd)){
                return menuPrivArry[i];
            }
        }
        return "";
    }

    public String getMenuList(){
        return menuList;
    }

    public void setMenuList(String menuList){
        this.menuList = menuList;
    }

    public String getPwdExpired(){
        return pwdExpired;
    }

    public void setPwdExpired(String pwdExpired){
        this.pwdExpired = pwdExpired;
    }

//    public String getLoginYn(){
//        return loginYn;
//    }
//
//    public void setLoginYn(String loginYn){
//        this.loginYn = loginYn;
//    }

    public void setUserPriv(String userPriv){
        this.userPriv = userPriv;
    }

    public String getUserPriv(){
        return userPriv;
    }

    public void setMenuPriv(String menuPriv){
        this.menuPriv = menuPriv;
    }

    public String getMenuPriv(){
        return menuPriv;
    }
}
