package com.tz.sy.template.pt02.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tz.sy.template.pt01.domain.Employee;

import org.springframework.beans.factory.annotation.Qualifier;
import tz.extend.query.CommonDao;

@Service
public class MuitiDetailService {

	@Autowired
	@Qualifier("mainDB")
	private CommonDao commonDao;

	/**
	 * <p>
	 * 사원리스트 조회
	 * </p>
	 * <p>
	 * example:
	 * </p>
	 * <p>
	 * {@code
	 *
	 * }
	 * </p>
	 *
	 * @param jobLevelCode 직급
	 * @param orgCd 부서
	 * @param skillCode 스킬
	 * @return
	 */
	public List<Employee> retrieveEmployee(String jobLevelCode, String orgCd, String skillCode) {
		Map<String, String> map = new HashMap<String, String>();

		map.put("departmentCode", orgCd);
		map.put("joblevelCode", jobLevelCode);
		map.put("skillCode", skillCode);
		return commonDao.queryForList("MultiDetail.retrieveEmployeeList", map, Employee.class);
	}
}
