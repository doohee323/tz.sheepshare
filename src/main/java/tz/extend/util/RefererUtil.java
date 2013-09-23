package tz.extend.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.extend.util.config.ConfigUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : RefererUtil
 * 설      명 : Referer 접근통제 처리를 위한 Utility Class
 * 작 성 자 : TZ
 * 작성일자 : 2013-12-05
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-07             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
@Service
public class RefererUtil {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(RefererUtil.class);

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    /**
     * <pre>
     * 체크에서 제외할 URL 목록을 로딩.
     * </pre>
     * @param url
     */
    public boolean checkFilterUrl(String url){
        String useYn = StringUtil.getText(ConfigUtil.getProperties(appProperties).get("tz.referer.useYn"));
        if(!useYn.equals("true")) return true;

        if (url.equals("")) return true;
        if(url.indexOf("!") > 0)
            url = url.substring(url.indexOf("!") + 1, url.length());
        String include = StringUtil.getText(ConfigUtil.getProperties(appProperties).get("tz.referer.list"));
        String includeArry[] = include.split(",");
        boolean bChk = false;
        if(!include.trim().equals("")){
            if(!include.trim().equals("*")){
                for(int i = 0; i < includeArry.length; i++){
                    String str = includeArry[i];
                    if(str.endsWith("*")) str = str.substring(0, str.indexOf("*"));
                    str = StringUtil.LRTrim(str);
                    if(url.indexOf(StringUtil.Trim(str)) > -1){
                        bChk = true; // O
                        break;
                    }
                }
            }else{
                bChk = true; // O
            }
        }
        return bChk; // X
    }
}
