package tz.common.login.service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : LoginService
 * 설    명 : Login 처리를 위한 Service
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

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.basis.iam.core.common.util.UserInfoHolder;
import tz.extend.core.exception.BizException;
import tz.extend.core.message.MessageCodes;
import tz.extend.core.mvc.context.WebContext;
import tz.extend.iam.authentication.UserDefinition;
import tz.extend.query.CommonDao;
import tz.extend.security.SecurityUtil;
import tz.extend.util.DateUtil;
import tz.extend.util.StringUtil;

@Service("loginService")
public class LoginService {

    /**
     * 로그처리를 위한 logger
     */
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    @Autowired
    @Qualifier("mainDB")
    private CommonDao commonDao;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;
    
    /**
     * 로그인을 위한 사용자 정보 상세조회
     *
     * @param Map[input member_id, member_pwd]
     * @return
     * @throws Exception
     */
    public UserDefinition memberLogin(Map<String, Object> data, UserDefinition userInfo){
        try{
            
            Map<String, Object> input = (Map<String, Object>)((HashMap)data).clone();
            String userId = StringUtil.getText(data.get("tUserId"));
            String password = SecurityUtil.base64Encode(StringUtil.getText(data.get("j_password")));
            userInfo.setPasswd(password);

            logger.debug("memberLogin() userId :" + userId);
            input.put("useYn", "Y");

            String userTpCd = userInfo.getUserTpCd();	//사용자유형

            //사용자 유형별 Session Make
            logger.debug("memberLogin() userTpCd:" + userTpCd);
            input.put("userId", userInfo.getUserId());
            if(userTpCd.equals("01") || userTpCd.equals("02")){ //내부사용자, 내부임시사용자
            }else{
            }
            
            //전체일경우
            input.put("userId", userInfo.getUserId());
            input.put("userTpCd", userTpCd);
        }catch(Exception e){
            logger.error(e.getLocalizedMessage());
            throw new BizException(e.getMessage());
        }
        return toUserData(userInfo);
    }

    /**
    *
    * 로그인 페이지 결정
    * @param input
    * @return
    */
    public Map<String, Object> getIndexPage(Map<String, Object> input){
        Map<String, Object> output = new HashMap<String, Object>();
        ServletOutputStream oout = null;
        try{
            HttpServletRequest req = WebContext.getRequest();
            HttpServletResponse res = WebContext.getResponse();
            oout = res.getOutputStream();
            res.setContentType("text/html"); //mht
            StringBuffer html = new StringBuffer();
            html.setLength(0);
            html.append("<!DOCTYPE HTML PUBLIC  \"-//W3C//DTD HTML 4.01//EN\">\n");
            html.append("<html>\n");
            html.append("<head>\n");
            html.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n");
            html.append("<title></title>\n");
            html.append("</head>\n");
            html.append("<body>\n");
            html.append("<form name='aform' id='aform' action='" + req.getContextPath()
                    + "/tz/common/login/loginPageSvc.ajax' method='POST'>\n");
            Enumeration<String> names = req.getParameterNames();
            while(names.hasMoreElements()){
                String code = (String)names.nextElement();
                String value = req.getParameter(code);
                html.append("  <input type=\"hidden\" id=\"" + code + "\" name=\"" + code + "\" value=\"" + value
                        + "\">\n");
            }
            html.append("</form>\n");
            html.append("<script type=\"text/javascript\">\n");
            html.append("    document.getElementById(\"aform\").submit();\n");
            html.append("</script>\n");
            html.append("</body>\n");
            html.append("</html>\n");
            byte[] b = html.toString().getBytes("UTF-8");
            oout.write(b, 0, b.length);
            oout.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                oout.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        return output;
    }

    /**
     * 사용자정보 Map를 UserData로 변환
     *
     * @param result
     * @return null if result is empty.
     */
    private UserDefinition toUserData(UserDefinition userInfo){
        if(userInfo == null){
            return null;
        }

        userInfo.setIpAddress(WebContext.getRequest().getRemoteAddr());
        userInfo.setAccessTm(DateUtil.getCurrentDateString("yyyyMMddHHmmss"));
        if(userInfo.getSource() == null){
            userInfo.setSource(WebContext.getRequest().getHeader("referer"));
        }

        logger.info("toUserData() userData.getSource() : " + userInfo.getSource());
        return userInfo;
    }

    /**
     * 패스워드를 변경하는 메소드
     */
    public void changePwd(Map<String, Object> input){
        try{
            String userId = input.get("tUserId").toString();
            String dencodedUserId = new String(SecurityUtil.base64Decode(userId));
            dencodedUserId = URLDecoder.decode(dencodedUserId, "utf-8");

            String userPwd = input.get("tUserPwd").toString();
            String dencodedPwd = new String(SecurityUtil.base64Decode(userPwd));
            dencodedPwd = URLDecoder.decode(dencodedPwd, "utf-8");

            String newPwd = input.get("newPwd").toString();
            newPwd = new String(SecurityUtil.base64Decode(newPwd));
            newPwd = URLDecoder.decode(newPwd, "utf-8");
            input.put("userId", dencodedUserId);
            if(input.get("userId").equals("") && input.get("loginId").equals(""))
                throw new BizException(MessageCodes.MSG_ERROR_UPDATE);

            UserDefinition result = UserInfoHolder.getUserInfo(UserDefinition.class);

            // 패스워드 불일치 확인
            String pwd = result.getPassword();
            if(!pwd.equals("")){
                String digestedPwd = "";
                if(pwd.length() == 24){
                    digestedPwd = SecurityUtil.Encode(dencodedPwd);
                    newPwd = SecurityUtil.Encode(newPwd);
                }else{
                    digestedPwd = SecurityUtil.makeUniqueKey(dencodedPwd);
                    newPwd = SecurityUtil.makeUniqueKey(newPwd);
                }
                if(!digestedPwd.equals(result.getPassword())){
                    logger.info("pwd is different :" + input.get("userPwd") + "/" + digestedPwd + "/"
                            + SecurityUtil.Decode(digestedPwd));
                    throw new BizException(MessageCodes.MSG_ERROR_PWD);
                }

                input.put("pwd", newPwd);
                commonDao.update("loginXP.changePwd", input);
            }
        }catch(Exception e){
            WebContext.getRequest().setAttribute("msg", MessageCodes.MSG_ERROR_UPDATE);
            throw new BizException(e.getMessage());
        }
    }

    /**
     * 로그아웃을 위한 처리
     *
     * @param Map[input member_id, member_pwd]
     * @return
     */
    public void logOut(Map<String, Object> input){
        WebContext.removeUserSessionId();
        WebContext.getRequest().getSession().invalidate();

        WebContext.setRequest(WebContext.getRequest());
        WebContext.setResponse(WebContext.getResponse());

        if(StringUtil.getText(input.get("source")).equals("front")){
            WebContext.getRequest().setAttribute("href", WebContext.getRequest().getHeader("referer"));
        }else if(StringUtil.getText(input.get("source")).equals("index")){
            WebContext.getRequest().setAttribute("href", input.get("url"));
        }
    }
}
