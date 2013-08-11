package tz.basis.upload;

import org.apache.commons.fileupload.FileItemHeaders;

/**
 * 파일 업로드/다운로드 개별 파일 정보.
 *
 * @author TZ
 */
public final class FileInfo {
	final private String fieldName;
	final private String name;
	final private String type;
	final private FileItemHeaders headers;
	final private String folder;
	final private int fileCount;
	private long length;
	private String callName;

	public FileInfo(String folder, String name) {
		this(null, name, null,   null, folder, 0);
	}

	public FileInfo(String fieldName, String name, String type,
			FileItemHeaders headers, String folder, int fileCount) {
		this.fieldName = fieldName;
		this.name = name;
		this.type = type;
		this.headers = headers;
		this.folder = folder;
		this.fileCount = fileCount;
	}

	public FileInfo(FileInfo f, String callName, long length) {
		this(f.getFieldName(), f.getName(), f.getType(), f.getHeaders(), f.getFolder(), f.getFileCount());
		this.length = length;
		this.callName = callName;
	}

	/**
	 * 멀티파트 업로드 폼에서 주어진 파일 이름.
	 * @see #getCallName()
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * Returns the content type passed by the browser or null if not defined.
	 * @return
	 */
	public String getType() {
		return type;
	}
	public String getFolder() {
		return folder;
	}
	/**
	 * 멀티파트 파일 순번.
	 * @return
	 */
	public int getFileCount() {
		return fileCount;
	}

	public String getFieldName() {
		return fieldName;
	}

	public FileItemHeaders getHeaders() {
		return headers;
	}

	public long getLength() {
		return length;
	}

	/**
	 * PersistenceManager에서 사용될 개별 파일 이름.
	 * @see #getName()
	 * @return
	 */
	public String getCallName() {
		return callName;
	}

}
