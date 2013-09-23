package tz.extend.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.util.FileUtil;
import tz.extend.util.StringUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : ConvertScript
 * 설      명 : OGG_CD, OGG_TIME 강제 주입을 위한 Utility Class
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
public class ConvertScript {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(ConvertScript.class);

    private String[] includes;

    private String include;

    private OutputStreamWriter os;

    private PrintWriter out;

    private File tempFile = new File("./__test.tmp");

    public String getInclude(){
        return include;
    }

    public void setInclude(String include){
        includes = StringUtil.split(include, ",");
        this.include = include;
    }

    /**
     * <pre>
     * 인자로 전달받은 path내 모든 파일에 강제주입 소스코드를 추가한다.
     * </pre>
     *
     * @param aFrom
     * @param aTo
     * @param path
     */
    public void injectAll(String path){
        File file = new File(path);
        File afile[] = file.listFiles();
        try{
            for(int i = 0; i < afile.length; i++){
                if(afile[i].isDirectory()){
                    String s1 = afile[i].getPath();
                    injectAll(s1);
                }else{
                    File file2 = afile[i];
                    String fileName = file2.getName();
                    String extend = "";
                    if(fileName.indexOf(".", fileName.length() - 11) > -1){
                        extend = fileName.substring(fileName.indexOf(".", fileName.length() - 11) + 1,
                                fileName.length());
                    }else{
                        extend = fileName.substring(fileName.indexOf(".") + 1, fileName.length());
                    }

                    if(file2.getName().equals("ConvertScript.java")) {
                        continue;
                    }
                    if(file2.getName().equals("PMZ0101Service.java")) {  //
                        logger.debug("");
                    }


//                    if(checkAllowed(extend) && StringUtil.replace(file2.getAbsolutePath(), "\\", "/").indexOf("/domain/") > -1){
//                        String strStr = FileUtil.getFromFile(file2.getAbsolutePath()).toString();
//                        injectDomain(strStr, afile[i]);
//                    }
//                    if(checkAllowed(extend) && StringUtil.replace(file2.getAbsolutePath(), "\\", "/").indexOf("/sqlmap/") > -1){
//                        String strStr = FileUtil.getFromFile(file2.getAbsolutePath()).toString();
//                        injectQuery(strStr, afile[i]);
//                    }
                      if(checkAllowed(extend)){
                          String strStr = FileUtil.getFromFile(file2.getAbsolutePath()).toString();
                          if(strStr.indexOf("CommonDao") > -1) {
                              injectDao(strStr, afile[i]);
                          }
                      }
                }
            }
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * <pre>
     * 인자로 넘겨 받은 aFile SqlMap xml 파일내 쿼리에 oggCd, oggTime 구현부를 추가한다.
     * </pre>
     *
     * @param current
     */
    private void injectQuery(String strStr, File aFile){
        try{
            if(strStr.toUpperCase().indexOf("OGG_CD") > 0) return;
            String strArry[] = strStr.split("UPDATE ");
            if(strArry.length > 0) {
                String strInj = "$tab$, OGG_CD  = #oggCd#\n";
                strInj += "$tab$, OGG_TIME  = TO_TIMESTAMP(#oggTime#, 'YYYYMMDDHH24MISS')\n";

                strStr = strArry[0];
                for(int i=1;i<strArry.length;i++) {
                    String tmp = strArry[i];
                    tmp = "UPDATE " + tmp;
                    int nPos = tmp.indexOf("\r\n", tmp.indexOf("WHERE") - 30) + 2;
                    if(nPos > 0) {
                        String strBef = "";
                        String strTab = "\t\t\t\t";
                        if(tmp.indexOf(",") == -1 || tmp.indexOf("WHERE") < tmp.indexOf(",")) {
                            try {
                                strBef = tmp.substring(0, nPos);
                            } catch (Exception e) {
                                logger.debug(aFile.getAbsolutePath() + " -> " + e.getMessage());
                            }
                        } else {
                            strBef = tmp.substring(0, nPos);
                        }
                        strInj = StringUtil.replace(strInj, "$tab$", strTab);
                        String strAft = tmp.substring(nPos, tmp.length());
                        if(strBef.indexOf("-->") == -1) {
                            tmp = strBef + strInj + strAft;
                        }
                    }
                    strStr += tmp;
                }
            }

            strArry = strStr.split("INSERT ");
            if(strArry.length > 0) {
                String strInj = "\r\n$tab$, OGG_CD\n";
                strInj += "$tab$, OGG_TIME\n";
                strInj += "$tab$";

                String strInj2 = ", #oggCd#\n";
                strInj2 += "$tab$, TO_TIMESTAMP(#oggTime#, 'YYYYMMDDHH24MISS')\n";
                strInj2 += "$tab$";

                if(aFile.getName().equals("SYA0106Sqlmap.xml")) {
                    // logger.debug(0);
                }
                strStr = strArry[0];
                for(int i=1;i<strArry.length;i++) {
                    String strTab = "\t\t\t\t";
                    String tmp = strArry[i];
                    tmp = "INSERT " + tmp;

                    String SEP = "";
                    int nPos = tmp.indexOf(")", tmp.indexOf(SEP) - 30) - 1;
                    if(tmp.indexOf("SELECT") != -1 && tmp.indexOf("VALUES") == -1) {
                        SEP = "SELECT";
                    } else {
                        SEP = "VALUES";
                    }
                    if(nPos > 0 && tmp.toUpperCase().indexOf(" INTO ") > -1) {
                        if(tmp.lastIndexOf("\r\n", nPos) == -1) continue;
                        String strBef = tmp.substring(0, tmp.lastIndexOf("\r\n", nPos));
                        strInj = StringUtil.replace(strInj, "$tab$", strTab);
                        String strAft = tmp.substring(nPos, tmp.length());
                        tmp = strBef + strInj + strAft;
                    }

                    if(SEP.equals("SELECT")) {
                        nPos = tmp.indexOf("\r\n", tmp.indexOf("FROM") - 20) + 2;
                    } else {
                        nPos = tmp.indexOf(")", tmp.indexOf("</insert>") - 20);
                    }
                    if(nPos > 0) {
                        String strBef = tmp.substring(0, nPos);
                        strInj2 = StringUtil.replace(strInj2, "$tab$", strTab);
                        String strAft = tmp.substring(nPos, tmp.length());
                        if(strBef.indexOf("-->") == -1) {
                            tmp = strBef + strInj2 + strAft;
                        }
                    }
                    strStr += tmp;
                }
            }

            os = new OutputStreamWriter(new FileOutputStream(tempFile));
            out = new PrintWriter(os, true);
            out.println(strStr);

            if(os != null)
                os.close();
            if(out != null)
                out.close();

            if(tempFile.exists()){
                if(aFile.delete()){
                    tempFile.renameTo(aFile);
                }else{
                    logger.debug("파일 삭제 오류 : " + aFile);
                }
            }
        }catch(Exception e){
            logger.debug(aFile.getAbsolutePath() + " -> " + e.getMessage());
        }
    }

    /**
     * <pre>
     * 인자로 넘겨 받은 aFile Domain Class내에 oggCd, oggTime 변수선언, getter, setter 메소드 구현부를 추가한다.
     * </pre>
     *
     * @param current
     */
    private void injectDomain(String strStr, File aFile){
        try{
            String strTar = "";
            String befStr = strStr.substring(0, strStr.indexOf("\r\n", strStr.lastIndexOf("private ")) + 2);
            String aftStr = strStr.substring(strStr.lastIndexOf("private "), strStr.length());
            aftStr = aftStr.substring(aftStr.indexOf("\r\n") + 2, aftStr.lastIndexOf("}"));

            String oggCd = "";
            String oggCd2 = "";
            if(strStr.indexOf("oggCd") == -1){
                oggCd = getPrivate("oggCd");
                oggCd2 = getGetSetter("oggCd");
            }

            String oggTime = "";
            String oggTime2 = "";
            if(strStr.indexOf("oggTime") == -1){
                oggTime = getPrivate("oggTime");
                oggTime2 = getGetSetter("oggTime");
            }

            strTar = befStr + oggCd + oggTime + aftStr;
            strTar += oggCd2 + oggTime2;

            strTar += "}";

            os = new OutputStreamWriter(new FileOutputStream(tempFile));
            out = new PrintWriter(os, true);
            out.println(strTar);

            if(os != null)
                os.close();
            if(out != null)
                out.close();

            if(tempFile.exists()){
                if(aFile.delete()){
                    tempFile.renameTo(aFile);
                }else{
                    logger.debug("파일 삭제 오류 : " + aFile);
                }
            }
        }catch(Exception e){
            logger.debug(aFile.getAbsolutePath() + " -> " + e.getMessage());
        }
    }

    /**
     * <pre>
     * 인자로 전달받은 aFile Service Class내에 CommonDao를 강제주입하는 소스를 삽입한다.
     * </pre>
     *
     * @param current
     */
    private void injectDao(String strStr, File aFile){
        try{
            if(strStr.indexOf("@Qualifier(\"mainDB\")") > -1) {
                return;
            }

            if(strStr.indexOf("\r\nimport") == -1) {
                return;
            }

            String strTar = "";

            if(strStr.indexOf("org.springframework.beans.factory.annotation.Qualifier") == -1) {
                strTar = strStr.substring(0, strStr.lastIndexOf("\r\nimport"));
                strTar += "\r\nimport org.springframework.beans.factory.annotation.Qualifier;";
                strTar += strStr.substring(strStr.lastIndexOf("\r\nimport"), strStr.length());
            } else {
                strTar = strStr;
            }

            String strArry[] = strTar.split("CommonDao");
            strTar = strArry[0] + "CommonDao";
            for(int i=1;i<strArry.length-1;i++) {
                String tmp = strArry[i].trim();
                if(tmp.lastIndexOf("@Autowired") < tmp.length() - 50) {
                    strTar += strArry[i] + "CommonDao";
                    continue;
                }
                if(tmp.indexOf("@Autowired") == -1) {
                    strTar += strArry[i] + "CommonDao";
                    continue;
                }
                tmp = tmp.substring(0, tmp.lastIndexOf("@Autowired"));
                tmp += "@Autowired\r\n";
                tmp += "\t@Qualifier(\"mainDB\")\r\n";
                tmp += "\tprivate CommonDao";
                strTar += tmp;
            }
            strTar += strArry[strArry.length - 1];

            os = new OutputStreamWriter(new FileOutputStream(tempFile));
            out = new PrintWriter(os, true);
            out.println(strTar);

            if(os != null)
                os.close();
            if(out != null)
                out.close();

            if(tempFile.exists()){
                if(aFile.delete()){
                    tempFile.renameTo(aFile);
                }else{
                    logger.debug("파일 삭제 오류 : " + aFile);
                }
            }
        }catch(Exception e){
            logger.debug(aFile.getAbsolutePath() + " -> " + e.getMessage());
        }
    }

    /**
     * return 'private' String
     * @param colNm
     * @return
     */
    private String getPrivate(String colNm){
        String strInj = "\n";
        strInj += "\t/**\n";
        strInj += "\t* " + colNm + "\n";
        strInj += "\t*/\n";
        strInj += "\tprivate String " + colNm + ";\n";
        return strInj;
    }

    /**
     * return 'get' method
     * @param colNm
     * @return
     */
    private String getGetSetter(String colNm){
        String strUpper = colNm.substring(0, 1).toUpperCase() + colNm.substring(1, colNm.length());
        String strInj = "\n";
        strInj += "\t/**\n";
        strInj += "\t* " + colNm + " getter\n";
        strInj += "\t* @return " + colNm + "\n";
        strInj += "\t*/\n";
        strInj += "\tpublic String get" + strUpper + "() {\n";
        strInj += "\t\treturn " + colNm + ";\n";
        strInj += "\t}\n";
        strInj += "\n";
        strInj += "\t/**\n";
        strInj += "\t* " + colNm + " setter\n";
        strInj += "\t* @param " + colNm + "\n";
        strInj += "\t*/\n";
        strInj += "\tpublic void set" + strUpper + "(String " + colNm + ") {\n";
        strInj += "\t\tthis." + colNm + " = " + colNm + ";\n";
        strInj += "\t}\n";
        return strInj;
    }

    /**
     * <pre>
     * 현재 요청 파일명이 적용이 필요한 지 판단
     * </pre>
     *
     * @param current
     */
    protected boolean checkAllowed(String current){
        if(includes == null){
            return true;
        }
        for(int i = 0; i < includes.length; i++){
            if(current.equals(includes[i]))
                return true;
        }
        return false;
    }

    /**
     * <pre>
     * main(String[] args)
     * </pre>
     *
     * @param args
     */
    public static void main(String[] args){
        ConvertScript conv = new ConvertScript();
        conv.setInclude("java");    //,xml
        conv.injectAll("C:/TZ_IDE/workspace/tz.sheepshare/src/main/java");
//        conv.injectAll("C:/TZ_IDE/workspace/tz.sheepshare/src/main/java/ll");
        logger.debug("Convert Success!!");
    }
}
