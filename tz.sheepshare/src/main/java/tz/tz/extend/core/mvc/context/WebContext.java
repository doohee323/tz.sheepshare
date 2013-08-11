package tz.extend.core.mvc.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.config.Constants;
import tz.extend.iam.UserInfo;
import tz.extend.util.ServletUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : WebContext
 * 설    명 : The WebContext is the context in which a web application is executed. Each
 *        context is basically a container of objects an command needs for execution
 *        like the session, request, response, etc.
 *        <p>
 *        The WebContext is thread local which means that values stored in the
 *        WebContext are unique per thread. See the {@link java.lang.ThreadLocal} class
 *        for more information. The benefit of this is you don't need to worry about a
 *        user specific web context, you just get it:
 *        <ul>
 *        <code>WebContext context = WebContext.getContext();</code>
 *        </ul>
 *        Finally, because of the thread local usage you don't need to worry about
 *        making your commands thread safe. J2EE 웹개발시 요청을 처리하는 쓰레드 사이에서 아래 객체들을 메소드의
 *        파라미터를 통해 전달받지 않고 쉽게 액세스할 수 있게 해주는 유틸리티입니다. -
 *        javax.servlet.http.HttpServletRequest; -
 *        javax.servlet.http.HttpServletResponse; - javax.servlet.ServletContext; -
 *        dev.foundation.front.fileupload.LMultipartRequest; 짐작하시겠지만 내부적으로
 *        java.lang.ThreadLocal을 이용하고 있습니다. 아래 4개의 메소드를 이용하여 원하는 웹관련 객체를 얻을 수 있습니다. -
 *        getServletContext() - getRequest() - getResponse() - getMultipartRequest() 이
 *        클래스가 작동하기 위해서는 web.xml에 WebContextFilter 필터를 설정해야 합니다.
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
public class WebContext {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(WebContext.class);

	static ThreadLocal webContext = new WebContextThreadLocal();

	/**
	 * Constant for the HTTP request object.
	 */
	public static final String HTTP_REQUEST = "webcontext.HttpServletRequest";

	/**
	 * Constant for the HTTP response object.
	 */
	public static final String HTTP_RESPONSE = "webcontext.HttpServletResponse";

	/**
	 * Constant for an HTTP {@link javax.servlet.RequestDispatcher request dispatcher}.
	 */
	public static final String MULTIPART_HTTP_REQUEST = "webcontext.MultipartHttpServletRequest";

	/**
	 * Constant for the {@link javax.servlet.ServletContext servlet context} object.
	 */
	public static final String SERVLET_CONTEXT = "webcontext.ServletContext";

	// SUBAPP_CD
	public static final String SUBAPP_CD = "webcontext.SubApp";

	// SessionID
	public static final String USER_SESSION_ID = "webcontext.UserSessionID";

	// DocumentRoot
	public static String DocumentRoot = "";

	/**
     */
	public static List userSessionIds;

	/**
     */
	Map context;

	/**
	 * <pre>
	 * Creates a new WebContext initialized with another context.
	 * </pre>
	 *
	 * @param context a context map.
	 */
	public WebContext(Map context) {
		this.context = context;
	}

	/**
	 * <pre>
	 * Sets the web context for the current thread.
	 * </pre>
	 *
	 * @param context the action context.
	 */
	public static void setContext(WebContext context) {
		webContext.set(context);
	}

	/**
	 * <pre>
	 * Returns the WebContext specific to the current thread.
	 * </pre>
	 *
	 * @return the WebContext for the current thread.
	 */
	public static WebContext getContext() {
		WebContext context = (WebContext) webContext.get();

		if (context == null) {
			context = new WebContext(new HashMap());
			setContext(context);
		}

		return context;
	}

	/**
	 * <pre>
	 * Sets the action's context map.
	 * </pre>
	 *
	 * @param contextMap the context map.
	 */
	public void setContextMap(Map contextMap) {
		getContext().context = contextMap;
	}

	/**
	 * <pre>
	 * Gets the context map.
	 * </pre>
	 *
	 * @return the context map.
	 */
	public Map getContextMap() {
		return context;
	}

	/**
	 * <pre>
	 * Returns a value that is stored in the current WebContext by doing a
	 * lookup using the value's key.
	 * </pre>
	 *
	 * @param key the key used to find the value.
	 * @return the value that was found using the key or <tt>null</tt> if the key was not found.
	 */
	public Object get(Object key) {
		return context.get(key);
	}

	/**
	 * <pre>
	 * Stores a value in the current WebContext. The value can be looked up
	 * using the key.
	 * </pre>
	 *
	 * @param key the key of the value.
	 * @param value the value to be stored.
	 */
	public void put(Object key, Object value) {
		context.put(key, value);
	}

	/**
	 * <pre>
	 * setRequest( HttpServletRequest request )
	 * </pre>
	 *
	 * @param request
	 */
	public static void setRequest(HttpServletRequest request) {
		WebContext.getContext().put(HTTP_REQUEST, request);
	}

	/**
	 * <pre>
	 * setResponse( HttpServletResponse response )
	 * </pre>
	 *
	 * @param response
	 */
	public static void setResponse(HttpServletResponse response) {
		WebContext.getContext().put(HTTP_RESPONSE, response);
	}

	/**
	 * <pre>
	 * setServletContext( ServletContext context )
	 * </pre>
	 *
	 * @param context
	 */
	public static void setServletContext(ServletContext context) {
		WebContext.getContext().put(SERVLET_CONTEXT, context);
	}

	/**
	 * <pre>
	 * setSubappCd( String aSubAppCd )
	 * </pre>
	 *
	 * @param aSubAppCd
	 */
	public static void setSubappCd(String aSubAppCd) {
		WebContext.getSession().setAttribute(SUBAPP_CD, aSubAppCd);
	}

	/**
	 * <pre>
	 * getUserSessionId()
	 * </pre>
	 *
	 * @return
	 */
	public static String getUserSessionId() {
		Object obj = WebContext.getSession().getAttribute(USER_SESSION_ID);
		if (obj == null)
			return "";
		return (String) obj;
	}

	/**
	 * <pre>
	 * setUserSessionId( String userSessionId )
	 * </pre>
	 *
	 * @param userSessionId
	 */
	public static void setUserSessionId(String userSessionId) {
		WebContext.getSession().setAttribute(USER_SESSION_ID, userSessionId);
		if (WebContext.userSessionIds == null)
			WebContext.userSessionIds = new ArrayList();
		if (UserInfo.getUserInfo() != null && !UserInfo.getUserId().equals("")) {
			userSessionId += "_" + UserInfo.getUserId();
		}
		WebContext.userSessionIds.add(userSessionId);
	}

	/**
	 * <pre>
	 * removeUserSessionId()
	 * </pre>
	 */
	public static void removeUserSessionId() {
		String jsessionId = ServletUtil.getJSessionId();
		if (jsessionId.equals(""))
			return;
		if (WebContext.userSessionIds == null)
			WebContext.userSessionIds = new ArrayList();
		for (int i = WebContext.userSessionIds.size(); i > 0; i--) {
			if (WebContext.userSessionIds.get(i - 1).toString().indexOf(ServletUtil.getJSessionId()) > -1) {
				WebContext.userSessionIds.remove(i - 1);
			}
		}
	}

	/**
	 * <pre>
	 * ServletContext getServletContext()
	 * </pre>
	 *
	 * @return
	 */
	public static ServletContext getServletContext() {
		return (ServletContext) WebContext.getContext().get(SERVLET_CONTEXT);
	}

	/**
	 * <pre>
	 * HttpServletRequest getRequest()
	 * </pre>
	 *
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		try {
			Object obj = WebContext.getContext().get(HTTP_REQUEST);
			if (obj != null) {
				return (HttpServletRequest) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.debug("WebContext.getContext().get( HTTP_REQUEST ) : " + e.toString());
		}
		return null;
	}

	/**
	 * <pre>
	 * HttpServletRequest getRequest(String contextStr)
	 * </pre>
	 *
	 * @param contextStr
	 * @return
	 */
	public static HttpServletRequest getRequest(String contextStr) {
		try {
			Object obj = WebContext.getContext().get(contextStr);
			if (obj != null) {
				return (HttpServletRequest) obj;
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.debug("WebContext.getContext().get( contextStr ) : " + e.toString());
		}
		return null;
	}

	/**
	 * <pre>
	 * HttpServletResponse getResponse()
	 * </pre>
	 *
	 * @return
	 */
	public static HttpServletResponse getResponse() {
		return (HttpServletResponse) WebContext.getContext().get(HTTP_RESPONSE);
	}

	/**
	 * <pre>
	 * HttpServletResponse getResponse(String contextStr)
	 * </pre>
	 *
	 * @param contextStr
	 * @return
	 */
	public static HttpServletResponse getResponse(String contextStr) {
		return (HttpServletResponse) WebContext.getContext().get(contextStr);
	}

	/**
	 * <pre>
	 * Returns the current session associated with this request, or if the
	 * request does not have a session, creates one.
	 * </pre>
	 *
	 * @return
	 */
	public static HttpSession getSession() {
		try {
			return getRequest().getSession();
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * <pre>
	 * Returns the current HttpSession associated with this request or, if if
	 * there is no current session and create is true, returns a new session.
	 * </pre>
	 *
	 * @param create
	 * @return
	 */
	public static HttpSession getSession(boolean create) {
		try {
			return getRequest().getSession(create);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * <pre>
	 * getSubappCd()
	 * </pre>
	 *
	 * @return
	 */
	public static String getSubappCd() {
		try {
			if (getSession().getAttribute(SUBAPP_CD) == null)
				return "";
			return (String) getSession().getAttribute(SUBAPP_CD);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return "";
	}

	/**
	 * <pre>
	 * getDocRoot()
	 * </pre>
	 *
	 * @return
	 */
	public static String getDocRoot() {
		if (WebContext.getServletContext() != null)
			DocumentRoot = WebContext.getServletContext().getRealPath("");
		return DocumentRoot;
	}

	/**
	 * <pre>
	 * getSysBase()
	 * </pre>
	 *
	 * @return
	 */
	public static String getSysBase() {
		return Constants.docRoot;
	}

	// 확장 기능 구현 필요
	public static Locale getLoclCd() {
		return new Locale("ko_kr");
	}

	public static void setLoclCd(Locale aLocale) {
	}

	public static String getLocaleCd() {
		return "ko_kr";
	}

	public static void setLocaleCd(String aLocale) {
	}

	public static String getCharset() {
		return "kr";
	}

	public static void setCharset(String aCharset) {
	}

	public static String getNatCd() {
		return "ko";
	}

	public static void setNatCd(String aNatCd) {
	}

	public static boolean getSSL() {
		return false;
	}

	public static void setSSL(boolean aSSL) {
	}

	public static HashMap getReplaceContent() {
		return new HashMap();
	}

	public static String getLanguageCd() {
		return "ko";
	}

	public static void removeLoclCd() {
	}

	public static void removeLocaleCd() {
	}

	public static final String USER_JAVALOCALE_CD = null;
	public static final String USER_LOCALE_CD = null;
	public static final String USER_COUNTRY_CD = null;

	public static void setLoclCds(String aStr) {
	}

	// --------------------------------------------------------- inner class
	private static class WebContextThreadLocal extends ThreadLocal {

		protected Object initialValue() {
			return new WebContext(new HashMap());
		}
	}
}
