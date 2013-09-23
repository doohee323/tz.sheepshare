package tz.extend.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.config.Constants;
import tz.extend.core.mvc.context.WebContext;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : ServletUtil
 * 설    명 : Servlet Request/Response 와 관련된 일반적인 목적의 유틸리티 메소드를 제공한다.
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
public class ServletUtil {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(ServletUtil.class);

	/**
	 * <pre>
	 * redirect
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @param uri
	 * @throws IOException
	 */
	public static void redirect(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException {
		response.sendRedirect(makeRedirectURL(request, response, uri));
	}

	/**
	 * <pre>
	 * makeRedirectURL
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @param uri
	 * @return return encoded & context appended Redirect URL
	 */
	public static String makeRedirectURL(HttpServletRequest request, HttpServletResponse response, String uri) {
		if (uri.startsWith("/")) {
			uri = request.getContextPath() + uri;
		}
		return response.encodeRedirectURL(uri);
	}

	/**
	 * <pre>
	 * forward
	 * </pre>
	 *
	 * @param request
	 * @param response
	 * @param uri
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void forward(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException, ServletException {

		RequestDispatcher dispatcher = null;
		dispatcher = request.getSession().getServletContext().getRequestDispatcher(uri);
		dispatcher.forward(request, response);
	}

	/**
	 * <pre>
	 * redirect : 최초 오픈한 browser에 jsession 노출
	 * forward : target url로 전환되지 않음(ulr count에 문제될 수 있음)
	 * 위의 문제 해결한 방법임
	 * </pre>
	 *
	 * @param response
	 * @param url
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void redirect(HttpServletResponse response, String url) throws IOException {
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">");
		out.println("<head>");
		out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.println("<title></title>");
		out.println("<script type=\"text/javascript\">");
		out.println("  window.location.href= \"" + url + "\";");
		out.println("</script>");
		out.println("</head>");
		out.println("<body>");
		out.println("</body>");
		out.println("</html>");
	}

	/**
	 * <pre>
	 * Return the URL representing the current request. This is equivalent to
	 * <code>HttpServletRequest.getRequestURL()</code> in Servlet 2.3.
	 * </pre>
	 *
	 * @param request The servlet request we are processing
	 * @return URL representing the current request
	 * @exception Exception if a URL cannot be created
	 */
	public static URL requestURL(HttpServletRequest request) throws Exception {
		StringBuffer url = new StringBuffer();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		if (port < 0)
			port = 80; // Work around java.net.URL bug
		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getRequestURI());
		return (new URL(url.toString()));
	}

    /**
     * <pre>
     * </pre>
     *
     * @exception Exception if a URL cannot be created
     */
    public static String getServerUrl(HttpServletRequest request) throws Exception {
        String url = request.getRequestURL().toString();
        url = url.substring(0, url.indexOf("/", url.indexOf("//") + 3));
        return url;
    }

	/**
	 * <pre>
	 * Return the URI representing the current request including Query String if
	 * exists.
	 * </pre>
	 *
	 * @param String The servlet request we are processing
	 * @return String representing the current request including Query String if exists.
	 */
	public static String requestURI(String url) {
		url = url.substring(url.indexOf("/", 10), url.length());
		if (url.indexOf("?") > -1)
			url = url.substring(0, url.indexOf("?"));
		return url;
	}

	/**
	 * <pre>
	 * Return the URI representing the current request including Query String if exists.
	 * </pre>
	 *
	 * @param request The servlet request we are processing
	 * @return String representing the current request including Query String if exists.
	 */
	public static String requestURI(HttpServletRequest request) {
		StringBuffer uri = new StringBuffer();
		uri.append(request.getRequestURI());
		if (ObjUtil.isNull(request.getQueryString())) {
			uri.append("?").append(request.getQueryString());
		}
		return uri.toString();
	}

	/**
	 * <pre>
	 * Map에 들어있는 데이터를 이용하여 쿼리스트링을 구성한다. String 타입만 쿼리스트링에 추가되며 url encoding은 수행하지 않는다.
	 * </pre>
	 *
	 * @param params
	 * @return ex) a=AAA&b=BBB&
	 */
	public static String buildQueryString(Map params) {
		StringBuffer buff = new StringBuffer();
		for (Iterator iter = params.keySet().iterator(); iter.hasNext();) {
			Object key = iter.next();
			Object param = params.get(key);
			if (param instanceof String) {
				buff.append(key.toString() + "=" + param.toString() + "&");
			}
		}
		return buff.toString();
	}

	/**
	 * <pre>
	 * Return the URL representing the scheme, server, and port number of the
	 * current request. Server-relative URLs can be created by simply appending
	 * the server-relative path (starting with '/') to this.
	 * </pre>
	 *
	 * @param request The servlet request we are processing
	 * @return URL representing the scheme, server, and port number of the current request
	 * @exception MalformedURException if a URL cannot be created
	 */
	public static URL serverURL(HttpServletRequest request) {
		try {
			StringBuffer url = new StringBuffer();
			String scheme = request.getScheme();
			int port = request.getServerPort();
			if (port < 0)
				port = 80; // Work around java.net.URL bug
			url.append(scheme);
			url.append("://");
			url.append(request.getServerName());
			if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
				url.append(':');
				url.append(port);
			}
			url.append(request.getContextPath());
			return (new URL(url.toString()));
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	/**
	 * <pre>
	 * server url에서 domain만 가져오는 메소드
	 * </pre>
	 *
	 * @param requestURL
	 * @return
	 * @throws Exception
	 * @throws MalformedURException
	 */
	public static String getDomain(String requestURL) throws Exception {
		requestURL = requestURL.substring(requestURL.indexOf("//") + 2);
		if (requestURL.indexOf(":") > 0) {
			requestURL = requestURL.substring(0, requestURL.indexOf(":"));
		}
		return requestURL;
	}

	/**
	 * <pre>
	 * getExtend
	 * </pre>
	 *
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public static String getExtend(HttpServletRequest req) throws Exception {
		String path = req.getRequestURI();
		if (path.indexOf(".") > 0) {
			return path.substring(path.lastIndexOf(".") + 1, path.length());
		}
		return "";
	}

	/**
	 * <pre>
	 * getExtend
	 * </pre>
	 *
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getExtend(String path) throws Exception {
		if (path.indexOf(".") > 0) {
			return path.substring(path.lastIndexOf(".") + 1, path.length());
		}
		return "";
	}

	/**
	 * <pre>
	 * Url strParam 으로 부터 param을 추출하여 input에 추가
	 * </pre>
	 *
	 * @param params
	 * @return ex) a=AAA&b=BBB&
	 */
	public static void setParamFromUrl(Map input, String strParam) {
		try {
			if (strParam.indexOf("=") > -1) {
				if (strParam.indexOf("&") > -1) {
					String arrInput[] = strParam.split("\\&");
					for (int i = 0; i < arrInput.length; i++) {
						String tmp = arrInput[i];
						if (tmp.indexOf("=") > 0) {
							String arrTmp[] = tmp.split("=");
							if (arrTmp.length > 1) {
								if (arrTmp[1].indexOf("=") > -1) {
									// String tmpArry[] = arrTmp[1].split("=");
								} else {
									input.put(arrTmp[0], arrTmp[1]);
								}
							}
						}
					}
				} else {
					String arrTmp[] = strParam.split("=");
					if (arrTmp.length > 1) {
						input.put(strParam.split("=")[0], strParam.split("=")[1]);
					} else {
						input.put(strParam.split("=")[0], "");
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * <pre>
	 * getData
	 * </pre>
	 *
	 * @param req
	 * @param aCol
	 * @return aVal
	 */
	public static String getData(HttpServletRequest req, String aCol) {
		String aVal = (req.getAttribute(aCol) == null) ? req.getParameter(aCol) : (String) req.getAttribute(aCol);
		if (aVal == null)
			aVal = "";
		if (aVal.equals("null"))
			aVal = "";
		return aVal;
	}

	/**
	 * <pre>
	 * getAttrJspParam
	 * </pre>
	 *
	 * @param req
	 * @return
	 */
	public static HashMap getAttrJspParam(HttpServletRequest req) {
		HashMap input = new HashMap();
		try {
			if (req.getAttribute("javax.servlet.include.query_string") != null) {
				String query = (String) req.getAttribute("javax.servlet.include.query_string");
				query = java.net.URLDecoder.decode(query, "utf-8");
				String strArry[] = query.split("&");
				for (int i = 0; i < strArry.length; i++) {
					input.put(strArry[i].split("=")[0], strArry[i].split("=")[1]);
				}
			}
		} catch (UnsupportedEncodingException e) {
		}
		return input;
	}

	/**
	 * <pre>
	 * TZ 액션 URL을 추출한다. ex) 요청주소가
	 * http://127.0.0.1:5001/lgcare/aaa/bbb/sample.
	 * p31.UpdateEmployeeForm.ajax?name1=value1 인 경우 리턴되는 값은
	 * 'sample.p31.UpdateEmployeeForm.ajax' 이다.
	 * </pre>
	 *
	 * @param req
	 * @return
	 */
	public static String getActionURL(HttpServletRequest req) {
		return req.getRequestURI();
	}

	/**
	 * <pre>
	 * 이전 페이지의 request url 가져오기
	 * </pre>
	 *
	 * @param req
	 * @return
	 * @throws MalformedURException
	 */
	public static String getReferActionURL(HttpServletRequest req) throws Exception {
		String url = req.getHeader("referer");
		url = url.substring(ServletUtil.serverURL(req).toString().length(), url.length());
		return url;
	}

	/**
	 * <pre>
	 * getParameterMap
	 * </pre>
	 *
	 * @param req
	 * @return
	 */
	public static Map<String, String> getParameterMap(HttpServletRequest req) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = req.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			map.put(name, req.getParameter(name));
		}
		return map;
	}

	/**
	 * <pre>
	 * redirectErrPage
	 * </pre>
	 *
	 * @param errData
	 * @throws IOException
	 */
	public static void redirectErrPage(HashMap errData) throws IOException {
		WebContext.getResponse().sendRedirect("/common/error/com.tz.tz.jsp?" + ServletUtil.buildQueryString(errData));
	}

	/**
	 * <pre>
	 * JSESSIONID 리턴
	 * </pre>
	 *
	 * @return String
	 */
	public static String getJSessionId(HttpServletRequest req) {
		String JSESSIONID = "";
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				String key = cookies[i].getName();
				JSESSIONID = cookies[i].getValue();
				if (key.equals("JSESSIONID"))
					break;
			}
		}
		return JSESSIONID;
	}

	/**
	 * <pre>
	 * getJSessionId
	 * </pre>
	 *
	 * @return
	 */
	public static String getJSessionId() {
		return getJSessionId(WebContext.getRequest());
	}

	/**
	 * <pre>
	 * getRemoteAddr
	 * </pre>
	 *
	 * @param req
	 * @return
	 */
	public static String getRemoteAddr(HttpServletRequest req) {
		if (Constants.accessLogIpForward.equals("true")) {
			String ipAddr = req.getHeader("X-Forwarded-For");
			if (ipAddr != null && ipAddr.indexOf(",") > 0) {
				ipAddr = ipAddr.substring(0, ipAddr.indexOf(","));
			}
			if (ipAddr == null)
				ipAddr = req.getRemoteAddr(); // 직접 was로 호출했을 경우 ex) http://165.244.98.11:8080/refresh.ajax
			return ipAddr;
		} else {
			return req.getRemoteAddr();
		}
	}

	/**
	 * <pre>
	 * get domain
	 * 0 => www.lge.com
	 * 1 => lge.com
	 * </pre>
	 *
	 * @param int dent
	 * @return String
	 */
	public static String getDomain(int dent) {
		String domain = WebContext.getRequest().getServerName();
		if (dent == 1) {
			domain = domain.substring(domain.indexOf(".") + 1, domain.length());
		}
		return domain;
	}

	/**
	 * <pre>
	 * index페이지에서 redirect 처리, 작업 중 페이지로 전환 제어
	 * </pre>
	 *
	 * @param HttpServletRequest req
	 * @param HttpServletResponse res
	 * @param String url
	 */
	public static void redirectIndexPage(HttpServletRequest req, HttpServletResponse res, String url) {
		try {
			res.sendRedirect(url);
		} catch (IOException e) {
		}
	}

	/**
	 * <pre>
	 * getQueryString
	 * </pre>
	 *
	 * @param data
	 * @return
	 */
	public static String getQueryString(Map data) {
		String strRslt = "";
		Set dataKeySet = data.keySet();
		Iterator dataIterator = dataKeySet.iterator();
		while (dataIterator.hasNext()) {
			String dataKey = dataIterator.next().toString();
			String str = "";
			if (data.get(dataKey) != null)
				str = data.get(dataKey).toString();
			strRslt += "&" + dataKey + "=" + str;

		}
		if (strRslt.length() > 0)
			strRslt = strRslt.substring(1, strRslt.length());
		return strRslt;
	}

	/**
	 * <pre>
	 * getQueryString
	 * </pre>
	 *
	 * @param req
	 * @return
	 */
	public static String getQueryString(HttpServletRequest req) {
		return getQueryString(req, "");
	}

	/**
	 * <pre>
	 * getQueryString
	 * </pre>
	 *
	 * @param req
	 * @param exceptStr
	 * @return strRslt
	 */
	public static String getQueryString(HttpServletRequest req, String exceptStr) {
		if (exceptStr.indexOf(",") == -1)
			exceptStr += ",";
		String strRslt = "";
		Enumeration names = req.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			if (exceptStr.indexOf((name + ",")) == -1) {
				strRslt += "&" + name + "=" + req.getParameter(name);
			}
		}
		if (strRslt.length() > 0)
			strRslt = strRslt.substring(1, strRslt.length());
		return strRslt;
	}
}
