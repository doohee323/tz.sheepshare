package tz.extend.core.mvc;

import tz.basis.core.mvc.adaptor.TzResponseAdapter;
import tz.basis.core.mvc.adaptor.TzResponseIF;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : TZ Response Adapter
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
public class TzResponse extends TzResponseAdapter {

	/**
	 * <pre>
	 *  생성자
	 * </pre>
	 *
	 * @param response tz response
	 */
	public TzResponse(TzResponseIF response) {
		super(response);
	}

}
