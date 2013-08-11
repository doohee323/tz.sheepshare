package tz.extend.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

import tz.extend.config.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : ConfigUtil
 * 설      명 : Sheepshare 설정정보를 로딩하여 메모리에 적재한다.
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
public class ConfigUtil {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * <pre>
     * Sheepshare 설정정보파일을 로딩한다.
     *   - environment-config.properties : Sheepshare 공통 설정파일
     *   - environment-sheepshare-config.properties : Sheepshare 업무 모듈 설정파일
	 *   - environment-database.properties : Sheepshare DB정보
	 *   - environment-spy-database.properties : Sheepshare DB모니터링을 위한 설정정보
     * </pre>
     *
     * @throws Exception
     */
    public static Properties getProperties() {
        Properties appProperties = new Properties();
        try{
            String path = Constants.docRoot +"/WEB-INF/classes/properties";
            File file = new File(path);
            File afile[] = file.listFiles();
            for(int i = 0; i < afile.length; i++){
                if(afile[i].getName().endsWith(".properties")){
                    Properties props = new Properties();
                    props.load(new FileInputStream(afile[i].getAbsoluteFile().toString()));
                    for (Enumeration en = props.propertyNames(); en.hasMoreElements();) {
                        String key = (String) en.nextElement();
                        String value = props.getProperty(key);
                        appProperties.setProperty(key, value);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return appProperties;
    }

    /**
     * <pre>
     *  Sheepshare 설정정보파일을 로딩한다.
     * </pre>
     *
     * @param appProperties
     * @throws Exception
     */
    public static Properties getProperties(Properties appProperties) {
        try{
            if(appProperties == null){
                appProperties = getProperties();
            }
        }catch(Exception e){
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return appProperties;
    }
}
