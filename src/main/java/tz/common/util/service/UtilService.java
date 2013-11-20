package tz.common.util.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.extend.util.StringUtil;
import tz.extend.iam.UserInfo;
import tz.extend.query.CommonDao;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : UtilService
 * 설    명 : Util 처리를 위한 service 클래스
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
@Service
public class UtilService {

	/**
	 * DB처리를 위한 공통 dao
	 */
	@Autowired
	@Qualifier("mainDB")
	private CommonDao dao;

	/**
	 * <pre>
	 * DB 조회를 통해 특정 값을 조회
	 * </pre>
	 *
	 * @param data
	 * @return @
	 */
	public List<Map<String, Object>> retrieveSimpleData(Map<String, Object> data) {
		String qUrlQuery = StringUtil.getText(data.get("qUrlQuery"));
		if (qUrlQuery.indexOf("?") > -1) {
			String tmp = qUrlQuery;
			qUrlQuery = tmp.substring(0, qUrlQuery.indexOf("?"));
			tmp = tmp.substring(tmp.indexOf("?") + 1, tmp.length());
			String tmpArry[] = tmp.split("&");
			if (tmpArry.length > 0) {
				for (int i = 0; i < tmpArry.length; i++) {
					String tmp2Arry[] = tmpArry[i].split("=");
					if (tmp2Arry.length > 1) { // 파라메타 있을때만..
						data.put(tmp2Arry[0], tmp2Arry[1]);
					}
				}
			}
		}
		return dao.queryForMapList(qUrlQuery, data);
	}

	/**
	 * <pre>
	 * DB 조회를 통해 특정 값을 조회
	 * </pre>
	 *
	 * @param data
	 * @return
	 */
	public String retrieveSingleData(Map<String, Object> data) {
		String qUrlQuery = StringUtil.getText(data.get("qUrlQuery"));
		String tmpArry[] = qUrlQuery.split("&");
		if (tmpArry.length > 0) {
			for (int i = 0; i < tmpArry.length; i++) {
				String tmp2Arry[] = tmpArry[i].split("=");
				if (tmp2Arry.length > 1) { // 파라메타 있을때만..
					data.put(tmp2Arry[0], tmp2Arry[1]);
				}
			}
		}

		String sVal = "";
		String qId = StringUtil.getText(data.get("qId"));
		if (qId.startsWith("/")) {
			qId = qId.replace("/", ".");
			qId = qId.substring(1, qId.length());
			sVal = (String) dao.queryForObject(qId, data, String.class);
		} else if (qId.indexOf(".") >= 0) {
			sVal = (String) dao.queryForObject(qId, data, String.class);
		}

		return sVal;
	}

	/**
	 * <pre>
	 * 우편번호 리스트 조회
	 * </pre>
	 *
	 * @param input 입력파라미터
	 * @return Query를 수행한 결과
	 * @exception Exception
	 */
	public List<Map<String, Object>> retrieveZpcdListPopup(Map<String, Object> input) {
		return dao.queryForMapList("util.retrieveZpCdListPopup", input);
	}

	/**
	 * <pre>
	 * 우편번호 리스트 조회
	 * </pre>
	 *
	 * @param input 입력파라미터
	 * @return Query를 수행한 결과
	 * @exception Exception
	 */
	public List<Map<String, Object>> retrieveZpcdList(Map<String, Object> input) throws Exception {
		return dao.queryForMapList("util.retrieveZpCdListPopup", input);
	}

	/**
	 * <pre>
	 * elapId 추출
	 * </pre>
	 *
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> getEaElapId(Map<String, Object> input) {
		return dao.queryForMapList("/ea/eaz/d99/common/getEaElapId", input);
	}

	/**
	 * <pre>
	 * 사용자 리스트를 팝업으로 보여줄때 사용되는 메소드
	 * </pre>
	 *
	 * @param input
	 * @return @
	 */
	public List<Map<String, Object>> retrieveUserListPopup(Map<String, Object> input) {
		return dao.queryForMapList("select/tz/common/util/retrieveUserListPopup", input);
	}

	/**
	 * <pre>
	 * retrieveOrgListCombo
	 * </pre>
	 *
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> retrieveOrgListCombo(Map<String, Object> input) {
		return dao.queryForMapList("select/pattern/pattern61/org/retrieveOrgListCombo", input);
	}

	/**
	 * <pre>
	 * retrieveOrgListPopup
	 * </pre>
	 *
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> retrieveOrgListPopup(Map<String, Object> input) {
		return dao.queryForMapList("select/tz/common/util/retrieveOrgListPopup", input);
	}

	/**
	 * <pre>
	 * retrieveMenuListPopup
	 * </pre>
	 *
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> retrieveMenuListPopup(Map<String, Object> input) {
		return dao.queryForMapList("select/tz/common/util/retrieveMenuListPopup", input);
	}

	/**
	 * <pre>
	 * 전체 Site 목록 조회를 위한 메소드
	 * </pre>
	 *
	 * @param input
	 * @return List<Map<String, Object>> @
	 */
	public List<Map<String, Object>> retrieveSiteListPopup(Map<String, Object> input) {
		return dao.queryForMapList("select/tz/common/util/retrieveSiteListPopup", input);
	}

	/**
	 * <pre>
	 * 동적 sql를 통한 조회를 위한 메소드
	 * </pre>
	 *
	 * @param input
	 * @return List<Map<String, Object>> @
	 */
	public List<Map<String, Object>> retrieveDynamicQuery(Map<String, Object> input) {
		return dao.queryForMapList("select/tz/common/util/retrieveDynamicQuery", input);
	}

	/**
	 * <pre>
	 * 동적 sql를 통한 조회를 위한 메소드
	 * </pre>
	 *
	 * @param input
	 * @return List<Map<String, Object>> @
	 */
	public String retrieveDBSequence(String strSeqNm) {
		Map<String, Object> input = new HashMap<String, Object>();
		String groupId = UserInfo.getGroupId();
		input.put("seqNm", strSeqNm);
		input.put("groupId", groupId);
		return (String) dao.queryForObject("util.retrieveSequence", input, String.class);
	}

	/**
	 * <pre>
	 * SYSDATE 조회
	 * </pre>
	 *
	 * @param
	 * @return List<Map<String, Object>> @
	 */
	public String retrieveSysDate() {
		return (String) dao.queryForObject("util.retrieveSysDate", new HashMap<String, Object>(), String.class);
	}
}
