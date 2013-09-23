package com.tz.sy.template.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Qualifier;
import tz.extend.query.CommonDao;


@Service
public class PatternCommonService {

	@Autowired
	@Qualifier("mainDB")
	private CommonDao commonDao;

	/**
	 * <p>
	 * 직무등급을 조회한다.
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
	 * @return
	 */
	public List<Map<String, Object>> getJobLevelCodeList() {
		return commonDao.queryForMapList("PatternCommonCode.retrieveJobLevelCodeList", null);
	}

	/**
	 * <p>
	 * 부서코드를 조회한다.
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
	 * @return
	 */
	public List<Map<String, Object>> getDepartmentCodeList() {
		return commonDao.queryForMapList("PatternCommonCode.retrieveDepartmentCodeList", null);
	}

	/**
	 * <p>
	 * 기술자등급을 조회한다.
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
	 * @return
	 */
	public List<Map<String, Object>> getSkillCodeList() {
		return commonDao.queryForMapList("PatternCommonCode.retrieveSkillCodeList", null);
	}

	/**
	 * 결재 연동 번호 업데이트
	 */
	public int updateSign(Map<String, Object> inputData) {
		return commonDao.update("PatternCommonCode.updateSign", inputData);
	}
}
