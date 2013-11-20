package com.tz.sy.template.pt02.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.sy.template.pt01.domain.Employee;
import com.tz.sy.template.pt02.service.MuitiDetailService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;


/**
 *
 * UI 템플릿 (Muiti Detail, List/Detail) 처리를 위한 Controller
 *
 * @author LHF
 *
 */
@Controller
@RequestMapping("/pattern/pt02/multidetail/*")
public class MultiDetailController {

	@Autowired
	private MuitiDetailService service;

	@RequestMapping("retrieveEmployeeList.*")
	public void retrieveEmployee(TzRequest request, TzResponse response) {
		String jobLevelCode = request.getParam("queryJoblevelCode");
		String orgCd = request.getParam("queryDepartmentCode");
		String skillCode = request.getParam("querySkillCode");

		List<Employee> employeeList = service.retrieveEmployee(jobLevelCode, orgCd, skillCode);

		response.setList("output1", employeeList, Employee.class);
	}
}
