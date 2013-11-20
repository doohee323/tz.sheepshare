package tz.extend.core.persistence.file.policy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import tz.extend.util.ObjUtil;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 정책관리 기본 클래스
 * 작 성 자 : TZ
 * 작성일자 : 2013-02-01
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-02-01             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class FilePolicyImpl extends AbstractFilePolicy {



	private boolean isCurrDateFolder = true;
	private String  endDirectory;


	public boolean getIsCurrDateFolder() {
		return isCurrDateFolder;
	}

	public void setCurrDateFolder(boolean isCurrDateFolder) {
		this.isCurrDateFolder = isCurrDateFolder;
	}

	public String getEndDirectory() {
		return endDirectory;
	}

	public void setEndDirectory(String endDirectory) {
		this.endDirectory = endDirectory;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#isDenied(String fileName)
	 */
	public boolean isDenied(String fileName) {
		boolean result = false;

		if(StringUtils.hasText(denied))	{
			Pattern pattern = WildcardAnalyzer
			.compileWildcardPattern(denied, false);

			result = pattern.matcher(fileName).matches();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * tz.core.persistence.file.policy.FilePolicy#isAllowed(String fileName)
	 */
	public boolean isAllowed(String fileName) {
		boolean result = true;

		if(StringUtils.hasText(allowed))	{
			Pattern pattern = WildcardAnalyzer
			.compileWildcardPattern(allowed, false);

			result = pattern.matcher(fileName).matches();
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see tz.core.persistence.file.policy.AbstractFilePolicy#getSubDirectory()
	 */
	@Override
	public String getSubDirectory() {
		if ( getIsCurrDateFolder() ) {
			if (ObjUtil.isNull(endDirectory)) {
				return super.getSubDirectory() + File.separator + new SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA).format(new java.util.Date());
			}
			else {
				return super.getSubDirectory() + File.separator + endDirectory + File.separator + new SimpleDateFormat("yyyyMMdd", java.util.Locale.KOREA).format(new java.util.Date());
			}
		}
		else {
			if (ObjUtil.isNull(endDirectory)) {
				return super.getSubDirectory();
			}
			else {
				return super.getSubDirectory() + File.separator + endDirectory ;
			}
		}
	}
}
