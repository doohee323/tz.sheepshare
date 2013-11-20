package tz.extend.iam.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tz.basis.iam.core.jdbc.authentication.UserMapping;
import tz.basis.query.core.mapper.ObjectCopyMapper;
import tz.extend.query.CommonDao;
import tz.extend.util.ClassUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 인증 과정에서 사용자 정보 및 권한 조회 작업을 수행한다.  SpringSecurity의 ProviderManager에서 호출되며, 개발자가 직접 호출하는 일은 없다.
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-02-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * 
 * @version 1.0
 */
public class UserDefinedAuthentication implements
		UserDetailsService, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * DB 처리를 위한 공통 dao
	 */
	@Autowired
	@Qualifier("mainDB")
	private CommonDao dao;

	private ObjectCopyMapper resultMapper = new ObjectCopyMapper();

	/**
	 * <pre>
	 * 인증에 필요한 사용자 정보를 조회한다.
	 * </pre>
	 * 
	 * @param username
	 *            사용자ID
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {

		if (username == null || username.length() == 0) {
			return null;
		}

		UserDetails userDetails = retrieveUserInfoFromLdap(username);

		/*
		 * LDAP에 사용자 정보가 없는 경우 DATABASE에서 조회 (협력업체??)
		 */
		if (userDetails == null) {
			userDetails = retrieveUserInfoFromRepository(username);

			if (userDetails == null) {
				logger.debug("Query returned no results for user '" + username
						+ "'");
				throw new UsernameNotFoundException("Username {" + username
						+ "} not found");
			}
		}

		return userDetails;
	}

	/**
	 * 
	 * <pre>
	 *  협력업체 직원은 DB에서 사용자 정보를 조회하여 반환
	 * </pre>
	 * 
	 * @param username
	 *            사용자ID
	 * @return 사용자 정보를 포함하는 인증 객체
	 */
	private UserDetails retrieveUserInfoFromRepository(String userId) {
		UserMapping user = new UserDefinition();

		try {
	        Map<String, Object> input = new HashMap<String, Object>();
	        input.put("userId", userId);
	        logger.debug("retrieveUserInfoFromRepository" + input.toString());
	        List<Map<String, Object>> mData = dao.queryForMapList(
	                "loginXP.userLogin", input);
	        user = resultMapper.mapperMap(mData.get(0), UserDefinition.class);
	        user.setUserId(userId);
	        ClassUtils.invokeSimple(user, "setPasswd", mData.get(0).get("passwd"));

	        List<UserDetails> users = new ArrayList<UserDetails>();
	        users.add(user);

	        if (users.size() > 0) {
	            return users.get(0);
	        }
		} catch (Exception e) {
		    e.fillInStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * <pre>
	 *  정직원의 경우 LDAP에서 사용자 정보를 조회하여 반환
	 * </pre>
	 * 
	 * @param username
	 *            사용자ID
	 * @return 사용자 정보를 포함하는 인증 객체
	 */
	private UserDetails retrieveUserInfoFromLdap(final String username) {
		return null;
	}

    public void afterPropertiesSet() throws Exception{
        // TODO Auto-generated method stub
        
    }

}
