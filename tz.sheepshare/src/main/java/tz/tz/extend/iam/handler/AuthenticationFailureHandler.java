package tz.extend.iam.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;

import tz.basis.iam.core.filter.handler.AbstractAuthenticationFailureHandler;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 사용자의 인증 요청이 실패한 경우 처리되어야 할 후처리 작업들을 정의한다.
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
public class AuthenticationFailureHandler extends AbstractAuthenticationFailureHandler {

    /**
     * <pre>
     * 표준웹과 AJAX 요청을 구분하여 처리하기 위해, 요청객체가 AJAX 요청인지 판단하여 반환한다.
     * </pre>
     * 
     * @param request
     *            HttpServletRequest
     * 
     * @return AJAX 요청인 경우 true
     * 
     */
    @Override
    protected boolean isTzRequest(HttpServletRequest request){
        return false;
    }

    /**
     * <pre>
     * AJAX 요청으로 인증을 시도한 경우에 한해 UI와의 통신을 위한 별도의 로직을 작성하기 위한 메소드이며, 필요에 따라 재정의하여 사용한다.
     * </pre>
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @param exception
     *            인증이 실패한 원인을 포함하는 exception
     */
    @Override
    protected void onTzAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception){
        System.out.println("");
    }

}
