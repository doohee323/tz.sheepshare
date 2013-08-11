package tz.extend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.springframework.jdbc.support.JdbcUtils;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : ConvertColumns
 * 설      명 : 파일내 틀정 Column을 다른 문자로 변환한다.
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
public class ConvertColumns {

    private InputStreamReader is;

    private BufferedReader in;

    private OutputStreamWriter os;

    private PrintWriter out;

    private File tempFile = new File("./__test.tmp");

    private int sep = 0;

    private String sFrom = "";

    private String sTo = "";

    static private String[] includes;

    /**
     * 기본 생성사
     */
    private ConvertColumns() {
        if(File.separator.equals("/")){
            sep = 1;
        }else{
            sep = 2;
        }
    }

    /**
     * path 경로 파일의 aFrom 문자열을 aTo 문자열로 치환한다.
     *
     * @param aFrom
     * @param aTo
     * @param path
     */
    private void findFile(String aFrom, String aTo, String path){
        sFrom = aFrom;
        sTo = aTo;
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("작업할 파일이 없습니다." + file.getAbsolutePath());
        }
        File afile[] = file.listFiles();
        try{
            for(int i = 0; i < afile.length; i++){
                if(afile[i].isDirectory()){
                    String s1 = afile[i].getPath();
                    findFile(sFrom, sTo, s1);
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
                        is = new InputStreamReader(new FileInputStream(afile[i]));
                        in = new BufferedReader(is);
                        StringBuffer sb = new StringBuffer(1000);
                        String str = "";

                        int readed = 0;
                        while((str = in.readLine()) != null){
                            readed += (str.getBytes().length + sep);
                            String tmp = str + (File.separator.equals("/") ? "" : "\r") + "\n";

                            String inputArry[] = sFrom.split(",");
                            String outputArry[] = sTo.split(",");

                            for(int k = 0; k < inputArry.length; k++){
                                String input = inputArry[k];
                                // case 1 : duty_sys_cd
                                // case 2 : DUTY_SYS_CD
                                // case 3 : dutySysCd
                                // case 4 : DutySysCd
                                String output = outputArry[k];
                                if(tmp.indexOf(input) > -1){
                                    System.out.println(tmp + " : " + input + " => " + output);
                                }
                                for(int j = 0; j < 4; j++){
                                    if(j == 0){
                                        tmp = StringUtil.replace(tmp, input, output);
                                    }else if(j == 1){
                                        input = input.toUpperCase();
                                        output = output.toUpperCase();
                                        tmp = StringUtil.replace(tmp, input, output);
                                    }else if(j == 2){
                                        input = JdbcUtils.convertUnderscoreNameToPropertyName(input);
                                        output = JdbcUtils.convertUnderscoreNameToPropertyName(output);
                                        tmp = StringUtil.replace(tmp, input, output);
                                    }else if(j == 3){
                                        input = upperFirst(input);
                                        output = upperFirst(output);
                                        tmp = StringUtil.replace(tmp, input, output);
                                    }
                                }
                            }
                            sb.append(tmp);
                        }

                        System.out.println(afile[i]);
                        os = new OutputStreamWriter(new FileOutputStream(tempFile));
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
                                System.out.println("!!!!! : " + afile[i]);
                            }
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Column 변환 승인 여부 반환
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

    String sArg2 = "C:/Temp/tz.sheepshare";

    /**
     * Sample 메소드
     */
    public void convert(){
        String input = "";
        String output = "";

        String fileName = "C:/TZ_IDE/workspace/tz.sheepshare/src/main/java/tz/com/tz/tz/common/utils/convertlist.txt";
        File f = new File(fileName);
        FileReader fr = null;
        BufferedReader br = null;
        try{
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            String line = br.readLine();
            while(line != null){
                input += line.substring(0, line.indexOf("\t")) + ",";
                output += line.substring(line.indexOf("\t") + 1, line.length()) + ",";
                line = br.readLine();
            }
        }catch(FileNotFoundException e1){
            e1.printStackTrace();
        }catch(IOException e1){
            e1.printStackTrace();
        }finally{
            try{
                br.close();
                fr.close();
            }catch(IOException e){
                e.printStackTrace();
            }
        }
        input = input.substring(0, input.length() - 1);
        output = output.substring(0, output.length() - 1);
        String extend = "java,xml"; // ,properties
        includes = StringUtil.split(extend, ",");
        findFile(input, output, sArg2);
    }

    /**
     * 첫번째 문자열을 대문자로 치환한다.
     * @param s
     * @return
     */
    public static String upperFirst(String s){
        if(s == null || s.length() < 1)
            return s;
        return (s.substring(0, 1).toUpperCase() + s.substring(1));
    }

    public static void main(String[] args){
        //		if (args[0] != null) {
        //			sArg0 = args[0];
        //		}
        //		if (args[1] != null) {
        //			sArg1 = args[1];
        //		}
        //		if (args[2] != null) {
        //			sArg2 = args[2];
        //		}
        ConvertColumns conv = new ConvertColumns();
        conv.convert();

        System.out.println("Convert Success!!");
    }

}
