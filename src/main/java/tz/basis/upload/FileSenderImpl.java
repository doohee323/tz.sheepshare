package tz.basis.upload;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import tz.basis.upload.handler.DownloadEventHandler;
import tz.basis.upload.persistence.DefaultPersistenceManager;
import tz.basis.upload.persistence.FileLoader;
import tz.basis.upload.persistence.PersistenceManager;

public class FileSenderImpl implements FileSender {

	private PersistenceManager persistenceManager = new DefaultPersistenceManager();

	/**
	 * 하나의 파일을 지정하여 서블릿 클라이언트로 전송.
	 *
	 * @param response
	 * @param urn
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void sendOctetStream(HttpServletRequest request, HttpServletResponse response, DownloadEventHandler handler, String folder, String individualFile) throws IOException {

		FileInfo fileInfo = new FileInfo(folder, individualFile);

		handler.preprocess(fileInfo);

		FileLoader fileLoader = persistenceManager.getFileLoader(folder, individualFile);

		String clientFileName = handler.createFileName(fileInfo);

		if(!StringUtils.hasText(clientFileName))	{
			clientFileName = individualFile;
		}

		response.setContentType("application/octet;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(clientFileName,"UTF8"));
		response.setContentLength(((int)fileLoader.getFileSize()));

		if (request.isRequestedSessionIdFromURL()) {
			// 세션 생겼어야할텐데.
			if (request.getSession(false) == null) {

			} else {
				response.sendRedirect(request.getRequestURI());
			}
		}

		fileLoader.sendFile(response.getOutputStream());
	}

	public void setPersistenceManager(PersistenceManager persistenceManager) {
		this.persistenceManager = persistenceManager;
	}
}
