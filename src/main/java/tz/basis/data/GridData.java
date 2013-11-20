package tz.basis.data;

import java.util.List;

import tz.basis.data.RowStatus;

/**
 *
 * @author TZ
 *
 * @param <E>
 */
public interface GridData<E> {

	/**
	 *
	 * @param rowIndex
	 * @return
	 */
	E get(int rowIndex);

	/**
	 * @return
	 */
	List<E> getList();

	/**
	 * @param rowIndex
	 * @return
	 */
	RowStatus getStatusOf(int rowIndex);

	/**
	 *
	 * @return
	 */
	int size();

	void forEachRow(RowStatusCallback<E> callback);

}