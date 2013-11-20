package com.tz.sy.template.common.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.sy.template.common.service.PatternCommonService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;


/**
 *
 * UI 패턴 공통데이터(Combo등) 처리를 위한 Controller
 *
 * @author LHF
 *
 */
@Controller
@RequestMapping("/pattern/common/code/*")
public class PatternCommonController {

	@Autowired
	private PatternCommonService service;

	/**
	 *
	 * <p>
	 *  Form이 초기화될때 화면에서 필요로하는 공통정보(직무,부서,스킬코드)를 조회한다.
	 * </p>
	 *
	 * <p>
	 * example:
	 * </p>
	 *
	 * <p>
	 * {@code
	 *
	 * }
	 * </p>
	 * @param request
	 * @param response
	 */
	@RequestMapping("init.*")
	public void initEmployeeInfo(TzRequest request, TzResponse response) {
		List<Map<String, Object>> jobCodeList = service.getJobLevelCodeList();
		List<Map<String, Object>> orgCdList = service.getDepartmentCodeList();
		List<Map<String, Object>> skillCodeList = service.getSkillCodeList();

		response.setList("output1", jobCodeList);
		response.setList("output2", orgCdList);
		response.setList("output3", skillCodeList);
	}
}