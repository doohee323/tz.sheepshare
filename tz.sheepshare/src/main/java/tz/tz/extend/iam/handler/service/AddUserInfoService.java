package tz.extend.iam.handler.service;

import javax.servlet.http.HttpServletRequest;

import tz.extend.iam.authentication.UserDefinition;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 인증에 성공한 사용자를 대상으로 시스템에서 필요로하는 부가 정보를 조회/설정하는 작업을 수행한다.
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
 * @see tz.iam.handler.AuthenSuccessHandler
 * 
 */
public interface AddUserInfoService {

	/**
	 * 
	 * <pre>
	 *  시스템에서 관리되는 사용자 부가 정보를 추가한다.
	 * </pre>
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param userInfo
	 *            사용자 정보를 포함하는 인증 객체
	 */
	public abstract void addUserInfo(HttpServletRequest request,
			UserDefinition userInfo);

}