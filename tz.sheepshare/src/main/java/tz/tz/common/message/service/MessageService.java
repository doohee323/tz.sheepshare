package tz.common.message.service;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MessageService
 * 설    명 : 메시지 관리 Service Class
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.extend.util.FileUtil;
import tz.extend.util.SpringUtil;
import tz.extend.util.StringUtil;
import tz.extend.core.mvc.context.WebContext;
import tz.extend.core.message.DatabaseMessageSource;
import tz.extend.query.CommonDao;

import tz.common.locale.service.LocaleService;

@Service("messageService")
public class MessageService {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

	@Autowired
	@Qualifier("mainDB")
	private CommonDao dao;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    @Autowired
    private LocaleService localeService;

	/**
	 * 메세지 목록 조회
	 *
	 * @param input
	 * @return List<Map<String, Object>>
	 */
	public List<Map<String, Object>> retrieveMessageList(Map<String, Object> input) {
		return dao.queryForMapList("message.retrieveMessageList", input);
	}

	/**
	 * 메세지 상세 목록 조회
	 *
	 * @param input
	 * @return Map
	 */
	public Map<String, Object> retrieveMessage(Map<String, Object> input) {
		return dao.queryForMap("message.retrieveMessage", input);
	}

	/**
	 * 메세지를 조회하는 메소드
	 *
	 * @param input
	 * @return
	 */
	public String getMessage(Map<String, Object> input) {
		return dao.queryForMap("message.retrieveMessage", input).get("message").toString();
	}

	/**
	 * 메세지 존재 여부 확인을 하는 메소드
	 *
	 * @param input
	 * @return boolean
	 */
	public boolean isMessageExist(Map<String, Object> input) {
		if (!checkMessageExist(input).isEmpty())
			return true;
		return false;
	}

	/**
	 * 메세지 존재 여부 확인을 하는 메소드
	 *
	 * @param input
	 * @return Map
	 */
	public Map<String, Object> checkMessageExist(Map<String, Object> input) {
		return dao.queryForMap("message.checkMessageExist", input);
	}

	/**
	 * Ajax용 메시지를 생성한다.
	 * @param paramMap
	 * @return
	 */
    public String getMessageByAjax(Map<String, Object> paramMap){
        DatabaseMessageSource messageSource = (DatabaseMessageSource)SpringUtil.getBean(WebContext.getRequest(), "messageSource");
        String param = StringUtil.getText(paramMap.get("param"));
        String paramArray[] = param.split(",");
        String loclCd = StringUtil.getText(paramMap.get("loclCd"));
        if(loclCd.equals(""))
            loclCd = "ko_KR";
        String msg = "";
        try{
            if(loclCd.equals("ko_KR")) {
                msg = messageSource.getMessage(StringUtil.getText(paramMap.get("msgId")), new Object[] { param },
                        new Locale("ko", "KR"));
            } else {
                msg = messageSource.getMessage(StringUtil.getText(paramMap.get("msgId")), new Object[] { param },
                        new Locale("en", "US"));
            }
        }catch(Exception e){
            msg = messageSource.getMessage(StringUtil.getText(paramMap.get("msgId")), new Object[] { param },
                    new Locale("ko", "KR"));
        }
        return msg;
    }
}
