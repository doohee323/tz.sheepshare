package tz.basis.upload.handler;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import tz.basis.upload.FileInfo;
import tz.basis.upload.MultiPartInfo;
import tz.basis.upload.persistence.PersistenceManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultUploadEventHandler implements UploadEventHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(DefaultUploadEventHandler.class);

	public void postprocess(String urn, MultiPartInfo info,
			PersistenceManager persistenceManager) {
		logger.info("form upload attributes : {}", info.getAttributes());
		Iterator<FileInfo> it = info.getFileInfos().iterator();
		while (it.hasNext()) {
			FileInfo fileInfo = it.next();
			logger.info(
					"file uploads : {} {} {} {} {}",
					new Object[] { fileInfo.getName(),
							Long.valueOf(fileInfo.getLength()),
							fileInfo.getType(), fileInfo.getFolder(),
							Integer.valueOf(fileInfo.getFileCount()) });
		}
	}

	public long getMaxUploadSize() {
		return 0;
	}

	public String createFileNameIfAccepted(String urn, FileInfo fileInfo) {
		return fileInfo.getName();
	}

	public void prepareStorage(PersistenceManager persistenceManager, String urn) {
		persistenceManager.delete(urn);
	}

	public String getFolder(HttpServletRequest request) {
		String path = request.getPathInfo();
		String urn = path.substring(0, path.lastIndexOf("/"));

		return urn;
	}

}
