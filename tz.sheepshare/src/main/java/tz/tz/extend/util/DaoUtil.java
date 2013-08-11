package tz.extend.util;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : DaoUtil
 * 설      명 : DAO의 트랜잭션 처리 설정을 위한 Utility Class
 * 작 성 자 : TZ
 * 작성일자 : 2013-12-05
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 * 2013-12-07             최초 작성
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
public class DaoUtil {

    /**
     * job type에 따른 트랜잭션 처리시 저장되는 Key
     */
    public final static String TZ_CUD_FILTER_KEY = "TZ_CUD_FILTER_KEY";

    /**
     * job type에 따른 트랜잭션 처리시 저장되는 insert 쿼리를 위한 값
     */
    public final static String TZ_CREATE_KEY = "TZ_CREATE_FILTER_VALUE";

    /**
     * job type에 따른 트랜잭션 처리시 저장되는 update 쿼리를 위한 값
     */
    public final static String TZ_UPDATE_KEY = "TZ_UPDATE_FILTER_VALUE";

    /**
     * job type에 따른 트랜잭션 처리시 저장되는 delete 쿼리를 위한 값
     */
    public final static String TZ_DELETE_KEY = "TZ_DELETE_FILTER_VALUE";

}
