package tz.extend.core.mvc.context;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.iam.UserInfo;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : WebContextFilter
 * 설    명 : 요청이 들어올 때마다 WebContext에 request, response 등을 세팅해주는 서블릿 필터 보통 이 필터는 다른 필터보다 먼저 적용되도록 설정할 것을 권장한다.
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
public class WebContextFilter implements Filter {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(WebContextFilter.class);

    ServletContext servletContext;
    protected Set<String> urlExtends = null;
    protected Set<String> includeUrls = null;
    protected FilterConfig config = null;

    public void init(FilterConfig aConfig) throws ServletException{
        config = aConfig;
        servletContext = config.getServletContext();
        loadExtendConfig();
    }

    private void loadExtendConfig() {
        String strUrlExtends = config.getInitParameter( "urlExtends" );
        if(strUrlExtends != null) {
            String arryUrlExtends[] = strUrlExtends.trim().split(",");
            List<String> urlExtendsList = Arrays.asList(arryUrlExtends);
            urlExtends = new HashSet<String>(urlExtendsList);
        }

        String strIncludeUrls = config.getInitParameter( "includeUrls" );
        if(strIncludeUrls != null) {
            String arryIncludeUrls[] = strIncludeUrls.trim().split(",");
            List<String> includeUrlList = Arrays.asList(arryIncludeUrls);
            includeUrls = new HashSet<String>(includeUrlList);
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException{

        HttpServletRequest req = (HttpServletRequest)request;
        System.out.println("HttpServletRequest.getRequestURL() : " + req.getRequestURL());
        HttpServletResponse res = (HttpServletResponse)response;
        boolean bChk = false;

        if( urlExtends == null && includeUrls == null ) {
            // 별도 필터파라미터가 web.xml에 정의되지 않았다면 <filter-mapping>에서 이미 필터링하는 것으로 간주한다
            bChk = true;
        } else if ( urlExtends != null ) {
            Iterator<String> it = urlExtends.iterator();
            while ( !bChk && it.hasNext() ) {
                String tmpJob = (String)it.next();
                if(req.getRequestURI().endsWith(tmpJob)) {
                    bChk = true;
                }
            }
        } else if(includeUrls != null) {
            Iterator<String> it = includeUrls.iterator();
            while ( !bChk && it.hasNext() ) {
                String tmpJob = (String)it.next();
                if(req.getRequestURI().indexOf(tmpJob) > -1) {
                    bChk = true;
                }
            }
        }

        if(bChk) {
            WebContext.setServletContext(servletContext);
            WebContext.setRequest(req);
            WebContext.setResponse(res);
            logger.debug("WebContext Request url => " + req.getRequestURI());
            WebContext.getDocRoot();
        }
        chain.doFilter(request, response);
    }

    /**
     * Take this filter out of service.
     */
    public void destroy(){
        this.urlExtends = null;
        this.includeUrls = null;
        this.config = null;
    }

}
