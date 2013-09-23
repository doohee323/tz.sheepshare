package tz.extend.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import tz.extend.core.mvc.context.ApplicationContextProvider;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : SpringUtil
 * 설    명 : Spring 연계 을 위한  Utility Class
 * 작 성 자 : TZ
 * 작성일자 : 2013.04.10
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
public class SpringUtil {

	public static ApplicationContext getApplicationContext() {
		return ApplicationContextProvider.getApplicationContext();
	}

	   /**
     * <pre>
     * getBean(HttpServletRequest req, String beanId)
     * </pre>
     *
     * @param req
     * @param beanId
     * @return
     */
    public static Object getBean(String beanId) {
        try {
            if(ApplicationContextProvider.getApplicationContext().containsBean(beanId)) {
                return ApplicationContextProvider.getApplicationContext().getBean(beanId);
            }
        } catch (Exception e) {
        }
         
        Object beanObject = null;
        // xml 파일에서 context를 가져 온다.
        // 업무 모듈별 xml 명명 규칙 : sy + "-servlet.xml"
        try {
            String dutySysCd = beanId.substring(0, 2).toLowerCase();
            ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("../config/" + dutySysCd + "-servlet.xml");
            if(ctx.containsBean(beanId)) {
                beanObject = ctx.getBean("fileEventDao");
            }
            return beanObject;
        } catch (Exception e) {
        }
        
        return beanObject;
    }
	
	/**
	 * <pre>
	 * getBean(HttpServletRequest req, String beanId)
	 * </pre>
	 *
	 * @param req
	 * @param beanId
	 * @return
	 */
	public static Object getBean(HttpServletRequest req, String beanId) {
	    try {
	        if(ApplicationContextProvider.getApplicationContext().containsBean(beanId)) {
	            return ApplicationContextProvider.getApplicationContext().getBean(beanId);
	        }
	    } catch (Exception e) {
	    }
	     
        Object beanObject = null;

        // DispatcherServlet으로 로딩된 context를 가져 온다.
		WebApplicationContext webApplicationContext = RequestContextUtils.getWebApplicationContext(req);
		if (webApplicationContext.containsBean(beanId)) {
			webApplicationContext.getBeanDefinitionNames();
			beanObject = webApplicationContext.getBean(beanId);
			return beanObject;
		}

		HttpSession hs = req.getSession();
		ServletContext sc = hs.getServletContext();
		// ContextLoaderListener으로 로딩된 context를 가져 온다.
		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sc);

		if (webApplicationContext.containsBean(beanId)) {
			beanObject = webApplicationContext.getBean(beanId);
			return beanObject;
		}

		if (RequestContextUtils.getLocaleResolver(req) == null) {
            // xml 파일에서 context를 가져 온다.
            // 업무 모듈별 xml 명명 규칙 : sy + "-servlet.xml"
            try {
                String dutySysCd = beanId.substring(0, 2).toLowerCase();
                ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("../config/" + dutySysCd + "-servlet.xml");
                if(ctx.containsBean(beanId)) {
                    beanObject = ctx.getBean("fileEventDao");
                }
                return beanObject;
            } catch (Exception e) {
            }
        }
		
		return beanObject;
	}
}
