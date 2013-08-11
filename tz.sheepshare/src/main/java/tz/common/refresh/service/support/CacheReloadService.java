package tz.common.refresh.service.support;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.common.code.CodeCache;
import tz.common.mlang.service.support.MlangFactory;
import tz.extend.core.message.DatabaseMessageSource;
import tz.extend.core.mvc.context.WebContext;
import tz.extend.util.SpringUtil;
import tz.extend.util.StringUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : CacheReloadService
 * 설    명 : 각종 어플리케이션 모듈 및 자원을 초기화
 * 작 성 자 :
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
@Service
public class CacheReloadService {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(CacheReloadService.class);

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    /**
     * <pre>
     * 캐싱되어 있는 자원들을 새로이 캐쉬함
     * </pre>
     * @param sb
     * @param HashMap
     * @return
     */
    public StringBuffer refreshInfos(StringBuffer sb, Map<String, Object> input){
        String types = StringUtil.getText(input.get("types"));
        types = " " + types;
        String allTypes = StringUtil.getText(appProperties.get("tz.refresh.type"));

        if(types.equals(" ") || types.indexOf("message") > 0){
            try{
                DatabaseMessageSource messageSource = (DatabaseMessageSource) SpringUtil.getBean(WebContext.getRequest(), "messageSource");
                messageSource.reloadMessage();

//                MessageService msForXP = (MessageService) SpringUtil.getBean(WebContext.getRequest(), "messageService");
//                msForXP.writeMessageForXP();
            }catch(Exception e){
                sb.append("DatabaseMessageSource refresh [Fail]<br><br>");
            }
        }

        if(types.equals(" ") || types.indexOf("config") > 0){
            try{
                /* EnvironmentConfigProperties 정보 로딩 */
            }catch(Exception e){
                sb.append("EnvironmentConfigProperties refresh [Fail]<br><br>");
            }
        }
        if(types.equals(" ") || types.indexOf("code") > 0){
            try{
                /* 코드성 데이터 메모리에 로딩(캐시) */
                CodeCache codeCache = (CodeCache)SpringUtil.getBean(WebContext.getRequest(), "codeCache");
                codeCache.loadCodeCache();
                sb.append("codeCache refresh [OK]<br><br>");
            }catch(Exception e){
                sb.append("codeCache refresh [Fail]<br><br>");
            }
        }
        if(types.equals(" ") || types.indexOf("mlang") > 0){
            if(allTypes.indexOf("mlang") > -1){
                try{
                    MlangFactory mlangFactory = (MlangFactory)SpringUtil.getBean(WebContext.getRequest(),
                            "mlangFactory");
                    mlangFactory.refresh();
                    sb.append("mlangFactory refresh [OK]<br><br>");
                }catch(Exception e){
                    sb.append("mlangFactory refresh [Fail]<br><br>");
                }
            }
        }
        return sb;
    }
}
