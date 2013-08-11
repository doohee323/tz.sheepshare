package tz.basis.integration.mail;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : ByteArrayDataSource
 * 설    명 : 이 클래스는 스트링 처리를 위해서 DataSource를 implements 했다.
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.activation.DataSource;

public class ByteArrayDataSource implements DataSource {

    private byte[] data; // data

    private String type; // content-type

    /**
     * ByteArrayDataSource 생성자
     *
     * @param data
     * @param type
     * @param charSet
     */
    public ByteArrayDataSource(String data, String type, String charSet) {
        try {
            this.data = data.getBytes(charSet); // 한글로 Encoding

        } catch (UnsupportedEncodingException uex) {
        }
        this.type = type;
    }

    /**
     * InputStream을 반환한다.
     *
     * @return  반환된 InputStream
     */
    public InputStream getInputStream() throws IOException{
        if (data == null) throw new IOException("no data");
        return new ByteArrayInputStream(data);
    }

    /**
     * OutputStream을 반환한다.
     *
     * @return 반환된 OutputStream
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException{
        throw new IOException("cannot do this");
    }

    /**
     * ContentType을 반환한다.
     *
     * @return 반환된 ContentType
     */
    public String getContentType(){
        return type;
    }

    /**
     * Name을 반환한다.
     *
     * @return String 반환된 Name
     */
    public String getName(){
        return "dummy";
    }

}



