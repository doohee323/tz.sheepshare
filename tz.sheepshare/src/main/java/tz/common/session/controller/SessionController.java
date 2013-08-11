package tz.common.session.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tz.basis.data.GridData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.common.session.domain.SyMenuBkmrk;
import tz.common.session.service.SessionService;

import tz.extend.config.Constants;
import tz.extend.util.StringUtil;
import tz.extend.iam.UserInfo;
import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : SessionController
 * 설    명 : 로그인 사용자의 자료 조회 controller 클래스
 * 작 성 자 : TZ
 * 작성일자 : 2013-12-05
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-07             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
@Controller
@RequestMapping("/tz/common/session/*")
public class SessionController {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(SessionController.class);

	@Autowired
	private SessionService sessionService;

	/**
	 * <pre>
	 *  retrieveLoadAllInfo
	 * </pre>
	 * @param
	 * @param
	 */
	@RequestMapping("retrieveLoadAllInfo.*")
	public void retrieveLoadAllInfo(TzRequest request, TzResponse response)  {
	    Map<String, Object> input = request.getParam();
		List<Map<String, Object>> sessionList = sessionService.retrieveSessionList(input);
		response.setList("session", sessionList);
	}

}