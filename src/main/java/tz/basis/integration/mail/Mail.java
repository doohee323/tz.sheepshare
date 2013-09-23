package tz.basis.integration.mail;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : Mail
 * 설    명 :
 * <pre>
 *     JAVA 로 Mail 을 전송하는 Utility Class.
 *     컴파일 및 실행을 위해서는 JAVA mail 패키지 ( mail.jar ) 와
 *     JAVA Activatiion Framework 의 패키지( activation.jar )가 필요하다.
 *
 *     [ 사용 방법 ]
 *     Mail mail = new Mail() ; // Mail mail = new Mail( &quot;mailSpec&quot; ) ;
 *     mail.setFromMailAddress(&quot;aaa@bbb.com&quot;,&quot;보내는사람이름&quot;);
 *     mail.setToMailAddress(&quot;ccc@ddd.com&quot;, &quot;TO로받는사람&quot;);
 *     //mail.setCcMailAddress(&quot;eee@fff.com&quot;, &quot;CC로받는사람&quot;);
 *     //mail.setBccMailAddress(&quot;ggg@hhh.com&quot;, &quot;BCC로받는사람&quot;);
 *
 *     mail.setSubject(&quot; MAIL 제목을 넣으면 됩니다.&quot;);
 *     String message = &quot; Text 메일 메시지 내용 &quot; ;
 *     String htmlMessage = &quot;&lt;html&gt;&lt;font color='red'&gt; HTML 메일 메시지 내용&lt;/font&gt;&lt;/html&gt;&quot; ;
 *     String[] fileNames = { &quot;c:/attachFile1.zip&quot;,&quot;c:/attachFile2.txt&quot;   } ;
 *
 *     mail.setHtmlAndFile(htmlMessage,fileNames);
 *     //mail.setHtml(htmlMessage);
 *     //mail.setText(message);
 *     mail.send();  // 메일 전송
 *
 *    [ 사용 방법 II ]
 *     html 형태로 메일을 발송할 때 템플릿 html 을 활용할 경우 예제는 sample.tzframework.service.mail.MailSample 를 참조하길 바란다.
 * </pre>
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import tz.extend.config.Constants;
import tz.extend.util.ObjUtil;
import tz.extend.util.StringUtil;
import tz.extend.util.config.ConfigUtil;

@Service
public class Mail {

    /**
     * log 처리를 위한 변수 선언
     */
    private static final Logger logger = LoggerFactory.getLogger(Mail.class);

    private String mailSpec = "";

    private String bodyTemplate = "";

    /* 전송 메시지 */
    private Message msg = null;

    /* 메세지 전송에 필요한 기본 사항 */
    private String MAIL_SMTP_HOST = "mail.smtp.host";

    private String MAIL_SMTP_PORT = "mail.smtp.port";

    private String MAIL_SMTP_AUTH = "mail.smtp.auth";

    private String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";

    private String SUB_TYPE = "mixed";

    /* 메일 전송에 필요한 기본 */
    private String MAIL_HOST = null;

    private String MAIL_PORT = null;

    private boolean MAIL_AUTH = false;

    private String MAIL_AUTH_ID = null;

    private String MAIL_AUTH_PWD = null;

    private boolean SESSION_DEBUG_MESSAGE_FLAG = false;

    private String DEFAULT_SENDER_MAIL_ADDRESS = null;

    private String DEFAULT_SENDER_NAME = null;

    private String CONTENT_TYPE = null;

    private String CHARSET_TYPE = null;

    private String PLAIN_CONTENT_TYPE = null;

    private String HTML_CONTENT_TYPE = null;

    private String ENCODING_TYPE = null;

    /* Template Html 을 활용하여 메일 Body 부분을 생성하기 위한 spec */
    private String BODY_TEMPLATE_DIRECTORY = "#docRoot/WEB-INF/classes/tzHome/contents/mail";

    private String BODY_TEMPLATE_HTML = "";

    private String IMG_SERVER_IP = "";

    private String IMG_SERVER_PORT = "";

    private boolean SAVE_RESOLVEDHTML_FLAG = false;

    private String SAVE_RESOLVEDHTML_DIR = "";

    /* NewMessageTest 발송을 위한 모드와 수신자 */
    private boolean TEST_MODE = true;

    private String[] TEST_RECEIVERS;

    @Autowired
    @Qualifier("appProperties")
    private Properties appProperties;

    /**
     * Mail의 생성자.
     *
     * @throws Exception
     */
    public void setMail() throws Exception {
        setMail("default", "default");
    }

    /**
     * Mail의 생성자.
     *
     * @param mailSpec 설정된 Mail Spec Configuration Information.
     * @throws Exception
     */
    public void setMail(String mailSpec) throws Exception {
        setMail(mailSpec, "default");
        setMimeMessage(getSession(SESSION_DEBUG_MESSAGE_FLAG));
        setDefaultFromAddress();
    }

    /**
     * Mail의 생성자.
     *
     * @param mailSpec
     *            com.tz.tz-framework.xml 파일에 설정된 Mail Spec Configuration
     *            Information.
     * @param bodyTemplate
     *            com.tz.tz-framework.xml 파일에 설정된 Body Template
     *            Configuration Information.
     */
    public void setMail(String mailSpec, String bodyTemplate) throws Exception {
        appProperties = ConfigUtil.getProperties(appProperties);

        checkTestOption();

        if(mailSpec.equals("")){
            mailSpec = "default";
        }
        if(bodyTemplate.equals("")){
            bodyTemplate = "default";
        }
        setMailSpec(mailSpec);
        setBodyTemplate(bodyTemplate);

        setMimeMessage(getSession(SESSION_DEBUG_MESSAGE_FLAG));
        setDefaultFromAddress();
    }

    /**
     * 테스트 발송인지를 체크하여, spec에 지정된 수신자(복수선택가능)로 세팅한다.
     *
     * @throws Exception
     */
    private void checkTestOption() throws Exception{
        this.TEST_MODE = Boolean
                .parseBoolean(StringUtil.getText(appProperties.getProperty("tz.mail.default.testMode")) == null ? "false"
                        : StringUtil.getText(appProperties.getProperty("tz.mail.default.testMode")));
        if(this.TEST_MODE){
            StringTokenizer st = new StringTokenizer(StringUtil.getText(appProperties
                    .getProperty("tz.mail.default.testReceivers")), ",");
            this.TEST_RECEIVERS = new String[st.countTokens()];
            int i = 0;
            while(st.hasMoreTokens()){
                this.TEST_RECEIVERS[i] = (String)st.nextElement();
                i++;
            }

            if(this.TEST_RECEIVERS.length == 0){
                throw new Exception("checkTestOption()");
            }
        }
    }

    /**
     * mail 발송에 필요한 기본 정보(spec, com.tz.tz-framework.xml에 설정)을 설정한다.
     *
     * @param com
     *            .tz.sheepshare-framework.xml 에 지정한 mailSpec의 이름
     * @throws Exception
     */
    private void setMailSpec(String mailSpec) throws Exception{
        this.mailSpec = mailSpec;
        this.MAIL_HOST = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".host"));
        this.MAIL_PORT = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".port"));
        this.MAIL_AUTH = Boolean.parseBoolean(StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec
                + ".auth")) == null ? "false" : StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec
                + ".auth")));
        this.MAIL_AUTH_ID = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".authId"));
        this.MAIL_AUTH_PWD = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".authPwd"));
        this.SESSION_DEBUG_MESSAGE_FLAG = Boolean.parseBoolean(StringUtil.getText(appProperties.getProperty("tz.mail."
                + mailSpec + ".sessionDebugMessageUseYN")) == null ? "false" : StringUtil.getText(appProperties
                .getProperty("tz.mail." + mailSpec + ".sessionDebugMessageUseYN")));
        this.DEFAULT_SENDER_MAIL_ADDRESS = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec
                + ".defaultSenderMailAddress"));
        this.DEFAULT_SENDER_NAME = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec
                + ".defaultSenderName"));
        this.CONTENT_TYPE = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".contentType"));
        this.CHARSET_TYPE = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".charsetType"));
        this.PLAIN_CONTENT_TYPE = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec
                + ".plainContentType"));
        this.HTML_CONTENT_TYPE = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec
                + ".htmlContentType"));
        this.ENCODING_TYPE = StringUtil.getText(appProperties.getProperty("tz.mail." + mailSpec + ".encodingType"));
    }

    /**
     * html template을 이용하여 mail전송 body message를 구성할 경우 필요한 정보를 설정한다. 관련 정보는
     * com.tz.tz-framework.xml 에서 읽어온다.
     *
     * @param com
     *            .tz.sheepshare-framework.xml에 지정한 bodyTemplate의 이름
     * @throws Exception
     */
    private void setBodyTemplate(String bodyTemplate) throws Exception{
        this.bodyTemplate = bodyTemplate;
        this.BODY_TEMPLATE_HTML = bodyTemplate + ".html";
        //		this.IMG_SERVER_IP = getConfigValue(
        //				this.bodyTemplate + "img-server-ip", this.IMG_SERVER_IP);
        //		this.IMG_SERVER_PORT = getConfigValue(this.bodyTemplate
        //				+ "img-server-port", this.IMG_SERVER_PORT);
        //		this.SAVE_RESOLVEDHTML_FLAG = getConfigValue(this.bodyTemplate
        //				+ "save-resolvedhtml-flag", this.SAVE_RESOLVEDHTML_FLAG);
        //		this.SAVE_RESOLVEDHTML_DIR = getConfigValue(this.bodyTemplate
        //				+ "save-resolvedhtml-dir", this.SAVE_RESOLVEDHTML_DIR);
    }

    /**
     * 디버깅을 위해 com.tz.tz-framework.xml 에 mail관련하여 설정한 내용을 출력한다. 명시적으로 호출하여
     * 사용한다.
     */
    public void printCurrentConfigInfo(){
        StringBuffer buf = new StringBuffer();
        buf.append("-------------------------------------------------------------------" + "\n");
        buf.append("[" + this.mailSpec + "],[" + this.bodyTemplate + "]" + "\n");
        buf.append(" *TEST_MODE                   " + this.TEST_MODE + "\n");
        if(TEST_RECEIVERS != null){
            buf.append(" *TEST_RECEIVERS              ");
            for(int i = 0; i < this.TEST_RECEIVERS.length; i++){
                buf.append(this.TEST_RECEIVERS[i] + " ");
            }
        }
        buf.append("\n");
        buf.append(" *MAIL_HOST                   " + this.MAIL_HOST + "\n");
        buf.append(" *MAIL_PORT                   " + this.MAIL_PORT + "\n");
        buf.append(" *MAIL_AUTH                   " + this.MAIL_AUTH + "\n");
        buf.append(" *MAIL_AUTH_ID                " + this.MAIL_AUTH_ID + "\n");
        buf.append(" *MAIL_AUTH_PWD          " + this.MAIL_AUTH_PWD + "\n");
        buf.append(" *SESSION_DEBUG_MESSAGE_FLAG  " + this.SESSION_DEBUG_MESSAGE_FLAG + "\n");
        buf.append(" *DEFAULT_SENDER_MAIL_ADDRESS " + this.DEFAULT_SENDER_MAIL_ADDRESS + "\n");
        buf.append(" *DEFAULT_SENDER_NAME         " + this.DEFAULT_SENDER_NAME + "\n");
        buf.append(" *CONTENT_TYPE                " + this.CONTENT_TYPE + "\n");
        buf.append(" *CHARSET_TYPE                " + this.CHARSET_TYPE + "\n");
        buf.append(" *PLAIN_CONTENT_TYPE          " + this.PLAIN_CONTENT_TYPE + "\n");
        buf.append(" *HTML_CONTENT_TYPE           " + this.HTML_CONTENT_TYPE + "\n");
        buf.append(" *ENCODING_TYPE               " + this.ENCODING_TYPE + "\n");
        buf.append(" *BODY_TEMPLATE_DIRECTORY     " + this.BODY_TEMPLATE_DIRECTORY + "\n");
        buf.append(" *BODY_TEMPLATE_HTML          " + this.BODY_TEMPLATE_HTML + "\n");
        buf.append(" *IMG_SERVER_IP               " + this.IMG_SERVER_IP + "\n");
        buf.append(" *IMG_SERVER_PORT             " + this.IMG_SERVER_PORT + "\n");
        buf.append(" *SAVE_RESOLVEDHTML_FLAG      " + this.SAVE_RESOLVEDHTML_FLAG + "\n");
        buf.append(" *SAVE_RESOLVEDHTML_DIR       " + this.SAVE_RESOLVEDHTML_DIR + "\n");
        buf.append("-------------------------------------------------------------------");

        logger.debug(buf.toString());
    }

    private void setDefaultFromAddress() throws Exception{
        setFromMailAddress(DEFAULT_SENDER_MAIL_ADDRESS, DEFAULT_SENDER_NAME);
    }

    private void setMimeMessage(Session session) throws Exception{
        try{
            msg = new MimeMessage(session);
            msg.setSentDate(new Date());
        }catch(MessagingException e){
            throw new Exception("setMimeMessage(Session session)", e);
        }
    }

    private Session getSession(boolean debugMode){
        Properties props = new Properties();
        Session session = null;
        Authenticator auth = null;
        // could use Session.getTransport() and Transport.connect()
        // assume we're using SMTP

        props.put(MAIL_SMTP_HOST, MAIL_HOST);
        props.put(MAIL_SMTP_PORT, MAIL_PORT);

        if(MAIL_AUTH){
            props.put(MAIL_SMTP_AUTH, "true");
            auth = new MailAuthenticator(MAIL_AUTH_ID, MAIL_AUTH_PWD);
            session = Session.getDefaultInstance(props, auth);
        }else{
            session = Session.getDefaultInstance(props, null);
        }

        session.setDebug(debugMode);
        return session;
    }

    /**
     * 보내는 사람의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @param name
     *            보내는 사람 이름
     * @exception Exception
     */
    public void setFromMailAddress(String mailAddress, String name) throws Exception{
        try{
            InternetAddress sender = new InternetAddress(mailAddress, name, CHARSET_TYPE);
            msg.setFrom(sender);
        }catch(MessagingException e){
            throw new Exception(e);
        }catch(UnsupportedEncodingException e){
            throw new Exception(e);
        }
    }

    /**
     * 보내는 사람의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @exception Exception
     */
    public void setFromMailAddress(String mailAddress) throws Exception{
        try{
            InternetAddress sender = new InternetAddress(mailAddress);
            msg.setFrom(sender);
        }catch(MessagingException e){
            throw new Exception(e);
        }
    }

    /**
     * 받는 사람(To) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @param name
     *            받는 사람 이름
     * @exception Exception
     */
    public void setToMailAddress(String mailAddress, String name) throws Exception{
        setMailAddress(mailAddress, name, Message.RecipientType.TO);
    }

    /**
     * 받는 사람(Cc - 참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @param name
     *            받는 사람 이름
     * @exception Exception
     */
    public void setCcMailAddress(String mailAddress, String name) throws Exception{
        setMailAddress(mailAddress, name, Message.RecipientType.CC);
    }

    /**
     * 받는 사람(Bcc - 비밀참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @param name
     *            받는 사람 이름
     * @exception Exception
     */
    public void setBccMailAddress(String mailAddress, String name) throws Exception{
        setMailAddress(mailAddress, name, Message.RecipientType.BCC);
    }

    private void setMailAddress(String mailAddress, String name, Message.RecipientType type) throws Exception{
        setMailAddress(new String[] { mailAddress }, new String[] { name }, type);
    }

    /**
     * 받는 사람(To) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address String Array
     * @param name
     *            받는 사람 이름의 String Array
     * @exception Exception
     */
    public void setToMailAddress(String[] mailAddress, String[] name) throws Exception{
        setMailAddress(mailAddress, name, Message.RecipientType.TO);
    }

    /**
     * 받는 사람(Cc - 참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address String Array
     * @param name
     *            받는 사람 이름의 String Array
     * @exception Exception
     */
    public void setCcMailAddress(String[] mailAddress, String[] name) throws Exception{
        setMailAddress(mailAddress, name, Message.RecipientType.CC);
    }

    /**
     * 받는 사람(Bcc - 비밀참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address String Array
     * @param name
     *            받는 사람 이름의 String Array
     * @exception Exception
     */
    public void setBccMailAddress(String[] mailAddress, String[] name) throws Exception{
        setMailAddress(mailAddress, name, Message.RecipientType.BCC);
    }

    private void setMailAddress(String[] mailAddress, String[] name, Message.RecipientType type) throws Exception{
        int i = 0;
        try{

            InternetAddress[] recipients = new InternetAddress[mailAddress.length];
            for(i = 0; i < mailAddress.length; i++){
                recipients[i] = new InternetAddress(mailAddress[i], name[i], this.CHARSET_TYPE);
            }
            msg.setRecipients(type, recipients);

        }catch(MessagingException e){
            throw new Exception(e);
        }catch(UnsupportedEncodingException e){
            throw new Exception(e);
        }
    }

    /**
     * 받는 사람(To) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @exception Exception
     */
    public void setToMailAddress(String mailAddress) throws Exception{
        setMailAddress(mailAddress, Message.RecipientType.TO);
    }

    /**
     * 받는 사람(Cc - 참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @exception Exception
     */
    public void setCcMailAddress(String mailAddress) throws Exception{
        setMailAddress(mailAddress, Message.RecipientType.CC);
    }

    /**
     * 받는 사람(Bcc - 비밀참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address
     * @exception Exception
     */
    public void setBccMailAddress(String mailAddress) throws Exception{
        setMailAddress(mailAddress, Message.RecipientType.BCC);
    }

    private void setMailAddress(String mailAddress, Message.RecipientType type) throws Exception{
        setMailAddress(new String[] { mailAddress }, type);
    }

    /**
     * 받는 사람(To) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address String Array
     * @exception Exception
     */
    public void setToMailAddress(String[] mailAddress) throws Exception{
        setMailAddress(mailAddress, Message.RecipientType.TO);
    }

    /**
     * 받는 사람(Cc - 참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address String Array
     * @exception Exception
     */
    public void setCcMailAddress(String[] mailAddress) throws Exception{
        setMailAddress(mailAddress, Message.RecipientType.CC);
    }

    /**
     * 받는 사람(Bcc - 비밀참조) 의 Mail 주소를 설정 한다.
     *
     * @param mailAddress
     *            aaa@bbb.com 형식의 internet mail address String Array
     * @exception Exception
     */
    public void setBccMailAddress(String[] mailAddress) throws Exception{
        setMailAddress(mailAddress, Message.RecipientType.BCC);
    }

    private void setMailAddress(String[] mailAddress, Message.RecipientType type) throws Exception{
        int i = 0;
        try{

            InternetAddress[] recipients = new InternetAddress[mailAddress.length];
            for(i = 0; i < mailAddress.length; i++){
                recipients[i] = new InternetAddress(mailAddress[i]);
            }
            msg.setRecipients(type, recipients);

        }catch(MessagingException e){
            throw new Exception(e);
        }
    }

    /**
     * 설정된 메일을 전송한다.
     *
     * @exception Exception
     */
    public void send() throws Exception{
        try{
            if(this.TEST_MODE){
                logger.debug("send()");
                setToMailAddress(this.TEST_RECEIVERS);
            }
            Transport.send(msg);
        }catch(MessagingException e){
            throw new Exception(e);
        }
    }

    /**
     * 메일 제목을 설정한다.
     *
     * @param subject
     *            메일 제목
     * @exception Exception
     */
    public void setSubject(String subject) throws Exception{
        try{
            ((MimeMessage)msg).setSubject(subject, CHARSET_TYPE);
        }catch(MessagingException e){
            throw new Exception(e);
        }
    }

    /**
     * text/plain 의 메일 메시지 내용을 설정 한다.
     *
     * @param textMessage
     *            메일 내용
     * @exception Exception
     */
    public void setText(String textMessage) throws Exception{
        try{
            msg.setContent(textMessage, PLAIN_CONTENT_TYPE);
            msg.setHeader(CONTENT_TRANSFER_ENCODING, CONTENT_TYPE);
            msg.saveChanges();
        }catch(MessagingException e){
            throw new Exception(e);
        }
    }

    /**
     * text/html 의 메일 메시지 내용을 설정 한다.
     *
     * @param htmlMessage
     *            메일 내용
     * @exception Exception
     */
    public void setHtml(String htmlMessage) throws Exception{
        try{
            msg.setDataHandler(new DataHandler(new ByteArrayDataSource(htmlMessage, HTML_CONTENT_TYPE, CHARSET_TYPE)));
            msg.setHeader(CONTENT_TRANSFER_ENCODING, CONTENT_TYPE);
            msg.saveChanges();
        }catch(MessagingException e){
            throw new Exception(e);
        }
    }

    //private void attachFileSourceArray(MimeMultipart multiPart, String[] fileNames) throws MessagingException,
    private void attachFileSourceArray(MimeMultipart multiPart, Map<String, Object> fileNames) throws MessagingException,
            UnsupportedEncodingException, IOException{
        MimeBodyPart[] fileBodyPartArray = null;

        String[] sfileName = null;

        if(fileNames == null){
            fileBodyPartArray = new MimeBodyPart[0];
        }else{
            sfileName = fileNames.get("fileList").toString().split(",");
        	fileBodyPartArray = new MimeBodyPart[sfileName.length];
        }

        for(int i = 0; i < fileBodyPartArray.length; i++){
            fileBodyPartArray[i] = new MimeBodyPart();

            FileDataSource fileSource = new FileDataSource(sfileName[i]);
            fileBodyPartArray[i].setDataHandler(new DataHandler(fileSource));
            fileBodyPartArray[i].setFileName(MimeUtility.encodeText(fileSource.getName(), CHARSET_TYPE, ENCODING_TYPE));
            multiPart.addBodyPart(fileBodyPartArray[i]);
        }
    }



    /**
     * text/plain 의 메일 메시지 내용과 첨부파일을 설정 한다.
     *
     * @param textMessage
     *            메일 내용
     * @param fileName
     *            첨부파일 이름
     * @exception Exception
     */
    /*public void setTextAndFile(String textMessage, String fileName) throws Exception{
        setTextAndFile(textMessage, new String[] { fileName });
    }*/

    /**
     * text/plain 의 메일 메시지 내용과 첨부파일을 설정 한다.
     *
     * @param textMessage
     *            메일 내용
     * @param fileNames
     *            첨부파일 이름 String Array
     * @exception Exception
     */
    //public void setTextAndFile(String textMessage, String[] fileNames) throws Exception{
    public void setTextAndFile(String textMessage, Map<String, Object> fileNames) throws Exception{
        try{

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(textMessage, CHARSET_TYPE);

            MimeMultipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(textPart);

            attachFileSourceArray(multiPart, fileNames);

            multiPart.setSubType(SUB_TYPE);
            msg.setContent(multiPart);
            msg.saveChanges();

        }catch(MessagingException e){
            throw new Exception(e);

        }catch(UnsupportedEncodingException e){
            throw new Exception(e);
        }
    }

    /**
     * text/html 의 메일 메시지 내용과 첨부파일을 설정 한다.
     *
     * @param htmlMessage
     *            메일 내용
     * @param fileName
     *            첨부파일 이름
     * @exception Exception
     */
    /*public void setHtmlAndFile(String htmlMessage, String fileName) throws Exception{
        setHtmlAndFile(htmlMessage, new String[] { fileName });
    }*/

    /**
     * text/html 의 메일 메시지 내용과 첨부파일을 설정 한다.
     *
     * @param htmlMessage
     *            메일 내용
     * @param fileNames
     *            첨부파일 이름 String Array
     * @exception Exception
     */
    //public void setHtmlAndFile(String htmlMessage, String[] fileNames) throws Exception{
    public void setHtmlAndFile(String htmlMessage, Map<String, Object> attach) throws Exception{
        try{
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setDataHandler(new DataHandler(new ByteArrayDataSource(htmlMessage, HTML_CONTENT_TYPE,
                    CHARSET_TYPE)));
            htmlPart.setHeader(CONTENT_TRANSFER_ENCODING, CONTENT_TYPE);

            MimeMultipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(htmlPart);

            attachFileSourceArray(multiPart, attach);

            multiPart.setSubType("mixed");
            msg.setContent(multiPart);
            msg.saveChanges();

        }catch(MessagingException e){
            throw new Exception(e);
        }catch(UnsupportedEncodingException e){
            throw new Exception(e);
        }
    }

    public String loadHtml(String htmlTemplate, Map<String, Object> var) throws Exception{
        return this.loadHtml(htmlTemplate, var, this.CHARSET_TYPE);
    }

    /**
     *
     * <pre>
     * template html 을 읽어들여 메일의 내용을 구성한다.
     * </pre>
     *
     * @param htmlTemplate
     *            htmlTemplete 이름
     * @param var
     *            htmlTemplete 속의 변수에 세팅되어야 하는 값
     * @return String
     * @exception Exception
     */
    public String loadHtml(String htmlTemplate, Map<String, Object> var, String charset) throws Exception{
        StringBuffer htmlStr = new StringBuffer();
        String resultStr = null;
        BufferedReader in = null;
        try{
            String htmlName = "";
            if(!htmlTemplate.equals("")) {
                htmlName = htmlTemplate + ".html";
            } else {
                htmlName = this.BODY_TEMPLATE_HTML;
            }
            String htmlPathNName = this.BODY_TEMPLATE_DIRECTORY + "/" + this.mailSpec + "/" + htmlName;
            // "#docRoot/WEB-INF/classes/tzHome/contents/mail";
            if(htmlPathNName.indexOf("#docRoot") > -1){
                htmlPathNName = StringUtil.replace(htmlPathNName, "#docRoot", Constants.docRoot);
                if(Constants.stage.equals("L")) {
                    // C:\TZ_IDE\workspace\tz.sheepshare\src\main\webapp\WEB-INF\classes\tzHome
                    // C:\TZ_IDE\workspace\tz.sheepshare\target\classes\tzHome
                    htmlPathNName = StringUtil.replace(htmlPathNName, "\\", "/");
                    htmlPathNName = StringUtil.replace(htmlPathNName, "src/main/webapp/WEB-INF", "target");
                }
            }
            in = new BufferedReader(new InputStreamReader(new FileInputStream(htmlPathNName), charset));
            String str = null;
            while((str = in.readLine()) != null){
                htmlStr.append(str).append("\n");
            }

            if(htmlStr != null && htmlStr.length() > 0){
                resultStr = replaceVariables(htmlStr.toString(), var);
                if(this.SAVE_RESOLVEDHTML_FLAG){
                    saveResolvedHtmlFile(htmlName, resultStr);
                }
            }
        }catch(Exception e){
            throw new Exception(e);
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(IOException e){
                throw new Exception(e);
            }
        }
        return resultStr;
    }

    /**
     * ${userKnm} 과 같은 위치에 userKnm 을 key로 같은 값을 치완한다.
     *
     * @param orgStr
     * @param var
     * @return String
     */
    public String replaceVariables(String orgStr, Map<String, Object> var){
        if(var != null){
            var.put("imgServerIp", IMG_SERVER_IP);
            var.put("imgServerPort", IMG_SERVER_PORT);
            Object[] key = var.keySet().toArray();
            for(int i = 0; i < key.length; i++){
                if(!((var.get((String)key[i])) instanceof String)) continue;
                String value = (String)var.get((String)key[i]);
                value = value == null ? "" : value;
                orgStr = orgStr.replaceAll("\\$\\{" + ((String)key[i]) + "\\}", StringUtil.quoteReplacement(value));
            }
        }

        return orgStr;
    }

    /**
     * 치환한 스트링을 파일로 저장하여 미리보기를 할 수 있도록 한다. 저장 여부는 com.tz.tz-framework.xml에서
     * 설정할 수 있다.
     *
     * @param htmlName
     * @param resultStr
     * @throws Exception
     */
    private void saveResolvedHtmlFile(String htmlName, String resultStr) throws Exception{
        if(resultStr != null){
            FileChannel foc = null;
            ByteBuffer buf = null;
            try{
                String path = this.SAVE_RESOLVEDHTML_DIR + "/" + htmlName;

                foc = new FileOutputStream(new File(path)).getChannel();
                buf = ByteBuffer.allocate(resultStr.getBytes().length);
                buf.put(resultStr.getBytes());
                buf.clear();
                foc.write(buf);
            }catch(Exception e){
                throw new Exception(e);
            }finally{
                try{
                    buf.clear();
                    foc.close();
                }catch(IOException e){
                    logger.error(e.getMessage());
                }
            }
        }
    }
}
