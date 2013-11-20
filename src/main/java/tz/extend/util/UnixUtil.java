package tz.extend.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.SequenceInputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : UnixUtil
 * 설      명 : java 소스에서 Unix 명령어 처리를 위한 Utility Class
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
public class UnixUtil {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(UnixUtil.class);

    /**
     * <pre>
     * Unix 시스템인지 확인 하는 메소드
     * 시스템 변수로 os의 이름이 명시되어 있어야 함
     * </pre>
     * @param request
     * @param response
     */
    public static boolean checkUnix(){
        String osName = " " + System.getProperty("os.name");
        logger.debug("checkUnix command:" + osName);
        if(osName.indexOf("Solaris") > 0)
            return true;
        if(osName.indexOf("AIX") > 0)
            return true;
        if(osName.indexOf("Unix") > 0)
            return true;
        if(osName.indexOf("HP-UX") > 0)
            return true;
        if(osName.indexOf("Linux") > 0)
            return true;
        return false;
    }

    /**
     * <pre>
     * execCommand(HashMap input)
     * </pre>
     * @param input
     * @throws Exception
     */
    public static void execCommand(HashMap input) throws Exception{
        String command = input.get("command").toString();
        logger.debug("execUnixCommand command:" + command);

        try{
            Runtime rt = Runtime.getRuntime();
            rt.exec(command);
        }catch(Exception e){
            logger.debug("execCommand return1 :" + e.getMessage());
            throw new Exception("execCommand command error 1!!!:" + command);
        }
    }

    /**
     * <pre>
     *  Unix 명령어 실행 Process를 새로 생성하여 명령어를 실행한다
     * </pre>
     * @param command : unix 명령어
     * @return
     * @throws Exception
     */
    public static String execUnixCommand(String command) throws Exception{
        logger.debug("execUnixCommand command:" + command);
        //if(!checkUnix()) return "";

        StringBuffer strReturn = new StringBuffer();
        Runtime rt = Runtime.getRuntime();
        Process ps = null;
        try{
            ps = rt.exec(command);
            ps.waitFor();
        }catch(Exception e){
            logger.debug("execUnixCommand return1 :" + e.getMessage());
            throw new Exception("execUnixCommand command error 1!!!:" + command);
        }

        if(ps.exitValue() == 0){
            BufferedReader br = new BufferedReader(new InputStreamReader(new SequenceInputStream(ps.getInputStream(),
                    ps.getErrorStream())));
            try{
                String readLine = null;
                while((readLine = br.readLine()) != null){
                    strReturn.append(readLine).append("\n");
                }
            }catch(IOException e){
                logger.debug("execUnixCommand return2 :" + e.getMessage());
                throw new Exception("execUnixCommand command error 2!!!:" + command);
            }
            logger.debug("execUnixCommand return :" + strReturn);
        }
        return strReturn.toString();
    }

    public static String executeInTelnet(Session session, String command) throws Exception{
        ChannelExec channel = (ChannelExec)session.openChannel("exec");
        channel.setCommand(command);
        channel.setInputStream(null);
        channel.setErrStream(System.err);
        channel.connect();

        InputStream in = channel.getInputStream();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        StringBuffer sb = new StringBuffer();
        while((line = br.readLine()) != null){
            sb.append(line + '\n');
        }
        channel.disconnect();

        return sb.toString();
    }

    // command cursor 읽기
    public static String readUntil(BufferedReader buf, String pattern){
        StringBuffer sb = new StringBuffer(1000);
        String output = null;
        try{
            while((output = buf.readLine()) != null){
                if(output.indexOf(pattern) > -1){
                    output.trim();
                    sb.append(output + "\r\n");
                    return sb.toString();
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String executeInTelnet(Map<String, Object> input) throws Exception{
        String strRslt = "";
        try{
            String authUseYn = StringUtil.getText(input.get("authUseYn"));
            String userId = StringUtil.getText(input.get("userId"));
            String password = StringUtil.getText(input.get("password"));
            String host = StringUtil.getText(input.get("host"));
            String port = StringUtil.getText(input.get("port"));
            int nPort = 23;
            if(!port.equals(""))
                nPort = Integer.parseInt(port);
            String command = StringUtil.getText(input.get("command"));

            TelnetClient tn = new TelnetClient();
            tn.connect(host, nPort);

            BufferedReader reader = new BufferedReader(new InputStreamReader(tn.getInputStream()));
            Writer writer = new PrintWriter(new OutputStreamWriter(tn.getOutputStream()), true);
            
            if(!authUseYn.equals("N")) {
                readUntil(reader, "login:");
                writer.write(userId);
                readUntil(reader, "assword:");
                writer.write(password);
                readUntil(reader, ">");
            }
            writer.write(command);
            String x = readUntil(reader, ">");
        }catch(Exception e){
            e.printStackTrace();
        }
        return strRslt;
    }

    public static void main(String[] arg){
        try{
            JSch jsch = new JSch();
            Session session = jsch.getSession("tz", "10.135.31.103", 22); // ssh만 지원
            session.setPassword("tz");
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);

            session.connect();
            System.out.println("==>" + executeInTelnet(session, "date"));
            System.out.println("==>" + executeInTelnet(session, "ls"));
            System.out.println("==>" + executeInTelnet(session, "who"));
            System.out.println("==>" + executeInTelnet(session, "tail -50 /path/abcd.log"));

            session.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
