package com.tz.sy.template.pt41.controller;

import java.util.List;

import tz.basis.data.GridData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.sy.template.pt01.domain.Employee;
import com.tz.sy.template.pt01.service.SingleDetailService;
import com.tz.sy.template.pt41.domain.Achieve;
import com.tz.sy.template.pt41.service.MasterDetailService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;

/**
 * UI 템플릿 (Master/Detail) 처리를 위한 Controller
 *
 * @author LHF
 */
@Controller
@RequestMapping("/pattern/pt41/masterdetail/*")
public class MasterDetailController {

	@Autowired
	private SingleDetailService employeeService;

	@Autowired
	private MasterDetailService masterDetailService;

	@RequestMapping("retrieveEmployeeAchievement.*")
	public void retrieveEmployeeAchievement(TzRequest request, TzResponse response) {
		String queryNum = request.getParam("queryNum");
		String queryYear = request.getParam("queryYear");
		String queryMonth = request.getParam("queryMonth");

		Employee employee = employeeService.retrieveEmployee(queryNum, "");
		List<Achieve> achievement = masterDetailService.retrieveAchievementList(queryNum, queryYear, queryMonth);

		response.set("output1", employee, Employee.class);
		response.setList("output2", achievement, Achieve.class);
	}

	@RequestMapping("saveEmployeeAchievement.*")
	public void saveEmployee(TzRequest request, TzResponse response) {
		GridData<Employee> employees = request.getGridData("input1", Employee.class);
		GridData<Achieve> achievements = request.getGridData("input2", Achieve.class);

		masterDetailService.saveEmployeeAchievement(employees, achievements);
	}

	@RequestMapping("removeEmployee.*")
	public void removeEmployee(TzRequest request, TzResponse response) {
		//Map<String,Object> paramMap = request.getParam();
		//masterDetailService.removeEmployee(paramMap);
	}

}
