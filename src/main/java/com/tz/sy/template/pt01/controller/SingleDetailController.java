package com.tz.sy.template.pt01.controller;

import java.util.Map;

import tz.basis.data.GridData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tz.sy.template.pt01.domain.Employee;
import com.tz.sy.template.pt01.service.SingleDetailService;

import tz.extend.core.mvc.TzRequest;
import tz.extend.core.mvc.TzResponse;


/**
*
* UI 패턴 (Single Detail) 처리를 위한 Controller
*
* @author LHF
*
*/
@Controller
@RequestMapping("/pattern/pt01/singledetail/*")
public class SingleDetailController {

	@Autowired
	private SingleDetailService service;

	/**
	 *
	 * <p>
	 *  사원정보를 조회한다.
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
	@RequestMapping("retrieveEmployee.*")
	public void retrieveEmployee(TzRequest request, TzResponse response) {
		Map<String,Object> paramMap = request.getParam();

		Employee employee = service.retrieveEmployee(paramMap);

		response.set("output1", employee, Employee.class);
	}

	/**
	 *
	 * <p>
	 *  사원정보를 추가/삭제/수정 한다.
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
	@RequestMapping("saveEmployee.*")
	public void saveEmployee(TzRequest request, TzResponse response) {
		GridData<Employee> gridData = request.getGridData("input1", Employee.class);

		if(gridData != null){
			service.saveEmployeeList(gridData);
		}
	}
}