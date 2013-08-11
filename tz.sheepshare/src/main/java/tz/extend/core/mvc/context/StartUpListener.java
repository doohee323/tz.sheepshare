package tz.extend.core.mvc.context;

import java.util.HashMap;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tz.common.refresh.service.support.CacheReloadService;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : StartUpListener
 * 설    명 : 각종 어플리케이션 모듈 및 자원을 초기화하는 리스너.
 *
 *        이 리스너는 최초의 사용자요청이 접수될 때까지 기다리지 않고
 *        웹어플리케이션이 컨테이너에 배포되어 가동되는 즉시
 *        시스템을 최적의 준비상태로 만들기 위해 사용된다.
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
public class StartUpListener implements ServletContextListener {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(StartUpListener.class);

    @Autowired
    private CacheReloadService service;

	/**
	 * <pre>
	 * contextInitialized
	 * </pre>
	 */
    public void contextInitialized( ServletContextEvent event ) {
        StringBuffer sb = new StringBuffer();
        service.refreshInfos( sb, new HashMap<String, Object>() );
        logger.debug( "[StartUpListener] This webapp initialized successfully." );
    }

    public void contextDestroyed( ServletContextEvent event ) {}

}
