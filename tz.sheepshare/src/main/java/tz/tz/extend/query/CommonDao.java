package tz.extend.query;

import java.util.List;
import java.util.Map;

import tz.basis.query.core.QueryExecutor;
import tz.extend.query.callback.BatchTransactionCallback;

import org.springframework.jdbc.core.RowMapper;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : {@link QueryExecutor}를 내부적으로 초기화하여 지원하는 편의 클래스. 배치 트랜잭션 모드를 지원하는 배치 연산 추가 제공.
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
public interface CommonDao {

	/**
	 * <pre>
	 * 단건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param <T> generic type class
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @param clazz generic type class
	 * @return 조회된 데이터 (단건) or null
	 */
	<T> T queryForObject(Object statementId, Object parameter, Class<T> clazz);

	/**
	 * <pre>
	 * 단건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @return 조회된 데이터 (단건) or null
	 */
	Map<String, Object> queryForMap(Object statementId, Object parameter);

	/**
	 * <pre>
	 * 다건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param <T> generic type class
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @param clazz generic type class
	 * @return 조회된 데이터 (0건 이상)
	 */
	<T> List<T> queryForList(Object statementId, Object parameter, Class<T> clazz);

	/**
	 * <pre>
	 * 다건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param <T> generic type class
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @param rowMapper ResultSet을 가공하기 위한 row mapper
	 * @return 조회된 데이터 (0건 이상)
	 */
	<T> List<T> queryForList(Object statementId, Object parameter, RowMapper<T> rowMapper);

	/**
	 * <pre>
	 * 다건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param <T> generic type class
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @param skipRows 커서를 스킵한다.
	 * @param maxRows  조회된 데이터가 maxRow를 초과하는 경우 maxRow로 건수를 제한한다.
	 * @param clazz generic type class
	 * @return 조회된 데이터 (0건 이상, maxRow 미만)
	 */
	<T> List<T> queryForList(Object statementId, Object parameter, int skipRows, int maxRows, Class<T> clazz);

	/**
	 * <pre>
	 * 다건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @return 조회된 데이터 (0건 이상)
	 */
	List<Map<String, Object>> queryForMapList(Object statementId, Object parameter);

	/**
	 * <pre>
	 * 다건 조회 연산을 수행하여 결과를 Map<String, String> 으로 반환한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @return 조회된 데이터 (0건 이상)
	 */
	List<Map<String, String>> queryForMapStringValueList(Object statementId, Object parameter);


	/**
	 * <pre>
	 * 다건 조회 연산을 수행한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @param skipRows 커서를 스킵한다.
	 * @param maxRows  조회된 데이터가 maxRow를 초과하는 경우 maxRow로 건수를 제한한다.
	 * @return 조회된 데이터 (0건 이상, maxRow 미만)
	 */
	List<Map<String, Object>> queryForMapList(Object statementId, Object parameter, int skipRows, int maxRows);

	/**
	 * <pre>
	 * 조회 결과를 정수형(Integer)으로 반환한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @return 조회된 결과 (Integer)
	 */
	Integer queryForInt(Object statementId, Object parameter);

	/**
	 * <pre>
	 * 조회 결과를 정수형(Long)으로 반환한다.)
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @return 조회된 결과 (Long)
	 */
	Long queryForLong(Object statementId, Object parameter);

	/**
	 * <pre>
	 * 입력/수정/삭제 연산을 수행한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter
	 * @return affected row count
	 */
	Integer update(Object statementId, Object parameter);

	/**
	 * <pre>
	 * 입력/수정/삭제  배치 연산을 수행한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter의 리스트
	 * @return affected row count
	 */
	int[] batchUpdate(Object statementId, List<?> parameter);

	/**
	 * <pre>
	 * 입력/수정/삭제 배치 연산을 수행한다.
	 * </pre>
	 *
	 * @param <T> generic type class
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter의 리스트
	 * @param callback 개별 행에 대한 조작 연산을 수행한 이후 호출되는 callback
	 * @return affected row count
	 */
	public <T> int[] batchUpdate(List<T> parameters, BatchTransactionCallback<T> callback);

	/**
	 * <pre>
	 * 조회한 결과의 각 행에 대한 연산을 배치로 처리한다.
	 * </pre>
	 *
	 * @param <T> generic type class
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter의 리스트
	 * @param clazz generic type class
	 * @param callback 개별 행에 대한 조작 연산을 수행한 이후 호출되는 callback
	 */
	public <T> void queryAndBatchUpdate(String statementId, Object parameter, Class<T> clazz, final BatchTransactionCallback<T> callback);

	/**
	 * <pre>
	 * Procedure/Fucntion을 실행한다.
	 * </pre>
	 *
	 * @param statementId iBatis namespace + statementId
	 * @param parameter 입력 parameter의 리스트
	 * @return SP/Fun을 처리하여 수행한 결과
	 */
	Map<String, Object> executeCallStatement(Object statementId, Object parameter);

}