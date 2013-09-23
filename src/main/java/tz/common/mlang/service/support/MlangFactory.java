package tz.common.mlang.service.support;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : MlangFactory
 * 설    명 : 다국어 정보를 메모리에 캐쉬
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import javax.annotation.PostConstruct;

import tz.common.locale.service.LocaleService;
import tz.common.mlang.service.MlangService;
import tz.extend.core.mvc.context.WebContext;
import tz.extend.util.FileUtil;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class MlangFactory implements Observer {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(MlangFactory.class);

    @Autowired
    private MlangService service;

    @Autowired
    private LocaleService localeService;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    /**
     * tz.mLang.cache.useYn 여부에 따라 다국어 정보를 cache 한다.
     */
	@PostConstruct
	public void init() {
		try {
		    if(StringUtil.getText(appProperties.get("tz.mLang.cache.useYn")).equals("false")) return;
//		    List<Map<String, Object>> mData = localeService.retrieveLocaleList(new HashMap<String, Object>());
		} catch (Exception e) {
		    logger.error(e.getMessage());
		}
	}

    /**
     * 해당 다국어의 메시지 목록을 조회한다.
     */
	public List<Map<String, Object>> findMlangInfo(String aJavaLoclCd) {
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("loclCd", aJavaLoclCd);
		return service.retrieveMlangList(input);
	}
	
    /**
     * 해당 다국어의 메시지 정보를 조회한다.
     */
    public String findMlng(List<Map<String, Object>> mLang, String col) {
        for(int i=0;i<mLang.size();i++) {
            if(StringUtil.getText(mLang.get(i)).equals(col)) {
                return StringUtil.getText(mLang.get(i).get("mlangAbbrnm"));
            }
        }
        return col;
    }

	/**
	 * MlangFactory에서 사용하는 Map을 초기화한다.
	 */
	public void refresh() {
		synchronized (this) {
		    init();
		}
	}

	/**
	 * <PRE>
	 * 메모리에 Cache 된 다국어 정보를 초기화한다.
	 * </PRE>
	 *
	 * @param o
	 * @param arg
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable o, Object arg) {
		this.refresh();
	}
}

