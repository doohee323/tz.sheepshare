package tz.extend.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import tz.extend.config.Constants;
import tz.extend.util.StringUtil;
import tz.extend.util.UnixUtil;

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
//            appProperties = init(appProperties, Constants); // TO-DO
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
    
   public static Properties init(Properties appProperties, Object obj){
        boolean bUnix = UnixUtil.checkUnix();
        String path = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
        path = path.substring(path.indexOf("/") + 1, path.length());
        if(path.indexOf(":") > -1) {
            path = path.substring(0, path.indexOf(":") + 1);
            ReflectionTestUtils.setField(obj, "drive", path);
        } else {
            ReflectionTestUtils.setField(obj, "drive", "");
        }
        
        for(Enumeration en = appProperties.propertyNames(); en.hasMoreElements();){
            String fieldNm = (String)en.nextElement();
            String value = appProperties.getProperty(fieldNm);

            if(fieldNm.equals("lhf.serverInfo.docRoot")){
                path = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
                if(path.indexOf("WEB-INF")  > -1) {
                    path = path.substring(path.indexOf("/") + 1, path.indexOf("WEB-INF") - 1);
                } else {
                    path = path.substring(path.indexOf("/") + 1, path.length());
                    path = path.substring(0, path.indexOf(":") + 1);
                    path += "/temp";
                }
                if(bUnix) path = "/" + path;
                value = path;
                ReflectionTestUtils.setField(obj, "docRoot", value);
                appProperties.setProperty(fieldNm, value);
            }else if(fieldNm.equals("lhf.serverInfo.appRoot")){
                path = obj.getClass().getProtectionDomain().getCodeSource().getLocation().toString();
                if(path.indexOf("WEB-INF") > -1) {
                    path = path.substring(path.indexOf("/") + 1, path.indexOf("WEB-INF")) + "WEB-INF/classes" + value;
                } else {
                    path = path.substring(path.indexOf("/") + 1, path.indexOf("classes")) + "classes" + value;
                }
                if(bUnix) path = "/" + path;
                value = path;
                ReflectionTestUtils.setField(obj, "appRoot", value);
                appProperties.setProperty(fieldNm, value);
            }else if(fieldNm.equals("lhf.serverInfo.pjtCd")){
                ReflectionTestUtils.setField(obj, "pjtCd", value);
            }else if(fieldNm.equals("lhf.serverInfo.working")){
                value = setEnvVal(value);
                ReflectionTestUtils.setField(obj, "workingDir", value);
                appProperties.setProperty(fieldNm, value);
            }else if(fieldNm.equals("lhf.serverInfo.groupId")){
                value = setEnvVal(value);
                ReflectionTestUtils.setField(obj, "groupId", value);
                appProperties.setProperty(fieldNm, value);
            }
        }
        
        for(Enumeration en = appProperties.propertyNames(); en.hasMoreElements();){
            String fieldNm = (String)en.nextElement();
            String value = appProperties.getProperty(fieldNm);

            if(value.indexOf("#") > -1){
                value = setEnvVal(value);
            }

            if(fieldNm.equals("lhf.serverInfo.upload.default")){
                ReflectionTestUtils.setField(obj, "uploadDefault", value);
            }else if(fieldNm.equals("lhf.serverInfo.sysCd")){
                ReflectionTestUtils.setField(obj, "sysCd", value);
            }else if(fieldNm.equals("lhf.serverInfo.systemNm")){
                ReflectionTestUtils.setField(obj, "systemNm", value);
            }else if(fieldNm.equals("lhf.serverInfo.groupId")){
                ReflectionTestUtils.setField(obj, "groupId", value);
            }else if(fieldNm.equals("lhf.serverInfo.defaultGroupId")){
                ReflectionTestUtils.setField(obj, "defaultGroupId", value);
            }else if(fieldNm.equals("lhf.serverInfo.stage")){
                ReflectionTestUtils.setField(obj, "stage", value);
            }else if(fieldNm.equals("lhf.accessLog.ipForward")){
                ReflectionTestUtils.setField(obj, "accessLogIpForward", value);
            }else if(fieldNm.equals("lhf.initPage.encodingType")){
                ReflectionTestUtils.setField(obj, "initPageEncodingType", value);
            }else if(fieldNm.equals("lhf.xp.adlFile")){
                ReflectionTestUtils.setField(obj, "xpAdlFile", value);
            }else if(fieldNm.equals("lhf.xp.cabVer")){
                ReflectionTestUtils.setField(obj, "xpCabVer", value);
            }
            
            appProperties.setProperty(fieldNm, value);
        }
        
        return appProperties;
    }

    /**
     * <pre>
     * setEnvVal
     * </pre>
     * @param value
     */
    public static String setEnvVal(String value){
        try{
            if(value.indexOf("#appRoot") > -1){
                value = StringUtil.replace(value, "#appRoot", Constants.appRoot);
            }
            if(value.indexOf("#docRoot") > -1){
                value = StringUtil.replace(value, "#docRoot", Constants.docRoot);
            }
            if(value.indexOf("#workingDir") > -1){
                value = StringUtil.replace(value, "#workingDir", Constants.workingDir);
            }
            if(value.indexOf("#drive") > -1){
                value = StringUtil.replace(value, "#drive", Constants.drive);
            }
            if(value.indexOf("#groupId") > -1){
                value = StringUtil.replace(value, "#groupId", Constants.groupId);
            }
            return StringUtil.replace(value, "\\", "/");
        }catch(Exception e){
            return "";
        }
    }    
}
