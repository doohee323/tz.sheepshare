package tz.extend.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : DownloadUtil
 * 설    명 : 파일 다운로드를 위한 유틸 글래스
 * 작 성 자 : TZ
 * 작성일자 : 2013.04.13
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class DownloadUtil {

	private static final int BUFFER_SIZE = 4096;

	private final HttpServletRequest request;

	private final HttpServletResponse response;

	private String contentType = "application/octect-stream";

	private String encoding = "ISO-8859-1";

	/**
	 * <pre>
	 * DownloadUtil
	 * </pre>
	 * @param req
	 * @param res
	 */
	public DownloadUtil(HttpServletRequest req, HttpServletResponse res) {
		super();
		this.request = req;
		this.response = res;
	}

    /**
     * <pre>
     * 현재 HttpRequest를 url로 Redirect 시킨다.
     * </pre>
     * @param  url http url
     */
	public void redirectTo(String url) throws Exception {
		try {
			response.sendRedirect(url);
		} catch (IOException e) {
			throw new Exception("IOExcetion occur : " + url, e);
		}
	}

	/**
     * <pre>
     * streamTo
     * </pre>
     * @param  file
     */
	public void streamTo(File file) throws Exception {
		streamTo(file, file.getName());
	}

    /**
     * <pre>
     * file을 클라이언트에 alias에 해당하는 이름으로 스트림으로 전송한다.
     * </pre>
     * @param  file File Object
     * @param  alias 다운로드 파일
     */
	public void streamTo(File file, String alias) throws Exception {
		FileInputStream fin = null;
		BufferedOutputStream fout = null;

		try {
			if (!file.canRead()) {
				throw new Exception("file cannot read : " + file.getName());
			}

			response.reset();

			setHeaders(alias, file.length());

			fin = new FileInputStream(file);
			FileChannel fc = fin.getChannel();
			fout = new java.io.BufferedOutputStream(response.getOutputStream());

			ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			int length = -1;
			while ((length = fc.read(buffer)) != -1) {
				buffer.flip();
				fout.write(buffer.array(), 0, length);
				buffer.clear();
			}
		} catch (FileNotFoundException e) {
			throw new Exception("file not found : " + file.getName(), e);
		} catch (IOException e) {
			throw new Exception("IOExcetion occur : " + file.getName(), e);
		} finally {
			if (fin != null)
				try {
					fin.close();
				} catch (Exception e) {
				}
			if (fout != null)
				try {
					fout.flush();
					fout.close();
				} catch (Exception e) {
				}
		}
	}

    /**
     * <pre>
     * Download response의 header 정보를 설정한다.
     * </pre>
     * @param  filename 다운로드 파일명
     * @param  filelength 파일 사이즈
     */
	private void setHeaders(String filename, long filelength) {

		response.setContentType(getContentType(filename));

		if (request.getHeader("User-Agent").indexOf("MSIE 5.5") != -1) {
			response.setHeader("Content-Disposition", "filename=" + getEncodedName(filename) + ";");
		} else {
			response.setHeader("Content-Disposition", "attachment;filename=" + getEncodedName(filename) + ";");
		}

		response.setHeader("Content-Length", new CastUtil(filelength).cString());
		response.setHeader("Content-Transfer-Encoding", "binary;");
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");
	}

	/**
	 * <pre>
	 * encoding name 을 가져오는 메소드
	 * </pre>
	 * @param name
	 * @return
	 */
	private String getEncodedName(String name) {
		String filename = name;
		try {
            filename = new String(filename.getBytes(), this.encoding);

		} catch (UnsupportedEncodingException e) {
		}
		return filename;
	}

	/**
	 * <pre>
	 * content type을 가져오는 메소드
	 * </pre>
	 * @param filename
	 * @return
	 */
	private String getContentType(String filename) {
		return this.contentType += "; charset=" + this.encoding;
	}

	/**
	 * <pre>
	 * encoding 설정
	 * </pre>
	 * @param encoding
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

}
