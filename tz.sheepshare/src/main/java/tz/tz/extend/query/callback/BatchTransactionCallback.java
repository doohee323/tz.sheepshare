package tz.extend.query.callback;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : 배치 연산 수행에서 개별 연산 수행을 추상화한 인터페이스. 일반적으로 Service 클래스 내부에서 구현하여
 *         사용하며, 배치 수행 중 개별 연산 수행을 정의하고 싶은 경우에 확장이 가능함
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
 * @param <T> generic type class
 */
public abstract class BatchTransactionCallback<T> {

	protected static final int DEFAULT_BATCH_SIZE = 100;

	/**
	 *
	 * <pre>
	 *  기본적인 배치 크기 지정
	 * </pre>
	 *
	 * @return 배치 실행 단위
	 */
	public int getBatchSize()	{
		return DEFAULT_BATCH_SIZE;
	}

	/**
	 *
	 * <pre>
	 *  배치처리를 위한 사용되는 STATEMENT ID
	 * </pre>
	 *
	 * @return Statement id
	 */
	public abstract String getStatementId();

	/**
	 *
	 * <pre>
	 *  배치 수행 중 개별 데이터를 기준으로 반복 수행되는 연산
	 * </pre>
	 *
	 * @param record 전체 데이터 중 현재 처리할 데이터(컬렉션 중 하나의 엘리먼트)
	 * @return 개별 Row 데이터
	 */
	public Object doInTransaction(T record) {
		return record;
	}
}
