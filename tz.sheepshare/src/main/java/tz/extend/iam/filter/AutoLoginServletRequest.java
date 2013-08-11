package tz.extend.iam.filter;

import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 자동 로그인 수행을 위한 위한 MockRequest, 클라이언트의 요청 객체와는 별도로 생성되어 세션범위의 인증객체를 생성한다.
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
public class AutoLoginServletRequest extends HttpServletRequestWrapper {

	/**
	 * <pre>
	 * 시스템 인증을 위한 MockRequest를 생성한다.
	 * </pre>
	 * 
	 * @param filterProcessingUrl
	 *            securityFilterChain에서 인증을 수행하는 virtual url
	 * @param usernameParameter
	 * @param username
	 *            사용자ID
	 * @param passwordParameter
	 * @param password
	 *            password
	 */
	public AutoLoginServletRequest(String filterProcessingUrl,
			String usernameParameter, String username,
			String passwordParameter, String password) {
		super(new MockHttpServletRequest());

		MockHttpServletRequest request = (MockHttpServletRequest) getRequest();

		request.setMethod(HttpMethod.POST.name());
		request.setRequestURI(filterProcessingUrl);
		request.setSession(new MockHttpSession());

		request.addParameter(usernameParameter, username);
		request.addParameter(passwordParameter, password);
	}
}
