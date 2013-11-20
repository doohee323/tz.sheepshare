package tz.basis.integration.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import tz.extend.util.StringUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 서버에 업로드/다운로드 하는 간단한 FTP 구현체
 *
 * @author
 */
public class Ftp {

    private Logger logger = LoggerFactory.getLogger(Ftp.class);

    private String serverIp;

    private int port;

    private String userId;

    private String passwd;

    private String encoding;

    private String rootPath;

    private String mode;

    private List<String> localFileFullPaths;

    private List<String> remoteFileFullPaths;

    public void init(){
        this.localFileFullPaths = new ArrayList<String>();
        this.remoteFileFullPaths = new ArrayList<String>();
    }

    public void setSpec(Properties appProperties, String spec){
        this.serverIp = appProperties.getProperty("tz.ftp." + spec + ".serverIp");
        this.port = Integer.parseInt(appProperties.getProperty("tz.ftp." + spec + ".port", "21"));
        this.userId = appProperties.getProperty("tz.ftp." + spec + ".user");
        this.passwd = appProperties.getProperty("tz.ftp." + spec + ".pwd");
        this.encoding = appProperties.getProperty("tz.ftp." + spec + ".encode", "UTF-8");
        this.rootPath = appProperties.getProperty("tz.ftp." + spec + ".rootPath", "");
        this.mode = appProperties.getProperty("tz.ftp." + spec + ".mode", "UTF-8");
    }

    /**
     * 파일 다운로드
     * @param  remoteFileFullPath 서버 저장 패스
     * @param  localFileFullPath  로컬 리소스 경로
     * @return 다운로드 성공 여부
     * @throws Exception
     */
    public void download(String remoteFileFullPath, String localFileFullPath) throws Exception{
        OutputStream stream = null;
        FTPClient ftpClient = new FTPClient();
        try{
            Assert.hasText(localFileFullPath);
            Assert.hasText(remoteFileFullPath);

            if(!StringUtil.getText(encoding).equals("")){
                ftpClient.setControlEncoding(encoding);
            }

            // ftp 접속
            connect(ftpClient);
            login(ftpClient);

            // 업로드 타입 지정
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 기본 디렉토리로 이동
            if(!"".equals(rootPath)){
                ftpClient.changeWorkingDirectory(rootPath);
            }

            String localFileFullPath2 = localFileFullPath.substring(0, localFileFullPath.lastIndexOf("/"));
            new File(localFileFullPath2).mkdirs();

            stream = new FileOutputStream(new File(localFileFullPath));

            // 다운로드
            if(!ftpClient.retrieveFile(remoteFileFullPath, stream))
                throw new RuntimeException("ftpClient retrieveFile fail - " + remoteFileFullPath);

            logger.info("download finished: {}", rootPath + "/" + remoteFileFullPath);
        }catch(Exception e){
            logger.error("download fail!", e);
            throw new Exception("파일 전송에 실패하였습니다.");
        }finally{
            try{
                if(stream != null)
                    stream.close();
            }catch(IOException e){
                logger.error("connection fail!", e);
            }

            disconnect(ftpClient);
        }
    }

    public void downloads() throws Exception{
        downloads(remoteFileFullPaths, localFileFullPaths);
    }

    /**
     * 파일 다운로드
     * @param  remoteFileFullPaths 서버 저장 패스
     * @param  localFileFullPaths  로컬 리소스 경로
     * @return 다운로드 성공 여부
     * @throws Exception
     */
    public void downloads(List<String> remoteFileFullPaths, List<String> localFileFullPaths) throws Exception{
        OutputStream stream = null;
        FTPClient ftpClient = new FTPClient();
        try{

            if(!StringUtil.getText(encoding).equals("")){
                ftpClient.setControlEncoding(encoding);
            }

            // ftp 접속
            connect(ftpClient);
            login(ftpClient);

            // 업로드 타입 지정
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 기본 디렉토리로 이동
            if(!"".equals(rootPath)){
                ftpClient.changeWorkingDirectory(rootPath);
            }

            for(int i=0;i<remoteFileFullPaths.size();i++) {
                String localFileFullPath = localFileFullPaths.get(i);
                String remoteFileFullPath = remoteFileFullPaths.get(i);

                String localFileFullPath2 = localFileFullPath.substring(0, localFileFullPath.lastIndexOf("/"));
                new File(localFileFullPath2).mkdirs();

                stream = new FileOutputStream(new File(localFileFullPath));

                // 다운로드
                if(!ftpClient.retrieveFile(remoteFileFullPath, stream))
                    throw new RuntimeException("ftpClient retrieveFile fail - " + remoteFileFullPath);

                logger.info("download finished: {}", rootPath + "/" + remoteFileFullPath);
            }
        }catch(Exception e){
            logger.error("download fail!", e);
            throw new Exception("파일 전송에 실패하였습니다.");
        }finally{
            try{
                if(stream != null)
                    stream.close();
            }catch(IOException e){
                logger.error("connection fail!", e);
            }

            disconnect(ftpClient);
        }
    }

    /**
     * 파일 업로드
     *
     * @param localFileFullPath 업로드할 파일의 절대 경로
     * @param remoteFileFullPath 서버에 저장할 절대 경로
     * @return 업로드 성공 여부
     * @throws Exception
     */
    public void upload(String localFileFullPath, String remoteFileFullPath) throws Exception{
        FileInputStream stream = null;
        FTPClient ftpClient = new FTPClient();

        try{
            Assert.hasText(localFileFullPath);
            Assert.hasText(remoteFileFullPath);

            if(!StringUtil.getText(encoding).equals("")){
                ftpClient.setControlEncoding(encoding);
            }

            // ftp 접속
            connect(ftpClient);
            login(ftpClient);

            // 업로드 타입 지정
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // 기본 디렉토리로 이동
            if(!"".equals(rootPath)){
                ftpClient.changeWorkingDirectory(rootPath);
            }

            localFileFullPath = StringUtil.replace(localFileFullPath, "\\", "/");
            stream = new FileInputStream(localFileFullPath);

            // 전송 준비
            String[] splitPathAndName = splitPathAndName(remoteFileFullPath, "/");
            String remoteFilePath = "";
            if(remoteFileFullPath.indexOf("/") > -1){
                remoteFilePath = splitPathAndName[0] + "/";
            }
            String remoteFileName = splitPathAndName[1];

            // 업로드 디렉토리 변경
            changeWorkingDirectory(ftpClient, remoteFilePath);

            // 업로드 시도
            if(!ftpClient.storeFile(remoteFileName, stream))
                throw new RuntimeException("ftpClient storeFile fail - " + remoteFilePath + remoteFileName);

            logger.info("upload finished: {}", ("".equals(rootPath) ? "" : (rootPath + "/")) + remoteFilePath
                    + remoteFileName);
        }catch(Exception e){
            logger.error("upload fail!", e);
            throw new Exception("파일 전송에 실패하였습니다.");
        }finally{
            try{
                if(stream != null)
                    stream.close();
            }catch(IOException e){
                logger.error("connection fail!", e);
            }

            disconnect(ftpClient);
        }
    }

    public void uploads() throws Exception{
        uploads(localFileFullPaths, remoteFileFullPaths);
    }

    /**
     * 파일 업로드
     *
     * @param localFileFullPath 업로드할 파일의 절대 경로
     * @param remoteFileFullPath 서버에 저장할 절대 경로
     * @return 업로드 성공 여부
     * @throws Exception
     */
    public void uploads(List<String> localFileFullPaths, List<String> remoteFileFullPaths) throws Exception{
        FileInputStream stream = null;
        FTPClient ftpClient = new FTPClient();

        try{
            if(!StringUtil.getText(encoding).equals("")){
                ftpClient.setControlEncoding(encoding);
            }

            // ftp 접속
            connect(ftpClient);
            login(ftpClient);

            // 업로드 타입 지정
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


            for(int i=0;i<remoteFileFullPaths.size();i++) {
                String localFileFullPath = localFileFullPaths.get(i);
                String remoteFileFullPath = remoteFileFullPaths.get(i);
                localFileFullPath = StringUtil.replace(localFileFullPath, "\\", "/");
                stream = new FileInputStream(localFileFullPath);

                // 전송 준비
                String[] splitPathAndName = splitPathAndName(remoteFileFullPath, "/");
                String remoteFilePath = "";
                if(remoteFileFullPath.indexOf("/") > -1){
                    remoteFilePath = splitPathAndName[0] + "/";
                }
                String remoteFileName = splitPathAndName[1];

                // 기본 디렉토리로 이동
                if(!"".equals(rootPath)){
                    ftpClient.changeWorkingDirectory(rootPath);
                }

                // 업로드 디렉토리 변경
                changeWorkingDirectory(ftpClient, remoteFilePath);

                // 업로드 시도
                if(!ftpClient.storeFile(remoteFileName, stream))
                    throw new RuntimeException("ftpClient storeFile fail - " + remoteFilePath + remoteFileName);

                logger.info("upload finished: {}", ("".equals(rootPath) ? "" : (rootPath + "/")) + remoteFilePath
                        + remoteFileName);
            }
        }catch(Exception e){
            logger.error("upload fail!", e);
            throw new Exception("파일 전송에 실패하였습니다.");
        }finally{
            try{
                if(stream != null)
                    stream.close();
            }catch(IOException e){
                logger.error("connection fail!", e);
            }

            disconnect(ftpClient);
        }
    }

    /**
     * 파일 삭제
     *
     * @param 삭제할파일의경로
     * @return 삭제 성공 여부
     * @throws Exception
     */
    public void delete(String deleteTargetPath) throws Exception{

        FTPClient ftpClient = new FTPClient();
        FileInputStream stream = null;

        try{
            if(!StringUtil.getText(encoding).equals("")){
                ftpClient.setControlEncoding(encoding);
            }

            // ftp 접속
            connect(ftpClient);
            login(ftpClient);

            if(!("").equals(deleteTargetPath)){
                ftpClient.deleteFile(deleteTargetPath);
            }

        }catch(IOException e){
            logger.error("delete fail!", e);
            throw new Exception("파일 삭제를 실패하였습니다.");
        }finally{
            try{
                if(stream != null)
                    stream.close();
            }catch(IOException e){
                logger.error("connection fail!", e);
            }

            disconnect(ftpClient);
        }
    }

    /* FTP 접속 */
    private void connect(FTPClient ftpClient) throws Exception{
        try{
            if (this.mode.equals("") || this.mode.equals("active")) {
                ftpClient.enterLocalActiveMode();
            } else {
                ftpClient.enterLocalPassiveMode();
            }

            ftpClient.connect(serverIp, port);
            int replyCode = ftpClient.getReplyCode();
            logger.info("server ftp connect: {}", replyCode);

            if(!FTPReply.isPositiveCompletion(replyCode)){
                disconnect(ftpClient);
                throw new Exception("서버 연결에 실패하였습니다.");
            }

            logger.info("server ftp connect success - {}:{}", new Object[] { serverIp, port });
        }catch(Exception e){
            logger.error("server ftp connect fail!", e);
            throw new Exception("서버 연결에 실패하였습니다.");
        }
    }

    /* FTP 접속해제 */
    private void disconnect(FTPClient ftpClient) throws Exception{
        try{
            if(ftpClient.isConnected())
                ftpClient.disconnect();
        }catch(Exception e){
            logger.error("disconnection fail!", e);
            throw new Exception("서버 연결 끊기에 실패하였습니다.");
        }
        logger.info("server ftp disconnect!");
    }

    /* 로그인 */
    private void login(FTPClient ftpClient) throws Exception{
        try{
            boolean result = ftpClient.login(userId, passwd);
            if(!result){
                logger.error("server ftp login fail!");
                throw new Exception("서버 연결에 실패하였습니다.");
            }

            logger.info("server ftp login success - {}", userId);
        }catch(IOException e){
            logger.error("server ftp login fail!", e);
            throw new Exception("서버 연결에 실패하였습니다.");
        }
    }

    /**
     * FTP 서버의 디렉토리로 이동하는 기능
     *
     * @param remoteFilePath 이동할 디렉토리
     * @return
    */
    public void changeWorkingDirectory(FTPClient ftpClient, String remoteFilePath){
        try{
            if(ftpClient.changeWorkingDirectory(remoteFilePath)){
                logger.info("ftpClient changeWorkingDirectory success - {}", ftpClient.printWorkingDirectory());
            }else{
                // 디렉토리가 없는 경우, 디렉토리를 만든다.
                logger.info("ftpClient makeDirectory: {}", remoteFilePath);

                String[] paths = remoteFilePath.trim().split("/");
                for(String path : paths){
                    if(StringUtils.isBlank(path))
                        continue;

                    // 디렉토리 변경 시도 후 없으면 생성
                    if(!ftpClient.changeWorkingDirectory(path)){
                        ftpClient.makeDirectory(path);

                        // 디렉토리 변경 재시도 후 없으면 예외
                        if(!ftpClient.changeWorkingDirectory(path))
                            throw new RuntimeException("ftpClient changeWorkingDirectory fail - " + path);
                    }
                }

                logger.info("ftpClient changeWorkingDirectory success - {}", ftpClient.printWorkingDirectory());
            }

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * 파일명이 포함된 전체경로를 주면 파일경로와 파일명으로 분리
     *
     * @param fullpath 전체경로
     * @return String[] - 파일경로[0], 파일명[1]
    */
    public String[] splitPathAndName(String path, String fileSep){

        String[] split = new String[2];
        if(path == null || "".equals(path)){
            split[0] = "";
            split[1] = "";
        }else{
            int lastIndex = path.lastIndexOf(fileSep);
            if(lastIndex >= 0){
                split[0] = path.substring(0, lastIndex);
                split[1] = path.substring(lastIndex + 1);
            }else{
                split[0] = "";
                split[1] = path;
            }
        }
        return split;
    }

    public List<String> getLocalFileFullPaths(){
        return localFileFullPaths;
    }

    public void addLocalFileFullPath(String localFileFullPath){
        this.localFileFullPaths.add(localFileFullPath);
    }

    public List<String> getRemoteFileFullPaths(){
        return remoteFileFullPaths;
    }

    public void addRemoteFileFullPath(String remoteFileFullPath){
        this.remoteFileFullPaths.add(remoteFileFullPath);
    }

    public void addUploadFile(String localFileFullPath, String remoteFileFullPath){
        this.localFileFullPaths.add(localFileFullPath);
        this.remoteFileFullPaths.add(remoteFileFullPath);
    }

    public void addDownloadFile(String remoteFileFullPath, String localFileFullPath){
        this.localFileFullPaths.add(localFileFullPath);
        this.remoteFileFullPaths.add(remoteFileFullPath);
    }
}
