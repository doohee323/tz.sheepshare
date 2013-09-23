package tz.extend.core.message;


/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : MessageSource 생성을 위한 ORM 클래스
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * ---------------------------------------------------------------
 * </pre>
 *
 * @version 1.0
 */
public class DefaultMessage {
    
	/**
	 * <pre>
	 * 메세지코드
	 * </pre>
	 */
	private String code;
	/**
	 * <pre>
	 * 로케일코드
	 * </pre>
	 */
	private String locale;
	/**
	 * <pre>
	 * 메세지
	 * </pre>
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getLocale() {
		return locale;
	}

    public void setCode(String code){
        this.code = code;
    }

    
    public void setLocale(String locale){
        this.locale = locale;
    }

    
    public void setMessage(String message){
        this.message = message;
    }
}