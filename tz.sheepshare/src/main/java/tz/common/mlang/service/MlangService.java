package tz.common.mlang.service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : MlangService
 * 설    명 : 다국어 정보를 관리하는 서비스
 * 작 성 자 : TZ
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

import java.util.List;
import java.util.Map;
import java.util.Properties;

import tz.extend.query.CommonDao;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MlangService {
	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(MlangService.class);

	@Autowired
	@Qualifier("mainDB")
	private CommonDao dao;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

	/**
	 * 조회
	 *
	 * @param parameter
	 * @return
	 */
	public List<Map<String, Object>> retrieveMlangList(Map<String, Object> parameter) {
		logger.debug("Constants.sysCd=" + StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		parameter.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("mlang.retrieveMlangList", parameter);
	}
}
