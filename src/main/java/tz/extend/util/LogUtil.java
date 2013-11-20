package tz.extend.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import tz.extend.iam.UserInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : LogUtil
 * 설      명 : 로그 처리를 위한 Utility Class
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
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(LogUtil.class);

    /**
     * <pre>
     * 모든 객체의 값을 StringBuffer로 변환하여 리턴
     * ex) tableId=SY그룹
     *     queryId=GROUP.RETRIEVEGROUPLIST
     * </pre>
     *
     * @param Object
     * @return StringBuffer
     */
    public static StringBuffer toStringForConsole(Object obj){
        StringBuffer buf = new StringBuffer();
        String objType = obj.getClass().getCanonicalName();
        buf.append("=============================================\n");
        if(objType.equals("java.util.HashMap")){
            Map map = (Map)obj;
            Set dataKeySet = map.keySet();
            Iterator dataIterator = dataKeySet.iterator();
            while(dataIterator.hasNext()){
                String dataKey = dataIterator.next().toString();
                buf.append(dataKey + "=");
                if(map.get(dataKey) != null){
                    buf.append(map.get(dataKey).toString() + "\n");
                }else{
                    buf.append("\n");
                }
            }
        }else{

        }
        buf.append("=============================================\n");
        return buf;
    }

    /**
     * <pre>
     * 모든 객체의 값을 StringBuffer로 변환하여 리턴
     * ex) SY그룹^GROUP.RETRIEVEGROUPLIST
     * </pre>
     *
     * @param Object
     * @return StringBuffer
     */
    public static StringBuffer toStringForCSV(Object obj){
        StringBuffer buf = new StringBuffer();
        String objType = obj.getClass().getCanonicalName();
        if(objType.equals("java.util.HashMap")){
            Map map = (Map)obj;
            Set dataKeySet = map.keySet();
            Iterator dataIterator = dataKeySet.iterator();
            while(dataIterator.hasNext()){
                String dataKey = dataIterator.next().toString();
                if(map.get(dataKey) != null){
                    buf.append(map.get(dataKey).toString() + "^");
                }else{
                    buf.append("^");
                }
            }
        }else{

        }
        return buf;
    }

    /**
     * <pre>
     * 모든 객체의 값을 StringBuffer로 변환하여 출력
     * </pre>
     *
     * @param sType
     * @param Object
     */
    public static void pringToString(String sType, Object obj){
        if(sType.equals("CONSOLE")){
            logger.debug(LogUtil.toStringForConsole(obj).toString());
        }else if(sType.equals("CSV")){
            logger.debug(LogUtil.toStringForCSV(obj).toString());
        }else{
            logger.debug(LogUtil.toStringForConsole(obj).toString());
        }
    }

    /**
     * <pre>
     * 모든 객체의 값을 StringBuffer로 변환하여 출력
     * </pre>
     *
     * @param Logger
     * @param sType
     * @param Object
     */
    public static void pringToString(Logger logger, String sType, Object obj){
        if(sType.equals("CONSOLE")){
            logger.debug(new String(LogUtil.toStringForConsole(obj)));
        }else if(sType.equals("CSV")){
            logger.debug(new String(LogUtil.toStringForCSV(obj)));
        }else{
            logger.debug(LogUtil.toStringForConsole(obj).toString());
        }
    }

    /**
     * <pre>
     * 로그에 부가 정보 포함하여 리턴
     * </pre>
     *
     */
    public static String logFormat(Object obj, String log){
        try {
            return "[" + obj.getClass().getSimpleName() + "^" + UserInfo.getUserId() + "^" + UserInfo.getIpAddress() + "]"
                    + log;
        } catch (Exception e) {
            return "";
        }
    }

}
