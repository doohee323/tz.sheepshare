package tz.extend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.util.StringUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : ConvertCharSet
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
public class ConvertCharSet {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(ConvertCharSet.class);

    private InputStreamReader is;

    private BufferedReader in;

    private OutputStreamWriter os;

    private PrintWriter out;

    private File tempFile = new File("./__test.tmp");

    private int sep = 0;

    private String sFrom = "";

    private String sTo = "";

    private String[] includes;

    private String include;

    public String getInclude(){
        return include;
    }

    public void setInclude(String include){
        includes = StringUtil.split(include, ",");
        this.include = include;
    }

    /**
     * <pre>
     *  ConvertCharSet
     * </pre>
     */
    public ConvertCharSet() {
        if(File.separator.equals("/")){ // 리눅스 계열의 줄바꿈은 \n만
            sep = 1;
        }else{// 윈도우즈는 \r\n
            sep = 2;
        }
    }

    /**
     * <pre>
     *  convert
     * </pre>
     * @param aFrom
     * @param aTo
     * @param path
     */
    public void convert(String aFrom, String aTo, String path){
        sFrom = aFrom;
        sTo = aTo;
        File file = new File(path);
        File afile[] = file.listFiles();
        try{
            for(int i = 0; i < afile.length; i++){
                if(afile[i].isDirectory()){
                    String s1 = afile[i].getPath();
                    convert(sFrom, sTo, s1);
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

                    if(checkAllowed(extend) && !fileName.equals("convertCharSet.java")){
                        is = new InputStreamReader(new FileInputStream(afile[i]), sFrom);
                        in = new BufferedReader(is);
                        StringBuffer sb = new StringBuffer(1000);
                        String str = "";

                        int readed = 0;
                        while((str = in.readLine()) != null){
                            readed += (str.getBytes(sFrom).length + sep);
                            sb.append(str + (File.separator.equals("/") ? "" : "\r") + "\n");
                        }

                        logger.debug(afile[i].getAbsolutePath());
                        os = new OutputStreamWriter(new FileOutputStream(tempFile), sTo);
                        out = new PrintWriter(os, true);
                        out.println(sb);

                        if(is != null)
                            is.close();
                        if(in != null)
                            in.close();
                        if(os != null)
                            os.close();
                        if(out != null)
                            out.close();

                        if(tempFile.exists()){
                            if(afile[i].delete()){
                                tempFile.renameTo(afile[i]);
                            }else{
                                logger.debug("파일 삭제 오류 : " + afile[i]);
                            }
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
     * 현재 요청 파일명이 적용이 필요한 지 판단
     * </pre>
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
     * @param args
     */
    	public static void main(String[] args) {
    		ConvertCharSet conv = new ConvertCharSet();

    		String sArg0 = "euc-kr";
    		String sArg1 = "UTF-8";
            String sArg2 = "C:/tz-dev/workspace/TZ_OZ/com.tz.tz-core_src";
//            String sArg2 = "C:/tz-dev/workspace/DEV_RUI/com.tz.tz-framework_src";
//            String sArg2 = "C:/tz-dev/workspace/DEV_RUI/com.tz.tz-webservice_src";
    		if (args[0] != null) {
    			sArg0 = args[0];
    		}
    		if (args[1] != null) {
    			sArg1 = args[1];
    		}
    		if (args[2] != null) {
    			sArg2 = args[2];
    		}
            logger.debug("sArg0:" + sArg0);
            logger.debug("sArg1:" + sArg1);
            logger.debug("sArg2:" + sArg2);

    //		String includes = "dtd";
    		String includes = "java,jsp,ini,conf,bat,vm,txt,html,htm,properties,js,vbs,htc,css,tld,conf,xml";
    		conv.setInclude(includes);

    		// iso_8859-1 EUC_KR UTF-8 C:\AADEV_HSNI\workspace\HSNI_ERP_TZ\src
    		logger.debug(sArg2 + " 하위 폴더 파일의 " + sArg0 + "을 " + sArg1
    				+ "로 변환!!");
    		conv.convert(sArg0, sArg1, sArg2);

    		logger.debug("Convert Success!!");
    	}

    //    ansi_x3.4-1968 1252 Western
    //    ansi_x3.4-1986 1252 Western
    //    ascii 1252 Western
    //    big5 950 Traditional Chinese (BIG5)
    //    chinese 936 Chinese Simplified
    //    cp367 1252 Western
    //    cp819 1252 Western
    //    csascii 1252 Western
    //    csbig5 950 Traditional Chinese (BIG5)
    //    cseuckr 949 Korean
    //    cseucpkdfmtjapanese CODE_JPN_EUC Japanese (EUC)
    //    csgb2312 936 Chinese Simplified (GB2312)
    //    csiso2022jp CODE_JPN_JIS Japanese (JIS-Allow 1 byte Kana)
    //    csiso2022kr 50225 Korean (ISO)
    //    csiso58gb231280 936 Chinese Simplified (GB2312)
    //    csisolatin2 28592 Central European (ISO)
    //    csisolatinhebrew 1255 Hebrew (ISO-Visual)
    //    cskoi8r 20866 Cyrillic (KOI8-R)
    //    csksc56011987 949 Korean
    //    csshiftjis 932 Shift-JIS
    //    euc-kr 949 Korean
    //    extended_unix_code_packed_format_for_japanese CODE_JPN_EUC Japanese (EUC)
    //    gb2312 936 Chinese Simplified (GB2312)
    //    gb_2312-80 936 Chinese Simplified (GB2312)
    //    hebrew 1255 Hebrew
    //    hz-gb-2312 936 Chinese Simplified (HZ)
    //    ibm367 1252 Western
    //    ibm819 1252 Western
    //    ibm852 852 Central European (DOS)
    //    ibm866 866 Cyrillic (DOS)
    //    iso-2022-jp CODE_JPN_JIS Japanese (JIS)
    //    iso-2022-kr 50225 Korean (ISO)
    //    iso-8859-1 1252 Western
    //    iso-8859-2 28592 Central European (ISO)
    //    iso-8859-8 1255 Hebrew (ISO-Visual)
    //    iso-ir-100 1252 Western
    //    iso-ir-101 28592 Central European (ISO)
    //    iso-ir-138 1255 Hebrew (ISO-Visual)
    //    iso-ir-149 949 Korean
    //    iso-ir-58 936 Chinese Simplified (GB2312)
    //    iso-ir-6 1252 Western
    //    iso646-us 1252 Western
    //    iso8859-1 1252 Western
    //    iso8859-2 28592 Central European (ISO)
    //    iso_646.irv:1991 1252 Western
    //    iso_8859-1 1252 Western
    //    iso_8859-1:1987 1252 Western
    //    iso_8859-2 28592 Central European (ISO)
    //    iso_8859-2:1987 28592 Central European (ISO)
    //    iso_8859-8 1255 Hebrew (ISO-Visual)
    //    iso_8859-8:1988 1255 Hebrew (ISO-Visual)
    //    koi8-r 20866 Cyrillic (KOI8-R)
    //    korean 949 Korean
    //    ks-c-5601 949 Korean
    //    ks-c-5601-1987 949 Korean
    //    ks_c_5601 949 Korean
    //    ks_c_5601-1987 949 Korean
    //    ks_c_5601-1989 949 Korean
    //    ksc-5601 949 Korean
    //    ksc5601 949 Korean
    //    ksc_5601 949 Korean
    //    l2 28592 Central European (ISO)
    //    latin1 1252 Western
    //    latin2 28592 Central European (ISO)
    //    ms_kanji 932 Shift-JIS
    //    shift-jis 932 Shift-JIS
    //    shift_jis 932 Shift-JIS
    //    us 1252 Western
    //    us-ascii 1252 Western
    //    windows-1250 1250 Central European (Windows)
    //    windows-1251 1251 Cyrillic (Windows)
    //    windows-1252 1252 Western
    //    windows-1253 1253 Greek (Windows)
    //    windows-1254 1254 Turkish (Windows)
    //    windows-1255 1255 Hebrew
    //    windows-1256 1256 Arabic
    //    windows-1257 1257 Baltic (Windows)
    //    windows-1258 1258 Vietnamese
    //    windows-874 874 Thai
    //    x-cp1250 1250 Central European (Windows)
    //    x-cp1251 1251 Cyrillic (Windows)
    //    x-euc CODE_JPN_EUC Japanese (EUC)
    //    x-euc-jp CODE_JPN_EUC Japanese (EUC)
    //    x-sjis 932 Shift-JIS
    //    x-x-big5 950 Traditional Chinese (BIG5)

}
