package tz.extend.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : CookieUtils
 * 설      명 : 쿠키처리를 위한 Utility Class
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
public class CookieUtils {

    private CookieUtils() {}

    /**
     * <pre>
     *  addCookie
     * </pre>
     * @param response
     * @param name
     * @param value
     */
    public static boolean addCookie(HttpServletResponse response, String name, String value) {
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "utf-8"));
            cookie.setMaxAge(24*60*60);

            response.addCookie(cookie);

            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * <pre>
     *  addCookie
     * </pre>
     * @param request
     * @param response
     * @param name
     * @param value
     */
    public static boolean addCookie(HttpServletRequest request, HttpServletResponse response, String name, String value) {
        try {
            Cookie cookie = new Cookie(name, URLEncoder.encode(value, "utf-8"));

            cookie.setMaxAge(24*60*60);

            response.addCookie(cookie);

            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    /**
     * <pre>
     *  removeCookie
     * </pre>
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    /**
     * <pre>
     *  readCookieValue
     * </pre>
     * @param request
     * @param name
     * @return
     */
    public static String readCookieValue(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if(cookies == null) return "";

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(name)) {
                try {
                    return URLDecoder.decode(cookie.getValue(), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        }
        return "";
    }

    /**
     * <pre>
     *  readCookieValue
     * </pre>
     * @param request
     * @param name
     * @return
     */
    public static String readCookieValue(ServletRequest request, String name) {
        if(request instanceof HttpServletRequest) {
            return readCookieValue((HttpServletRequest) request, name);
        }
        return "";
    }

}
