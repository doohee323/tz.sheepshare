package tz.basis.core.ux.comweb.mvc.file;

import javax.servlet.http.HttpServletRequest;

import tz.basis.core.mvc.file.operator.AbstractTzFileOperator;

import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * TZ-FileUpload 모듈을 이용한 기본 파일처리 오퍼레이터
 *
 * @author TZ
 *
 */
public class ComWebFileOperator extends AbstractTzFileOperator {

    public boolean isMultiPartRequest(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

}
