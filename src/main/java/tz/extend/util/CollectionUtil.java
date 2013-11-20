package tz.extend.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : CollectionUtil
 * 설    명 :
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
public class CollectionUtil {

    private static final String SEP = "|";

	/**
	 * <pre>
	 * 파라미터로 HttpServletRequest를 받아 FORM INPUT Data를 parsing하여 HashMap<String, Object>객체에 담아 return한다. getAttributeNames() 사용한다.
	 * </pre>
	 * @param req HttpServletRequest
	 * @return HashMap<String, Object> attribute data
	 */
	public static Map<String, Object> getAttributeBox(HttpServletRequest req) {
		Map<String, Object> data = new HashMap<String, Object>();
		@SuppressWarnings("unchecked")
		Enumeration<String> e = req.getAttributeNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			data.put(key, req.getAttribute(key));
		}
		return data;
	}

	/**
	 * <pre>
	 * 파라미터로 HttpServletRequest를 받아 FORM INPUT Data를 parsing하여 HashMap<String, Object>객체에 담아 return한다. getParameterValues() 사용한다.
	 * </pre>
	 * @param req HttpServletRequest
	 * @return HashMap<String, Object> Input data
	 */
	public static Map<String, Object> getData(HttpServletRequest req) {
		Map<String, Object> data = new HashMap<String, Object>();

		@SuppressWarnings("unchecked")
		Enumeration<String> e = req.getParameterNames();

		while (e.hasMoreElements()) {
			String key = e.nextElement();
			data.put(key, req.getParameter(key));
		}

		return data;
	}

	/**
	 * <pre>
	 * 파라미터로 HttpServletRequest를 받아 cookie를 가져와서 HashMap<String, Object>객체에 담아 return한다. getCookies() 사용한다.
	 * </pre>
	 * @param req HttpServletRequest
	 * @return HashMap<String, Object> cookies
	 */
	public static Map<String, Object> getDataFromCookie(HttpServletRequest req) {
		Map<String, Object> cookieData = new HashMap<String, Object>();
		Cookie[] cookies = req.getCookies();
		if (cookies == null) {
			return cookieData;
		}

		for (int i = 0; i < cookies.length; i++) {
			String key = cookies[i].getName();
			String value = cookies[i].getValue();
			if (value == null) {
				value = "";
			}
			String cookiesValue = value;
			cookieData.put(key, cookiesValue);
		}
		return cookieData;
	}

	/**
	 * <pre>
	 * 파라미터로 HttpServletRequest를 받아 FORM INPUT Data를 parsing하여 List<Map<String, Object>>객체에 담아 return한다. getParameterValues() 사용한다.
	 * </pre>
	 * @param req HttpServletRequest
	 * @return List<Map<String, Object>> Input multi-data
	 */
	public static List<Map<String, Object>> getMultiData(HttpServletRequest req) {
		List<Map<String, Object>> multiData = new ArrayList<Map<String, Object>>();

		@SuppressWarnings("unchecked")
		Enumeration<String> e = req.getParameterNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			String[] values = req.getParameterValues(key);
			Map<String, Object> list = new HashMap<String, Object>();
			for (int i = 0; i < values.length; i++) {
				list.put(key, values[i]);
			}
			multiData.add(multiData.size(), list);
		}
		return multiData;
	}

	/**
	 * <pre>
	 * HashMap<String, Object> 객체의 복사한 새로운 HashMap<String, Object> 객체를 생성하여 return한다.
	 * </pre>
	 * @param data HashMap<String, Object>
	 * @return HashMap<String, Object>
	 */
	public static Map<String, Object> clone(Map<String, Object> data) {
		Map<String, Object> newData = new HashMap<String, Object>();

		Map<String, Object> src = data;
		Map<String, Object> target = newData;

		Set<String> set = src.keySet();
		Iterator<String> e = set.iterator();

		while (e.hasNext()) {
			String key = e.next();
			Object value = src.get(key);
			target.put(key, value);
		}
		return newData;
	}

    /**
     * <pre>
     * MapToString
     * </pre>
     *
     * @param
     * @return
     */
    public static String mapToString(Map<String, Object> objList) {
        String listValues = "";
        for (Map.Entry<String, Object> mapEntry : objList.entrySet()) {
            String dataKey = mapEntry.getKey();
            String str = tz.extend.util.StringUtil.getText(mapEntry.getValue());
            listValues += dataKey + "=" + str + SEP;
        }
        if (listValues.indexOf(SEP) > 0) {
            listValues = listValues.substring(0, listValues.length() - 1);
        }
        return listValues;
    }

    public static String getSValue(String strStr, String key) {
        if(strStr.indexOf(SEP) == -1) return "";
        String strArry[] = strStr.split("\\" + SEP);
        for (int i=0;i<strArry.length;i++) {
            String strArry2[] = strArry[i].split("=");
            if(strArry2[0].equals(key)) {
                return strArry2[1];
            }
        }
        return "";
    }

    /**
     * <pre>
     * ListToString
     * </pre>
     *
     * @param
     * @return
     */
    public static String listToString(Object obj) {
        String listValues = "";
        List objList = (List) obj;
        if (objList != null) {
            for (int j = 0; j < objList.size(); j++) {
                listValues += objList.get(j) + SEP;
            }
            if (listValues.indexOf(SEP) > 0) {
                listValues = listValues.substring(0, listValues.length() - 1);
            }
        }
        return listValues;
    }
}
