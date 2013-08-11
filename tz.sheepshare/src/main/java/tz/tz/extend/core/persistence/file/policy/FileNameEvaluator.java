package tz.extend.core.persistence.file.policy;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 이름을 재정의하기 위한 규칙 및 세부 로직을 정의한다.
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
public class FileNameEvaluator {

	private Pattern filePattern = Pattern.compile("\\{(.*?)\\}");

	/**
	 *
	 * <pre>
	 * 업로드 되는 파일의 이름을 재정의한다.
	 * </pre>
	 *
	 * @param pattern 파일 Rename을 위한 패턴 (ex. {fileName}[{date:yyyyMMddHHmmssSSS}].{extension} : text.txt => text[201301011111].txt)
	 * @param baseFileName
	 * @param override 같은 이름을 가지는 파일을 겹쳐쓸 것인지..
	 * @return
	 */
	public String buildIndividualFileName(String pattern, String baseFileName, boolean override)	{
		Matcher matcher = filePattern.matcher(pattern);

		int lastDot = baseFileName.lastIndexOf('.');

		String prefix = (lastDot == -1) ? baseFileName : baseFileName.substring(0, lastDot);
		String extension = (lastDot == -1) ? "" : baseFileName.substring(lastDot + 1);
		String part;
		StringBuffer fileName = new StringBuffer();

		while (matcher.find()) {
			part = matcher.group(1);

			if ("fileName".equals(part)) {
				part = prefix;
			} else if ("extension".equals(part)) {
				part = extension;
			} else if ("date:".equals(part.substring(0, 5))) {
				part = getDate(part.substring(5));
			}

			matcher.appendReplacement(fileName, part);
		}

		matcher.appendTail(fileName);

		if(!StringUtils.hasText(fileName.toString()))	{
			fileName.append(baseFileName);
		}

		return StringUtils.getFilename(fileName.toString());
	}

	/**
	 *
	 * <pre>
	 *  Current time 을 포멧팅하여 반환한다.
	 * </pre>
	 *
	 * @param format 표현형식
	 * @return
	 */
    private String getDate(String format) {
        return new SimpleDateFormat(format, java.util.Locale.KOREA).format(new java.util.Date());
    }
}
