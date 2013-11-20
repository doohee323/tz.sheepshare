package tz.common.session.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.common.login.domain.LoginInfo;
import tz.extend.iam.UserInfo;
import tz.extend.iam.authentication.UserDefinition;
import tz.extend.query.CommonDao;
import tz.extend.util.ClassUtils;
import tz.extend.util.CollectionUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : SessionService
 * 설    명 :  사용자 세션 조회를 위한 Service Class
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
@Service("SessionSysService")
public class SessionService {
	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(SessionService.class);

	/**
	 * DB 처리를 위한 공통 dao
	 */
	@Autowired
	@Qualifier("mainDB")
	private CommonDao commonDao;

	 @Autowired
	 @Qualifier("appProperties")
	 private Properties appProperties;

	/**
	 * <pre>
	 * 사용자 세션 조회를 위한 메소드
	 * </pre>
	 *
	 * @param input
	 * @return List<Map<String, Object>>
	 * @throws Exception
	 */
	public List<Map<String, Object>> retrieveSessionList(Map<String, Object> input) {
        List<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
		try {
            UserDefinition userData = UserInfo.getUserInfo();
            mData = convertObj2Map(userData, mData);
            LoginInfo loginInfo = (LoginInfo)UserInfo.getLoginInfo();
            mData = convertObj2Map(loginInfo, mData);

            // 초기화
            UserInfo.setLoginInfo(null);
		} catch (Exception e) {
		    logger.error(e.getMessage());
		}
		return mData;
	}

    /**
     * <pre>
     * 오브젝트를 맵형태로 변경
     * </pre>
     *
     * @param input
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> convertObj2Map(Object sourceObj, List<Map<String, Object>> mData) {
        try {
            Method methods[] = null;
            if(sourceObj.getClass().getSimpleName().equals("UserDefinition")) {
                UserDefinition userData = (UserDefinition)sourceObj;
                methods = userData.getClass().getMethods();
            } else {
                LoginInfo loginInfo = (LoginInfo)sourceObj;
                methods = loginInfo.getClass().getMethods();
            }
            for (int i = 0; i < methods.length; i++) {
                try {
                    String rtype = methods[i].getReturnType().getSimpleName();
                    String methodNm = methods[i].getName();
                    Class[] param = methods[i].getParameterTypes();
                    if (param.length == 0 && !methodNm.equals("wait") && !methodNm.equals("reset")
                            && !methodNm.equals("notify") && !methodNm.equals("notifyAll") && !methodNm.equals("getAuthorityString")
                            && !methodNm.equals("hashCode") && !methodNm.startsWith("reset")) {
                        String value = "";
                        Object obj = ClassUtils.invokeSimple(sourceObj, methodNm);
                        if (rtype.equals("int")) {
                            value = Integer.toString((Integer) obj);
                        } else if (rtype.equals("String")) {
                            value = (String) obj;
                        } else if (rtype.equals("List")) {
                            value = CollectionUtil.listToString(obj);
                        } else if (rtype.equals("boolean")) {
                            value = obj.toString();
                        } else if (rtype.equals("HashMap")) {
                            Map objList = (Map) obj;
                            String listValues = "";
                            if (objList != null) {
                                listValues = CollectionUtil.mapToString(objList);
                            }
                            value = listValues;
                        } else if (rtype.equals("List<Map<String, Object>>")) {
                            List<Map<String, Object>> objList = (List<Map<String, Object>>) obj;
                            String listValues = "";
                            if (objList != null) {
                                for (int j = 0; j < objList.size(); j++) {
                                    listValues += CollectionUtil.mapToString((HashMap) objList.get(j)) + "<br>";
                                }
                                if (listValues.indexOf("<br>") > 0) {
                                    listValues = listValues.substring(0, listValues.length() - 3);
                                }
                            }
                            value = listValues;
                        } else {
                            logger.debug(obj.toString());
                        }
                        mData.add(mData.size(), setValue("userSession", methodNm, value));
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return mData;
    }

	/**
	 * <pre>
	 * 맵에 값 넣기
	 * </pre>
	 *
	 * @param
	 * @return
	 */
	public Map<String, Object> setValue(String aType, String aName, String aValue) {
		Map<String, Object> mData = new HashMap<String, Object>();
		aName = aName.trim();
		mData.put("type", "userSession");
		if (aName.startsWith("get")) {
			aName = aName.substring(3, aName.length());
		} else if (aName.startsWith("is")) {
			aName = aName.substring(2, aName.length());
		}
		mData.put("name", aName);
		mData.put("value", aValue);
		return mData;
	}

}
