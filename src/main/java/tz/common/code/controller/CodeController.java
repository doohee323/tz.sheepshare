package tz.common.code.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import tz.common.code.CodeData;
import tz.common.code.service.CodeService;

import tz.extend.util.DateUtil;
import tz.extend.util.ObjUtil;
import tz.extend.iam.UserInfo;
import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : CodeController
 * 설    명 : 공통코드 처리를 위한 controller 클래스
 * 작 성 자 : 배태일
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
@Controller
@RequestMapping("/tz/common/code/*")
public class CodeController {

	@Autowired
	private CodeService service;

	@Autowired
	private CodeData codeData;

	/**
	 * <pre>
	 *  공통콤보리스트 조회
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveCommCdComboList.*")
	public void retrieveCommCdComboList(TzRequest request, TzResponse response) {

		Map<String, Object> data = request.getParam();
		// data.put("userId", UserInfo.getUserId());
		// data.put("today", DateUtil.getCurrentDateString());

		List<Map<String, Object>> mData;
		if (ObjUtil.isNull(data.get("highLevelGroup"))) {
			mData = codeData.codeList(data.get("queryCommGrpCd").toString());
		} else {
			data.put("commGrpCd", data.get("queryCommGrpCd"));
			mData = service.codeValueList(data);
		}

		List<Map<String, Object>> mData2 = service.retrieveCodeList(data);

		response.setMapList("output1", mData);
		response.setMapList("output2", mData2);
	}

	/**
	 * <pre>
	 *  공통 코드 조회
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveCommCdList.*")
	public void retrieveCommCdList(TzRequest request, TzResponse response) {
		Map<String, Object> data = request.getParam();
		data.put("userId", UserInfo.getUserId());
		data.put("today", DateUtil.getCurrentDateString());
		List<Map<String, Object>> mData = service.retrieveCodeList(data);
		response.setMapList("output1", mData);
	}

	/**
	 * <pre>
	 *  공통 코드 조회
	 * </pre>
	 * @param request
	 * @param response
	 */
	@RequestMapping("retrieveSingleCommCd.*")
	public void retrieveSingleCommCd(TzRequest request, TzResponse response) {
		Map<String, Object> data = request.getParam();
		data.put("userId", UserInfo.getUserId());
		data.put("today", DateUtil.getCurrentDateString());
		List<Map<String, Object>> mData = service.retrieveCodeList(data);
		response.setMapList("output1", mData);
	}
}