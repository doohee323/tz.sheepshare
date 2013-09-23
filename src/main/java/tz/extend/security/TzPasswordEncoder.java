package tz.extend.security;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.security.authentication.encoding.PasswordEncoder;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : TzPasswordEncoder
 * 설    명 :
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
public class TzPasswordEncoder implements PasswordEncoder {

	/**
	 * log 처리를 위한 변수 선언
	 */
	private static final Logger logger = LoggerFactory.getLogger(TzPasswordEncoder.class);

    public String encodePassword(String rawPass, Object notUsed) throws DataAccessException {
        try {
            String encodedPwd = SecurityUtil.makeUniqueKey(URLDecoder.decode(rawPass, "UTF-8"));
            logger.trace("TZ SecurityUtil encoded pwd : {}", encodedPwd);
            return encodedPwd;
        } catch (UnsupportedEncodingException e) {
            throw new InvalidDataAccessResourceUsageException(e.getMessage(), e);
        }
    }

    public boolean isPasswordValid(String encPass, String rawPass, Object notUsed) throws DataAccessException {
    	return encPass.equals(rawPass) || encPass.equals(encodePassword(rawPass, notUsed));
    }
}

