package tz.extend.iam.filter;

import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.mock.web.MockHttpServletResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 자동 로그인 수행을 위한 MockResponse
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
public class AutoLoginServletResponse extends HttpServletResponseWrapper {

	/**
	 * <pre>
	 * 시스템 자동 인증을 위한 MockResponse를 생성한다.
	 * </pre>
	 */
	public AutoLoginServletResponse() {
		super(new MockHttpServletResponse());
	}

}
