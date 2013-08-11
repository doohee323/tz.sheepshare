package tz.common.code.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import tz.common.code.CodeData;
import tz.extend.config.Constants;
import tz.extend.query.CommonDao;
import tz.extend.util.MapUtil;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통모듈
 * 프로그램 : CodeService
 * 설    명 : 공통코드 처리를 위한 service 클래스
 * 작 성 자 :
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
@Service("CodeService")
public class CodeService {
	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(CodeService.class);

	/**
	 * DB처리를 위한 공통 dao
	 */
	@Autowired
	@Qualifier("mainDB")
	private CommonDao dao;

	@Autowired
	private CodeData codeData;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;
    
	/**
	 * 모든 코드그룹키 목록조회.
	 *
	 * @return List<Map<String, Object>>[code]
	 */
	public List<Map<String, Object>> commGrpCdKeyList() {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.commGrpCdKeyList", input);
	}

	/**
	 * 모든 코드그룹키/값 목록조회.
	 *
	 * @param locale
	 * @param dutySysCd
	 * @return
	 */
	public List<Map<String, Object>> commGrpCdValueList() {
		logger.debug("Constants.sysCd1=" + Constants.sysCd);
		logger.debug("config.sysCd=" + StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.commGrpCdValueList", input);
	}

	/**
	 * 모든 코드그룹키/값 목록조회.
	 *
	 * @param locale
	 * @param dutySysCd
	 * @return
	 */
	public List<Map<String, Object>> commGrpCdValueList(Map<String, Object> input) {
		logger.debug("Constants.sysCd2=" + Constants.sysCd);
		logger.debug("config.sysCd=" + StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		input.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.commGrpCdValueList", input);
	}

	/**
	 * 주어진 코드그룹에 해당하는 코드키 목록조회.
	 *
	 * @param comm_grp_cd
	 * @return List<Map<String, Object>>[code]
	 */
	public List<Map<String, Object>> codeKeyList(String commGrpCd) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("commGrpCd", commGrpCd);
		param.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.codeKeyList", param);
	}

	/**
	 * 주어진 코드그룹에 해당하는 코드키/값 목록조회. - value는 MESSAGE_로케일 테이블의 해당 값.
	 *
	 * @return List<Map<String, Object>>[code,value,comm_grp_cd]
	 */
	public List<Map<String, Object>> codeValueList(Map<String, Object> inputData) {
		inputData.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.codeValueList", inputData);
	}

	/**
	 * 주어진 코드그룹에 해당하는 코드키/값 목록조회
	 *
	 * @param commGrpCd
	 * @param locale
	 * @param sortCodeNotnull
	 * @return
	 */
	public List<Map<String, Object>> codeValueAllList(List commGrpCd, Map<String, Object> inputData) {
//		if (commGrpCd.size() > 0) {
//			inputData.put("commGrpCd", commGrpCd);
//		}
		if (StringUtil.getText(inputData.get("sortCodeNotnull")).equals("")) {
			inputData.put("sortCodeNotnull", "Y");
		}
		inputData.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.codeValueAllList", inputData);
	}

	/**
	 * 쿼리를 받아 처리하는 확장 콤보 지원
	 *
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> getExtCodeList(Map<String, Object> inputData) {
		inputData.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList(inputData.get("queryString").toString(), inputData);
	}

	/**
	 * 메모리에서 공통코드 목록 조회
	 *
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> retrieveCodeList(Map<String, Object> data) {
		MapUtil.setNullString(data);
		String commGrpCd = StringUtil.getText(data.get("queryCommGrpCd"));
		if (commGrpCd.equals(""))
			commGrpCd = data.get("commGrpCd").toString();
		List<Map<String, Object>> mData = codeData.codeList(commGrpCd);
		String queryCommCd = StringUtil.getText(data.get("queryCommCd"));
		String queryCommCdNm = StringUtil.getText(data.get("queryCommCdNm"));
		List<Map<String, Object>> newData = new ArrayList<Map<String, Object>>();
		if (!queryCommCd.equals("") || !queryCommCdNm.equals("")) {
			for (int i = 0; i < mData.size(); i++) {
				boolean bChk = false;
				String code = mData.get(i).get("code").toString();
				String value = mData.get(i).get("value").toString();
				if (!queryCommCd.equals("") && !queryCommCdNm.equals("")) {
					if (code.indexOf(queryCommCd) > -1 && value.indexOf(queryCommCdNm) > -1)
						bChk = true;
				} else {
					if (!queryCommCd.equals("") && code.indexOf(queryCommCd) > -1)
						bChk = true;
					if (!queryCommCdNm.equals("") && value.indexOf(queryCommCdNm) > -1)
						bChk = true;
				}
				if (bChk)
					newData.add(newData.size(), mData.get(i));
			}
		} else {
			newData = (List<Map<String, Object>>) mData;
		}

		if (!commGrpCd.equals("")) {
			String v_header = "commCd:STRING," + "commCdNm:STRING," + "commGrpCd:STRING";
			newData = MapUtil.changeLMultiHeader(newData, v_header);
		}
		return newData;
	}

	/**
	 * 공통코드 리스트를 가져오는 메소드
	 *
	 * @return Query를 수행한 결과
	 */
	public List<Map<String, Object>> retrieveCodeData(Map<String, Object> input) {
		input.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
		return dao.queryForMapList("code.retrieveCodeData", input);
	}
}
