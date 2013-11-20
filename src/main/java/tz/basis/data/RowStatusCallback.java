package tz.basis.data;

/**
 *
 * GridData 각 행에 대한 Insert/Update/Delete 연산을 수행한다.
 *
 * @author TZ
 *
 * @param <E>
 */
public interface RowStatusCallback<E> {

	public abstract void normal(E paramE, int paramInt);

	/**
	 *
	 * ROW INSERT 수행.
	 *
	 * @param record 추가된 행 데이터
	 * @param rowNum row number
	 */
	void insert(E record, int rowNum);

	/**
	 *
	 * ROW UPDATE 수행.
	 *
	 * @param newRecord 변경된 행 데이터
	 * @param oldRecord 변경되기 이전의 행 데이터
	 * @param rowNum row number
	 */
	void update(E newRecord, E oldRecord, int rowNum);

	/**
	 *
	 * ROW DELETE 수행.
	 *
	 * @param record 삭제된 행 데이터
	 * @param rowNum row number
	 */
	void delete(E record, int rowNum);

}
