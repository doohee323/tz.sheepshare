package tz.common.security.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import tz.common.login.service.LoginService;
import tz.extend.iam.authentication.UserDefinition;
import tz.extend.iam.handler.service.AddUserInfoService;
import tz.extend.util.ClassUtils;
import tz.extend.util.CollectionUtil;
import tz.extend.util.DateUtil;
import tz.extend.util.ServletUtil;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통모듈
 * 프로그램 : AddUserInfoServiceImpl
 * 설    명 : 추가세션정보를 추가하기 위한
 *           AddUserInfoService 의 Sheepshare프로젝트 구현클래스
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-28
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-02-28    TZ   주석최추 작성
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
public class AddUserInfoServiceImpl implements AddUserInfoService {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private final static Logger logger = LoggerFactory.getLogger(AddUserInfoServiceImpl.class);

	/**
	 * 로그인 관련 서비스
	 */
	@Autowired
	private LoginService loginService;

	/*
	 * (non-Javadoc)
	 * @see tz.extend.iam.handler.service.AddUserInfoService#addUserInfo(javax.servlet.http.HttpServletRequest, tz.extend.iam.authentication.UserDefinition)
	 */
	public void addUserInfo(HttpServletRequest request, UserDefinition userInfo) {

        UserDefinition userInput = userInfo; 
		userInput.setAccessTm(DateUtil.getCurrentDateString("yyyyMMddHHmmss"));

		if (userInput.getSource() == null) {
			userInput.setSource(request.getHeader("referer"));
		}

		logger.debug("addUserInfo() sessionId 1 :" + request.getSession().getId());
		userInput.setIpAddress(request.getRemoteAddr());
		Map<String, Object> data = CollectionUtil.getData(request);
		data.put("tUserId", userInput.getUserId());
        data.put("tUserPwd", userInput.getPassword());
		data.put("source", ServletUtil.getData(request, "source"));

        String loclCd = StringUtil.getText(request.getParameter("loclCd"));
        if(!loclCd.equals("")) {
            userInput.setLoclCd(loclCd);
            data.put("loclCd", loclCd);
        }
        String oggCd = StringUtil.getText(request.getParameter("oggCd"));
        if(!oggCd.equals("")) {
            userInput.setCurSite(new HashMap());
        }
		userInput = loginService.memberLogin(data, userInput);
		logger.debug("addUserInfo() loginService.memberLogin() after : " + userInput.getUserId() + " : "+ userInput);

		try {
			ClassUtils.cloneObject(userInput, userInfo);
		} catch (Exception e) {
			logger.trace("DefaultPrivService.loadUserByUsername() : error occured - user authorization failed"+e.getStackTrace());
		}
		logger.debug("addUserInfo() sessionId 2 :" + request.getSession().getId());
		
	}
}