package tz.extend.core.message.ux;

import java.util.List;

import tz.extend.core.message.DefaultMessage;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : UX에서 필요로하는 메세지 파일을 생성한다.
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
public interface MessageSourceGenerator {

	/**
	 *
	 * <pre>
	 *  메세지 파일을 생성한다.
	 * </pre>
	 *
	 * @param messageResources 저장소로 부터 조회된 Message resources
	 */
	public void generateMessageSourceFile(List<DefaultMessage> messageResources);

}