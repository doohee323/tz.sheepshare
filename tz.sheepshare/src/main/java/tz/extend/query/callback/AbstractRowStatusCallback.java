package tz.extend.query.callback;

import tz.basis.data.RowStatusCallback;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : TZ
 * 프로그램 :
 * 설    명 : GridData의 일괄 처리 연산 수행에서 개별 연산 수행을 추상화한 추상클래스. 일반적으로 Service 클래스 내부에서 구현하여
 *         사용하며, 수행 중 개별 연산 수행을 정의하고 싶은 경우에 확장하여 사용한다.
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
 *
 * @param <E> generic type class
 */
public abstract class AbstractRowStatusCallback<E> implements RowStatusCallback<E> {

    /**
     * <pre>
     * 개별 데이터 (ROWSTATUS = 'NORMAL')를 기준으로 수행되는 연산을 정의한다.
     * </pre>
     *
     * @param record 개별  Row 데이터
     * @param rowNum row number
     */
    public void normal(E record, int rowNum){

    }

    /**
     * <pre>
     * 개별 데이터 (ROWSTATUS = 'INSERT')를 기준으로 수행되는 연산을 정의한다.
     * </pre>
     *
     * @param record 개별  Row 데이터
     * @param rowNum row number
     */
    public void insert(E record, int rowNum){

    }

    /**
     * <pre>
     * 개별 데이터 (ROWSTATUS = 'UPDATE')를 기준으로 수행되는 연산을 정의한다.
     * </pre>
     *
     * @param newRecord 수정된 데이터
     * @param oldRecord 수정전 데이터
     * @param rowNum row number
     */
    public void update(E newRecord, E oldRecord, int rowNum){

    }

    /**
     * <pre>
     * 개별 데이터 (ROWSTATUS = 'DELETE')를 기준으로 수행되는 연산을 정의한다.
     * </pre>
     *
     * @param record 개별  Row 데이터
     * @param rowNum row number
     */
    public void delete(E record, int rowNum){

    }

}
