package tz.extend.iam.filter;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tz.extend.core.exception.BizException;
import tz.extend.core.mvc.context.WebContext;
import tz.extend.security.SecurityUtil;
import tz.extend.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 자동로그인을 수행하며, Spring Security와의 연계를 위해 Fileter chain 의 가장 앞에 위치해야 한다.
 *         개발단계에서만 사용할 것을 권장한다.
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * </pre>
 * 
 * @version 1.0
 * @see tz.iam.filter.AutoLoginServletRequest
 * @see tz.iam.filter.AutoLoginServletResponse
 */
public class AutoLoginFilter extends OncePerRequestFilter {

    public static Logger logger = LoggerFactory.getLogger(AutoLoginFilter.class);

    private String username;

    private String password;

    @Autowired
    private UsernamePasswordAuthenticationFilter authFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    public static final String LPORTAL_AUTHKEY = "LPORTAL_AUTHKEY";

    /**
     * <pre>
     * {@ link OncePerRequestFilter}는 요청당 한번만 수행되는 것을 보장한다. doFilterInternal는 상위 클래스의 doFileter가 수행되었을 때의 세부 전략을 구현하고 있다.
     * </pre>
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @param filterChain
     *            Servlet filter chain
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException{
        WebContext.setServletContext(request.getSession().getServletContext());
        WebContext.setRequest(request);
        WebContext.setResponse(response);

        if(!StringUtil.getText(appProperties.get("tz.sign.tempUserUse")).equals("true")){
            filterChain.doFilter(request, response);
            return;
        }

        if(!hasSecurityInfomation(request)){

            String username = this.username;
            String password = this.password;

            String lportalUsername = (String)request.getHeader(LPORTAL_AUTHKEY);

            if(StringUtils.hasText(lportalUsername)){

                username = lportalUsername;

                UserDetails user = userDetailsService.loadUserByUsername(username);

                if(user == null || !user.isEnabled()){
                    throw new BizException("", "권한 없는 사용자", null);
                }

                password = user.getPassword();

            }else{
                // UI에서 넘겨진 계정 정보 사용 (session time-out되어도 재인증되도록, 다른 group 간의
                // 통신이 허용되도록 처리하기 위해서)
                // tUserPwd 를 받아 올 수 없다면, 패스워드 없이 인증되어야 함
                if(request.getParameter("tUserId") != null && request.getParameter("tUserPwd") != null){
                    username = request.getParameter("tUserId");
                    password = SecurityUtil.base64Decode(request.getParameter("tUserPwd"));
                }

                if((username == null || password == null)
                        && StringUtil.getText(appProperties.get("tz.tempUser.userId")) != null
                        && StringUtil.getText(appProperties.get("tz.tempUser.password")) != null){
                    username = StringUtil.getText(appProperties.get("tz.tempUser.userId"));
                    password = StringUtil.getText(appProperties.get("tz.tempUser.password"));
                }
            }

            if(username != null && password != null){

                HttpServletRequest loginRequest = new AutoLoginServletRequest(authFilter.getFilterProcessesUrl(),
                        authFilter.getUsernameParameter(), username, authFilter.getPasswordParameter(), password);
                loginRequest.setAttribute("source", request.getParameter("source")); // XP, link
                HttpServletResponse loginResponse = new AutoLoginServletResponse();

                try{
                    authFilter.doFilter(loginRequest, loginResponse, new FilterChain() {

                        public void doFilter(ServletRequest request, ServletResponse response) throws IOException,
                                ServletException{}

                    });

                    SecurityContext context = SecurityContextHolder.getContext();

                    if(context != null && context.getAuthentication() != null){
                        request.getSession().setAttribute(
                                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
                    }

                    SecurityContextHolder.clearContext();

                }catch(Exception e){
                    logger.debug("자동로그인 실패 - {}", e.getMessage());
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 
     * <pre>
     *  인증 객체가 세션에 있는지 여부를 체크한다.
     * </pre>
     * 
     * @param request
     * @return
     */
    private boolean hasSecurityInfomation(HttpServletRequest request){
        return request.getSession().getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY) != null;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }
}