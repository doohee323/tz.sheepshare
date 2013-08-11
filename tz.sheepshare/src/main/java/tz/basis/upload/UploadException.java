package tz.basis.upload;

import tz.basis.exception.MessageException;

/**
 * 파일 업로드 모듈에서의 오류.
 * 
 * @author TZ
 */
public class UploadException extends MessageException {
	private static final long serialVersionUID = 1L;

	public UploadException(String message) {
		super(message);
	}

	public UploadException(String message, Throwable t) {
		super(message, t);
	}

}
