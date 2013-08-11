package tz.common.login.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.common.login.service.LoginService;
import tz.common.util.service.UtilService;
import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * @author
 * 로그인 처리를 위한 콘트롤러
 *
 */
@Controller
@RequestMapping("/tz/common/login/*")
public class LoginController {

	@Autowired
	private UtilService service;

    @Autowired
    private LoginService loginService;

    @Autowired
	private MessageSourceAccessor messageSourceAccessor;

    /**
     * <PRE>
     * DB 조회를 통해 특정 값을 조회
     * </PRE>
     * @author
     */
    @RequestMapping("existUserId.*")
    public void retrieveSingleData(TzRequest request, TzResponse response) {
    	Map<String,Object> paramMap = request.get("params", HashMap.class);
        String msg = service.retrieveSingleData(paramMap);
        if(msg == null) {
            Object[] arguments = null;
            msg = messageSourceAccessor.getMessage("sy.err.noUserId", arguments);
        } else {
            msg = "";
        }
        response.addSuccessMessage(msg);
    }

    /**
     * <pre>
     *  logout
     * </pre>
     * @param
     * @param
     */
    @RequestMapping("logout.*")
    public void logout(TzRequest request, TzResponse response)  {
        Map<String, Object> input = request.getParam();
        loginService.logOut(input);
    }

    /**
     * <PRE>
     * 본사 / Site 포탈용
     * 특정 Site에서 접근하는 로그인 페이지에 전달해야 하는 변수 처리용
     * http://sheepsharedev.sheepshare.com/sheepshare/tz/common/login/loginPage.ajax?groupId=102
     * </PRE>
     * @author
     * @return
     * @return
     */
    @RequestMapping("loginPageSvc.*")
    public void loginPageSvc(TzRequest request, TzResponse response) {
        Map<String,Object> paramMap = request.getParam();
        response.setViewName("/jsp/common/Login.jsp");
    }

}
