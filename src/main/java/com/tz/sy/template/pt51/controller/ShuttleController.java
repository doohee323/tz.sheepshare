package com.tz.sy.template.pt51.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.sy.template.common.service.PatternCommonService;
import com.tz.sy.template.pt01.domain.Employee;
import com.tz.sy.template.pt02.service.MuitiDetailService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;


/**
*
* UI 템플릿 (Shuttle) 처리를 위한 Controller
*
* @author LHF
*
*/
@Controller
@RequestMapping("/pattern/pt51/shuttle/*")
public class ShuttleController {

	@Autowired
	private PatternCommonService commonService;

	@Autowired
	private MuitiDetailService employeeService;

	@RequestMapping("initOrgCd.*")
	public void initEmployeeInfo(TzRequest request, TzResponse response) {
		List<Map<String, Object>> orgCdList = commonService.getDepartmentCodeList();

		response.setList("output1", orgCdList);
	}

	@RequestMapping("retrieveEmployeeList.*")
	public void retrieveEmployeeList(TzRequest request, TzResponse response) {
/*
		// 강제 에러 테스트
		if(true){
			throw new RuntimeException("aaaaaas");
		}
*/
		Map<String, Object> params = request.getParam();
		String orgCd = (String) params.get("queryDepartmentCode");

		List<Employee> frOrgCdList = employeeService.retrieveEmployee("", orgCd, "");
		List<Employee> toOrgCdList = employeeService.retrieveEmployee("", "0000200006", "");

		response.setList("output1", frOrgCdList, Employee.class);
		response.setList("output2", toOrgCdList, Employee.class);
	}
}

