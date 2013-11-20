package tz.common.locale.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import tz.basis.data.GridData;
import tz.common.locale.domain.LoclInfo;
import tz.extend.query.CommonDao;
import tz.extend.query.callback.AbstractRowStatusCallback;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : LocaleService
 * 설    명 : 로케일정보를 조회하는 service 클래스
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
public class LocaleService {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(LocaleService.class);

	@Autowired
	@Qualifier("mainDB")
	private CommonDao dao;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

	/**
	 * 조회
	 *
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> retrieveLocaleList(Map<String, Object> input) {
	    String sysCd = "";
	    if(StringUtil.getText(input.get("sysCd")).equals("")) {
	        sysCd = StringUtil.getText(appProperties.get("tz.serverInfo.sysCd"));
	        logger.debug("Constants.sysCd = " + sysCd);
	        input.put("sysCd", StringUtil.getText(appProperties.get("tz.serverInfo.sysCd")));
	    }
		return dao.queryForMapList("locale.retrieveLocaleList", input);
	}
	/**
	 * <pre>
	 * User에 LoclCd update
	 * </pre>
	 * @param gridData
	 */
	public void updateUserLocl(GridData<LoclInfo> mData) {
		mData.forEachRow(new AbstractRowStatusCallback<LoclInfo>() {
			@Override
			public void insert(LoclInfo record, int rowNum) {
				dao.update("locale.updateUserLocl", record);
			}
		});
	}

}
