package tz.common.metaData.service.support;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : DataSourcePool
 * 설    명 : DataSource을 Pool로 관리하도록 지원
 * 작 성 자 : TZ
 * 작성일자 :
 * 수정이력
 * ---------------------------------------------------------------
 * 수정일          이  름    사유
 * ---------------------------------------------------------------
 *
 * ---------------------------------------------------------------
 * </pre>
 * @version 1.0
 */
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tz.extend.util.StringUtil;

public class DataSourcePool implements Observer {

    private static Logger logger = LoggerFactory.getLogger(DataSourcePool.class);

    /**
     * DataSourcePool의 싱글턴 객체
     */
    private static DataSourcePool singleton = null;

    /**
     * 사용하는 jdbcDataSource 를 풀링하는 HashMap 변수
     */
    private HashMap jdbcDataSourcePool = null;

    /**
     * 사용하는 jndiDataSource 를 풀링하는 HashMap 변수
     */
    private HashMap jndiDataSourcePool = null;

    /**
     * Default Constructor
     */
    private DataSourcePool() {
        super();
        jdbcDataSourcePool = new HashMap();
        jndiDataSourcePool = new HashMap();
    }

    /**
     * 환경파일에서 지정한 Default Connection Type의 Spec 에
     * 대한 커넥션 반환.
     *
     * @param Spec 데이터소스 스펙
     * @return Connection Spec에 해당하는 Connection
     * @throws Exception connection 획득 error가 발생할 경우.
     */
    public Connection getConnection(Properties appProperties) throws Exception {
        String spec = StringUtil.getText(appProperties.get("spec"));
        return getConnection(spec, appProperties);
    }

    public Connection getConnection(String spec, Properties appProperties) throws Exception {
        String jndiName = appProperties.getProperty("com.tz." + spec + ".database.jndiName");
        String conType = "";
        if (!StringUtil.getText(jndiName).equals("")) conType = "jndi";
        if (conType.equals("jndi")) return getJNDIConnection(spec, appProperties);
        else return getJDBCConnection(spec, appProperties);
    }

    /**
     * Connection Type의 Spec 에 대한 커넥션 반환.
     *
     * @param  spec 데이터소스 스펙
     * @return spec에 해당하는 Connection
     * @throws SQLException connection 획득 error가 발생할 경우.
     */
    public Connection getJDBCConnection(String spec, Properties appProperties) throws SQLException{
        JdbcDataSource simpleDataSource = null;
        synchronized(this){
            simpleDataSource = (JdbcDataSource)jdbcDataSourcePool.get(spec);
            if(simpleDataSource == null){
                JdbcDataSource ds = new JdbcDataSource(spec, appProperties);
                jdbcDataSourcePool.put(spec, ds);
                simpleDataSource = ds;
            }
        }
        return simpleDataSource.getConnection();
    }

    /**
     * JNDI Type의 Default Spec 에 대한 커넥션 반환.
     *
     * @param spec 데이터소스 스펙
     * @return Connection Default Spec의 Connection
     * @throws Exception connection 획득 error가 발생할 경우.
     */
    public Connection getJNDIConnection(String spec, Properties appProperties) throws Exception{
        JndiDataSource dataSource = null;
        synchronized (this) {
            dataSource = (JndiDataSource)jndiDataSourcePool.get(spec);
            if (dataSource == null) {
                JndiDataSource ds = new JndiDataSource(spec, appProperties);
                jndiDataSourcePool.put(spec, ds);
                dataSource = ds;
            }
        }
        return dataSource.getConnection();
    }

    /**
     * Connection Type의 Spec 에 대한 커넥션 반환.
     *
     * @param  spec 데이터소스 스펙
     * @return spec에 해당하는 Connection
     * @throws SQLException connection 획득 error가 발생할 경우.
     */
    public void freeJDBCConnection(Connection conn, String spec) throws SQLException{
        JdbcDataSource simpleDataSource = null;
        synchronized(this){
            simpleDataSource = (JdbcDataSource)jdbcDataSourcePool.get(spec);
            if(simpleDataSource == null){
                throw new SQLException("JDBC DataSource is not exists");
            }
            simpleDataSource.freeConnection(conn);
        }
    }

    /**
     * 싱글턴 대상 객체가 존재하지 않을시 그룹화 한다.
     *
     * @return DataSourcePool의 싱글턴 객체
     */
    public static synchronized DataSourcePool getGroup(){
        if(singleton == null){
            singleton = new DataSourcePool();
        }
        return singleton;
    }

    /**
     * LFileManager class 초기화를 위해 refresh()를 호출한다.
     * @param o the observable object.
     * @param arg notifyObservers method에 전달되어지는 argument.
     */
    public void update(Observable o, Object arg){
        if(singleton != null){
            singleton = new DataSourcePool();
            logger.debug("[DataSourcePool.update()] : Update notifyed.");
        }
    }
}
