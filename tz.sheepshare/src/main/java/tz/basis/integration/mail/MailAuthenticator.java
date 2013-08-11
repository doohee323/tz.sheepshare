package tz.basis.integration.mail;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : MailAuthenticator
 * 설    명 : 메일 인증 처리 지원
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

import javax.mail.Authenticator;

/**
 * <pre>
 * Mail Authenticator
 * </pre>
 */
public class MailAuthenticator extends Authenticator {

    private String id;

    private String pw;

    /**
     * MailAuthenticator 생성자
     *
     * @param id ID
     * @param pw pwd
     */
    public MailAuthenticator(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    protected javax.mail.PasswordAuthentication getPwdAuthentication(){
        return new javax.mail.PasswordAuthentication(this.id, this.pw);
    }
}




