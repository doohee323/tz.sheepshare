package tz.extend.core.persistence.file.policy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import tz.basis.upload.FileInfo;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 파일 정책관리자
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
public class FilePolicyManager {

	private List<FilePolicyInterceptor> interceptors = new ArrayList<FilePolicyInterceptor>();

	private PathMatcher matcher = new AntPathMatcher();
	private FileNameEvaluator nameEvaluator = new FileNameEvaluator();

	private FilePolicy defaultPolicy = new FilePolicyImpl();

	/**
	 *
	 * <pre>
	 *  요청된 패턴에 적용되어야 할 관리정책을 반환한다.
	 * </pre>
	 *
	 * @param pattern 입력된 패턴 (ex. 파일이름 , URL)
	 * @return filePolicy 관리정책
	 */
	public FilePolicy getFilePolicy(String pattern)	{

		for(FilePolicyInterceptor interceptor : interceptors)	{
			if(matcher.match(interceptor.getPattern(), pattern))	{
				return interceptor.getPolicy();
			}
		}

		return defaultPolicy;
	}

	/**
	 *
	 * <pre>
	 *  관리정책의 위반 여부를 검사한다.
	 * </pre>
	 *
	 * @param fileInfo 업로드할 파일의 정보
	 * @param policy 관리정책
	 * @throws FilePolicyViolationException
	 */
	public void checkFilePolicy(FileInfo fileInfo, FilePolicy policy)	{

		/*
		 * 허용되지 않는 파일의 경우 예외발생
		 */
		if (policy.isDenied(fileInfo.getName())) {
			throw new FilePolicyViolationException("파일관리 정책(Denied pattern) 위반 - pattern={" + policy.getDeniedPattern() + "} fileName={" + fileInfo.getName() + "}");
		}

		if(!policy.isAllowed(fileInfo.getName())) {
			throw new FilePolicyViolationException("파일관리 정책(Allowed pattern) 위반 - pattern={" + policy.getAllowedPattern() + "} fileName={" + fileInfo.getName() + "}");
		}

		/*
		 * 파일의 크기를 체크함
		 */
		if (fileInfo.getLength() > 0 && fileInfo.getLength() > policy.getMaxSize()) {
			throw new FilePolicyViolationException("허용 가능한 파일 크기를 초과하였습니다. - maxsize={" + policy.getMaxSize() + "} filesize={" + fileInfo.getLength() + "}");
		}
	}

	/**
	 *
	 * <pre>
	 *  정책을 적용할 파일의 패턴 및 적용될 정책을 정의한다.
	 * </pre>
	 *
	 * @param interceptors  정책을 적용할 파일의 패턴
	 */
	public void setInterceptors(List<FilePolicyInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	/**
	 *
	 * <pre>
	 *  패턴매칭을 처리할 Matcher를 정의한다.
	 * </pre>
	 *
	 * @param matcher 패턴매칭을 처리할 Matcher
	 */
	public void setMatcher(PathMatcher matcher) {
		this.matcher = matcher;
	}

	/**
	 *
	 * <pre>
	 *  Rename 정책이 적용된 경우 처리할 NameEvaluator를 정의한다.
	 * </pre>
	 *
	 * @param nameEvaluator Rename 정책이 적용된 경우 처리할 NameEvaluator
	 */
	public void setNameEvaluator(FileNameEvaluator nameEvaluator) {
		this.nameEvaluator = nameEvaluator;
	}

	/**
	 * <pre>
	 *  Rename 정책이 적용된 경우 처리할 NameEvaluator를 반환한다.
	 * </pre>
	 * @return Rename 정책이 적용된 경우 처리할 NameEvaluator
	 */
	public FileNameEvaluator getNameEvaluator() {
		return nameEvaluator;
	}
}
