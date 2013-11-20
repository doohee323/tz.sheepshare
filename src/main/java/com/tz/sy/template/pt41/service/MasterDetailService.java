package com.tz.sy.template.pt41.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tz.basis.data.GridData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tz.sy.template.pt01.domain.Employee;
import com.tz.sy.template.pt01.service.SingleDetailService;
import com.tz.sy.template.pt41.domain.Achieve;

import org.springframework.beans.factory.annotation.Qualifier;
import tz.extend.query.CommonDao;


@Service
public class MasterDetailService {

	@Autowired
	@Qualifier("mainDB")
	private CommonDao commonDao;

	@Autowired
	private SingleDetailService employeeService;

	/**
	 *
	 * <p>
	 *  개인성과조회
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
	 * @param queryNum
	 * @param queryYear
	 * @param queryMonth
	 * @return
	 */
	public Achieve retrieveAchievement(String queryNum, String queryYear, String queryMonth) {
		Map<String, String> search = new HashMap<String, String>();

		search.put("num", queryNum);
		search.put("yyyy", queryYear);
		search.put("mm", queryMonth);

		return commonDao.queryForObject("MasterDetail.retrieveRecentAchievement", search,Achieve.class);
	}

	public List<Achieve> retrieveAchievementList(String queryNum, String queryYear, String queryMonth) {
		Map<String, String> search = new HashMap<String, String>();

		search.put("num", queryNum);
		search.put("yyyy", queryYear);
		search.put("mm", queryMonth);

		return commonDao.queryForList("MasterDetail.retrieveRecentAchievement", search, Achieve.class);
	}

	/**
	 *
	 * <p>
	 *  사원정보 / 개인성과 저장
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
	 * @param employees
	 * @param achievements
	 */
	public void saveEmployeeAchievement(GridData<Employee> employees, GridData<Achieve> achievements) {
		if(employees != null){
			employeeService.saveEmployeeList(employees);
		}

		if(achievements != null){
			this.saveAchievement(achievements);
		}
	}

	/**
	 *
	 * <p>
	 *  개인성과 저장
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
	 * @param achievements
	 */
	public void saveAchievement(GridData<Achieve> achievements) {
		for (int i = 0; i < achievements.size(); ++i) {
			Achieve achievement = achievements.get(i);
			String statementId = "";

			switch(achievements.getStatusOf(i)){
				case INSERT :
					statementId = "MasterDetail.insertAchievement";
					break;
				case UPDATE :
					statementId = "MasterDetail.updateAchievement";
					break;
				case DELETE :
					statementId = "MasterDetail.deleteAchievement";
					break;
			}

			commonDao.update(statementId, achievement);
		}
	}
}
