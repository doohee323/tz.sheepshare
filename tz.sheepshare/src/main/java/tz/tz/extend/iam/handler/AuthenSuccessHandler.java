package tz.extend.iam.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import tz.basis.iam.core.filter.handler.AbstractAuthenticationSuccessHandler;
import tz.extend.iam.authentication.UserDefinition;
import tz.extend.iam.handler.service.AddUserInfoService;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 사용자의 인증 요청이 성공한 경우 처리되어야 할 후처리 작업들을 정의한다.
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
public class AuthenSuccessHandler extends
		AbstractAuthenticationSuccessHandler {
	/**
	 * 로그처리를 위한 logger
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AuthenSuccessHandler.class);

	@Autowired(required = false)
	protected AddUserInfoService additionalUserService;

	/**
	 * <pre>
	 * 표준웹과 AJAX 요청을 구분하여 처리하기 위해, 요청객체가 AJAX 요청인지 판단하여 반환한다.
	 * </pre>
	 * 
	 * @return AJAX 요청인 경우 true
	 */
	@Override
	protected boolean isTzRequest(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");

		if (agent != null && (agent.indexOf("XPLATFORM") != -1)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <pre>
	 * 인증이 성공하였을 경우 호출되며, 상위 클래스의 기본적인 구현은 표준웹과 AJAX 요청을 구분하여 처리하는 흐름으로 기술되어 있다.
	 * 본 메소드를 재정의하고 상위 클래스의 onAuthenticationSuccess 메소드를 명시적으로 호출하지 않을 경우, onTzAuthenticationSuccess 가 수행되지 않음에 주의한다.
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @param authentication
	 *            인증객체
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		UserDefinition userDefinition = (UserDefinition) authentication
				.getPrincipal();
		logger.info(userDefinition.getUserId() + "("
				+ userDefinition.getUserNm() + ")님이 접속하였습니다.");
		/*
		 * 인증부가정보 처리
		 */
		if (additionalUserService != null) {

			additionalUserService.addUserInfo(request, userDefinition);
		}

		/*
		 * 처리 흐름을 상위 클래스로 위임
		 */
		super.onAuthenticationSuccess(request, response, authentication);
	}

	/**
	 * <pre>
	 * AJAX 요청으로 인증한 경우에 한해 UI와의 통신을 위한 별도의 로직을 작성하기 위한 메소드이며, 필요에 따라 재정의하여 사용한다.
	 * </pre>
	 * 
	 * @param request
	 * @param response
	 * @param authentication
	 *            인증객체
	 */
	@Override
	protected void onTzAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) {

	}
}
