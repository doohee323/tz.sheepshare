package tz.common.metaData.service.support;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 :
 * 프로그램 : JdbcDataSource
 * 설    명 : Jdbc DataSource를 지원
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
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcDataSource implements DataSource {

    private static Logger logger = LoggerFactory.getLogger(JdbcDataSource.class);

    /**
     * JdbcDataSource Spec의 Connection Type 변수
     */
    private int expectedConnectionTypeCode;

    /**
     * JdbcDataSource의 Pool 정보를 Synchronize하기위한 Object 변수
     */
    private static final Object POOL_LOCKER = new Object();

    /**
     * JdbcDataSource의 비활성화된 커넥션을 관리하는 List 객체
     */
    private List idleConnections = new ArrayList();

    /**
     * JdbcDataSource의 활성화된 커넥션을 관리하는 List 객체
     */
    private List activeConnections = new ArrayList();

    /**
     * JdbcDataSource에 의해 요청된 커넥션의 수
     */
    private long requestCount = 0;

    /**
     * JdbcDataSource에 의해 요청에 걸린 누적 시간
     */
    private long accumulatedRequestTime = 0;

    /**
     * JdbcDataSource에 의해 커넥션 반환에 걸린 누적 시간
     */
    private long accumulatedCheckoutTime = 0;

    /**
     * JdbcDataSource에서 사이즈 초과된 요청 카운트
     */
    private long claimedOverdueConnectionCount = 0;

    /**
     * JdbcDataSource에서 사이즈 초과된 요청에의한 커넥션들이 반환된 누적 시간
     */
    private long accumulatedCheckoutTimeOfOverdueConnections = 0;

    /**
     * JdbcDataSource에 Wait걸린 커넥션 요청에 대한 누적 시간
     */
    private long accumulatedWaitTime = 0;

    /**
     * JdbcDataSource에 대기중인 요청 카운트
     */
    private long hadToWaitCount = 0;

    /**
     * JdbcDataSource에 불량 커넥션 카운트
     */
    private long badConnectionCount = 0;

    // JDBC 접속정보
    /**
     * JdbcDataSource에 등록된 JDBC Driver 클래스명
     */
    private String jdbcDriver;

    /**
     * JdbcDataSource에 등록된 JDBC 접속 URL
     */
    private String jdbcUrl;

    /**
     * JdbcDataSource에 등록된 JDBC 접속 USERID
     */
    private String jdbcUsername;

    /**
     * JdbcDataSource에 등록된 JDBC 접속 PASSWORD
     */
    private String jdbcPassword;

    // 환경파일에 등록된 설정값 변수
    /**
     * 환경파일에 등록된 PoolMaximumActiveConnections 항목변수
     */
    private int poolMaximumActiveConnections;

    /**
     * 환경파일에 등록된 PoolMaximumIdleConnections 항목변수
     */
    private int poolMaximumIdleConnections;

    /**
     * 환경파일에 등록된 PoolMaximumCheckoutTime 항목변수
     */
    private int poolMaximumCheckoutTime;

    /**
     * 환경파일에 등록된 PoolTimeToWait 항목변수
     */
    private int poolTimeToWait;

    /**
     * 환경파일에 등록된 PoolPingQuery 항목변수
     */
    private String poolPingQuery;

    /**
     * 환경파일에 등록된 PoolQuietMode 항목변수
     */
    private boolean poolQuietMode;

    /**
     * 환경파일에 등록된 PoolPingEnabled 항목변수
     */
    private boolean poolPingEnabled;

    /**
     * 환경파일에 등록된 PoolPingConnectionsOlderThan 항목변수
     */
    private int poolPingConnectionsOlderThan;

    /**
     * 환경파일에 등록된 PoolPingConnectionsNotUsedFor 항목변수
     */
    private int poolPingConnectionsNotUsedFor;

    /**
     * Constructor with Spec name
     */
    public JdbcDataSource(String spec, Properties appProperties) {
        initialize(spec, appProperties);
    }

    /**
     * dbSpec에 해당하는 property 정보를 로드하여 커넥션 풀을 생성한다.
     *
     * @param dbSpec Database 스펙명
     */
    private void initialize(String spec, Properties appProperties){
        try{
            jdbcDriver = appProperties.getProperty("com.tz." + spec + ".database.driverClass");
            jdbcUrl = appProperties.getProperty("com.tz." + spec + ".database.jdbcUrl");
            jdbcUsername = appProperties.getProperty("com.tz." + spec + ".database.user");
            jdbcPassword = appProperties.getProperty("com.tz." + spec + ".database.password");

            poolMaximumActiveConnections = 100; //appProperties.getProperty("");
            poolMaximumIdleConnections = 5; //appProperties.getProperty("");
            poolMaximumCheckoutTime = 20000; //appProperties.getProperty("");
            poolTimeToWait = 15000; //appProperties.getProperty("");
            poolPingEnabled = false; //appProperties.getProperty("");
            poolPingQuery = "No Ping QUERY SET"; //appProperties.getProperty("");
            poolPingConnectionsOlderThan = 0; //appProperties.getProperty("");
            poolPingConnectionsNotUsedFor = 0; //appProperties.getProperty("");
            poolQuietMode = true; //appProperties.getProperty("");
            expectedConnectionTypeCode = assembleConnectionTypeCode(jdbcUrl, jdbcUsername, jdbcPassword);

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            classLoader.loadClass(jdbcDriver).newInstance();
        }catch(Exception e){
            throw new RuntimeException("[JdbcDataSource] Error while loading properties. Cause: " + e.toString());
        }
    }

    /**
     * 접속정보를 조합한 hash 값을 반환한다.
     *
     * @param url 접속주소
     * @param username 사용자명
     * @param password 패스워드
     * @return 접속정보를 조합한 hash 값
     */
    private int assembleConnectionTypeCode(String url, String username, String password){
        return ("" + url + username + password).hashCode();
    }

    /**
     * Connection을 반환한다.
     *
     * @return Connection - 커넥션
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException{

        return popConnection(jdbcUsername, jdbcPassword);
    }

    /**
     * username에 해당하는 Connection을 반환한다.
     *
     * @param username - 사용자명
     * @param password - 패스워드
     * @return Connection - 커넥션
     * @throws SQLException
     */
    public Connection getConnection(String username, String password) throws SQLException{
        return popConnection(username, password);
    }

    /**
     * Connection을 반환한다.
     *
     * @param conn - 반환할 Connection
     * @throws SQLException
     */
    public void freeConnection(Connection conn) throws SQLException{
        this.pushConnection(conn);
    }

    /**
     * DB에 접속대기할 최대 시간(초)을 설정한다.
     *
     * @param loginTimeout - 접속대기할 최대 시간(초)
     * @throws SQLException
     */
    public void setLoginTimeout(int loginTimeout) throws SQLException{
        DriverManager.setLoginTimeout(loginTimeout);
    }

    /**
     * DB에 접속대기할 최대 시간(초)을 반환한다.
     *
     * @return int - 접속대기할 최대 시간(초)
     * @throws SQLException
     */
    public int getLoginTimeout() throws SQLException{
        return DriverManager.getLoginTimeout();
    }

    /**
     * DriverManager에서 로그/트레이싱에 사용할 PrintWriter object 를 설정한다.
     *
     * @param logWriter - DB정보를 Trace 로깅할 Writer
     * @throws SQLException
     */
    public void setLogWriter(PrintWriter logWriter) throws SQLException{
        DriverManager.setLogWriter(logWriter);
    }

    /**
     * DriverManager에서 로그/트레이싱에 설정된 PrintWriter object 를 반환한다.
     *
     * @return PrintWriter - DB정보를 Trace 로깅할 Writer
     * @throws SQLException
     */
    public PrintWriter getLogWriter() throws SQLException{
        return DriverManager.getLogWriter();
    }

    /**
     * 사용되지 않고있는 커넥션수를 반환한다.
     *
     * @return int - 사용되지 않고있는 커넥션수
     */
    public int getPoolPingConnectionsNotUsedFor(){
        return poolPingConnectionsNotUsedFor;
    }

    /**
     * JDBC Driver 클래스명을 반환한다.
     *
     * @return String - JDBC Driver 클래스명
     */
    public String getJdbcDriver(){
        return jdbcDriver;
    }

    /**
     * DB 접속 URL을 반환한다.
     *
     * @return String - DB URL
     */
    public String getJdbcUrl(){
        return jdbcUrl;
    }

    /**
     * DB접속자 아이디를 반환한다.
     *
     * @return String - 접속 아이디
     */
    public String getJdbcUsername(){
        return jdbcUsername;
    }

    /**
     * DB접속 패스워드를 반환한다.
     *
     * @return String - 접속 패스워드
     */
    public String getJdbcPassword(){
        return jdbcPassword;
    }

    /**
     * 최대 활성 커넥션 수를 반환한다.
     *
     * @return int - 최대 활성 커넥션 수
     */
    public int getPoolMaximumActiveConnections(){
        return poolMaximumActiveConnections;
    }

    /**
     * 최대 비활성 커넥션 수를 반환한다.
     *
     * @return int - 최대 비활성 커넥션 수
     */
    public int getPoolMaximumIdleConnections(){
        return poolMaximumIdleConnections;
    }

    /**
     * 최대 커넥션 반환 시간을 반환한다.
     *
     * @return int - 최대 커넥션 반환 시간
     */
    public int getPoolMaximumCheckoutTime(){
        return poolMaximumCheckoutTime;
    }

    /**
     * 접속 wait 시 대기시간을 반환한다.
     *
     * @return int - 접속 wait 시 대기시간
     */
    public int getPoolTimeToWait(){
        return poolTimeToWait;
    }

    /**
     * ping test용 쿼리를 반환한다.
     *
     * @return String - ping test용 쿼리
     */
    public String getPoolPingQuery(){
        return poolPingQuery;
    }

    /**
     * ping test가 가능한지 여부를 반환한다.
     *
     * @return boolean - ping test 가능여부 boolean
     */
    public boolean isPoolPingEnabled(){
        return poolPingEnabled;
    }

    /**
     * Connection 생성시 old 커넥션으로 인식할 시간을 반환한다.
     *
     * @return int - Connection 생성시 old 커넥션으로 인식할 시간
     */
    public int getPoolPingConnectionsOlderThan(){
        return poolPingConnectionsOlderThan;
    }

    /**
     * Trace 모드 여부를 반환한다.
     *
     * @return boolean - Trace 모드 여부
     */
    public boolean isPoolQuietMode(){
        return poolQuietMode;
    }

    /**
     * Trace 모드 시 해당 Object를 Logging한다.
     *
     * @param o - 로깅대상
     */
    private void log(Object o){
        if(!isPoolQuietMode()){
            logger.debug(String.valueOf(o));
        }
    }

    /**
     * Logging 여부 설정
     *
     * @param poolQuietMode - Logging 여부
     */
    public void setPoolQuietMode(boolean poolQuietMode){
        this.poolQuietMode = poolQuietMode;
    }

    /**
     * 예상되는 Connection Type을 반환한다.
     *
     * @return int - 예상되는 Connection Type
     */
    private int getExpectedConnectionTypeCode(){
        return expectedConnectionTypeCode;
    }

    /**
     * 커넥션 요청 카운트수를 반환한다.
     *
     * @return long - 커넥션 요청 카운트수
     */
    public long getRequestCount(){
        synchronized(POOL_LOCKER){
            return requestCount;
        }
    }

    /**
     * 커넥션 요청에서 실제 반환에 이르기까지의 평균시간을 반환한다.
     *
     * @return long - 커넥션 반환 평균시간
     */
    public long getAverageRequestTime(){
        synchronized(POOL_LOCKER){
            return requestCount == 0 ? 0 : accumulatedRequestTime / requestCount;
        }
    }

    /**
     * 평균 대기시간을 반환한다.
     *
     * @return long - 평균 대기시간
     */
    public long getAverageWaitTime(){
        synchronized(POOL_LOCKER){
            return hadToWaitCount == 0 ? 0 : accumulatedWaitTime / hadToWaitCount;
        }
    }

    /**
     * Wait 걸린 카운트를 반환한다.
     *
     * @return long - wait 사인 카운트
     */
    public long getHadToWaitCount(){
        synchronized(POOL_LOCKER){
            return hadToWaitCount;
        }
    }

    /**
     * 불량 커넥션의 수를 반환한다.
     *
     * @return long - 불량 커넥션 수
     */
    public long getBadConnectionCount(){
        synchronized(POOL_LOCKER){
            return badConnectionCount;
        }
    }

    /**
     * Maximum 카운터를 넘어간 경우 DB에 반환되지 않은 커넥션의 수를 반환한다.
     *
     * @return long - 반환되지 않는 커넥션
     */
    public long getClaimedOverdueConnectionCount(){
        synchronized(POOL_LOCKER){
            return claimedOverdueConnectionCount;
        }
    }

    /**
     * Maximum 카운터를 넘어간 경우 DB에 반환되지 않은 커넥션의 평균 체크아웃 시간을 반환한다.
     *
     * @return long - 반환되지 않는 커넥션들의 평균 체크아웃 타임
     */
    public long getAverageOverdueCheckoutTime(){
        synchronized(POOL_LOCKER){
            return claimedOverdueConnectionCount == 0 ? 0 : accumulatedCheckoutTimeOfOverdueConnections
                    / claimedOverdueConnectionCount;
        }
    }

    /**
     * 평균체크아웃 시간을 반환한다.
     *
     * @return long - 평균 체크아웃 시간
     */
    public long getAverageCheckoutTime(){
        synchronized(POOL_LOCKER){
            return requestCount == 0 ? 0 : accumulatedCheckoutTime / requestCount;
        }
    }

    /**
     * 현재 풀의 프러퍼티 및 통계정보를 반환한다.
     *
     * @return String - 프러퍼티 및 통계정보
     */
    public String getStatus(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("\r\n ========================================================================");
        buffer.append("\r\n [jdbc_Driver]                       :" + jdbcDriver);
        buffer.append("\r\n [jdbc_Url]                          :" + jdbcUrl);
        buffer.append("\r\n [jdbc_Username]                     :" + jdbcUsername);
        buffer.append("\r\n [jdbc_Password]                     :" + (jdbcPassword == null ? "NULL" : "****"));
        buffer.append("\r\n [pool_Max_Active_Connections]       :" + poolMaximumActiveConnections);
        buffer.append("\r\n [pool_Max_Idle_Connections]         :" + poolMaximumIdleConnections);
        buffer.append("\r\n [pool_Max_Checkout_Time]            :" + poolMaximumCheckoutTime);
        buffer.append("\r\n [pool_Time_To_Wait]                 :" + poolTimeToWait);
        buffer.append("\r\n [pool_Quiet_Mode]                   :" + poolQuietMode);
        buffer.append("\r\n [pool_Ping_Enabled]                 :" + poolPingEnabled);
        buffer.append("\r\n [pool_Ping_Query]                   :" + poolPingQuery);
        buffer.append("\r\n [pool_Ping_Connections_Older_Than]  :" + poolPingConnectionsOlderThan);
        buffer.append("\r\n [pool_Ping_Connections_Not_Used For]:" + poolPingConnectionsNotUsedFor);
        buffer.append("\r\n ------------------------------------------------------------------------");
        buffer.append("\r\n [active_Connections]                :" + activeConnections.size());
        buffer.append("\r\n [idle_Connections]                  :" + idleConnections.size());
        buffer.append("\r\n [request_Count]                     :" + getRequestCount());
        buffer.append("\r\n [averageRequest_Time]               :" + getAverageRequestTime());
        buffer.append("\r\n [averageCheckout_Time]              :" + getAverageCheckoutTime());
        buffer.append("\r\n [claimed_Overdue                    :" + getClaimedOverdueConnectionCount());
        buffer.append("\r\n [average_Overdue_CheckoutTime]      :" + getAverageOverdueCheckoutTime());
        buffer.append("\r\n [had_To_Wait]                       :" + getHadToWaitCount());
        buffer.append("\r\n [average_Wait_Time]                 :" + getAverageWaitTime());
        buffer.append("\r\n [bad_Connection_Count]              :" + getBadConnectionCount());
        buffer.append("\r\n =======================================================================");
        String rtnStr = buffer.toString();
        return rtnStr;
    }

    /**
     * 활성화된 커넥션이든, 비활성화된 커넥션이든 모두 종료한다.
     *
     * @throws SQLException
     */
    public void forceCloseAll() throws SQLException{
        synchronized(POOL_LOCKER){
            final int activeConnectionSize = activeConnections.size();
            for(int inx = 0; inx < activeConnectionSize; inx++){
                Connection conn = null;
                try{
                    PooledJDBCConnection pooledJDBCConnection = (PooledJDBCConnection)activeConnections.get(inx);
                    conn = pooledJDBCConnection.getRealConnection();
                    pooledJDBCConnection.invalidate();
                    if(!conn.getAutoCommit()){
                        conn.rollback();
                    }
                }catch(Exception e){
                    logger.debug("Exception occurred while Connection rollback" + " : " + e);
                }finally{
                    try{
                        if(conn != null && !conn.isClosed()){
                            conn.close();
                        }
                    }catch(Exception e){
                        logger.debug("Exception occurred while Connection close" + " : " + e);
                    }
                }
            }
            activeConnections.clear();
            final int idleConnectionSize = idleConnections.size();
            for(int inx = 0; inx < idleConnectionSize; inx++){
                Connection conn = null;
                try{
                    PooledJDBCConnection pooledJDBCConnection = (PooledJDBCConnection)idleConnections.get(inx);
                    conn = pooledJDBCConnection.getRealConnection();
                    pooledJDBCConnection.invalidate();
                    if(!conn.getAutoCommit()){
                        conn.rollback();
                    }
                }catch(Exception e){
                    logger.debug("Exception occurred while Connection rollback" + " : " + e);
                }finally{
                    try{
                        if(conn != null && !conn.isClosed()){
                            conn.close();
                        }
                    }catch(Exception e){
                        logger.debug("Exception occurred while Connection close" + " : " + e);
                    }
                }
            }
            activeConnections.clear();
        }
        log("[JdbcDataSource] Forcefully closed all connections.");
    }

    /**
     * 사용자에의해 반환된 커넥션이 최대 Idle 사이즈 이내이면 풀에 저장하고, 그 이상이면 종료한다.
     *
     * @throws SQLException
     */
    private void pushConnection(Connection connection) throws SQLException{
        final PooledJDBCConnection pooledJDBCConnection = (PooledJDBCConnection)connection;
        synchronized(POOL_LOCKER){
            activeConnections.remove(pooledJDBCConnection);
            if(pooledJDBCConnection.isValid()){
                final Connection conn = pooledJDBCConnection.getRealConnection();
                final int idleConnectionSize = idleConnections.size();
                final int connectionTypeCode = pooledJDBCConnection.getConnectionTypeCode();
                if((idleConnectionSize < poolMaximumIdleConnections)
                        && (connectionTypeCode == getExpectedConnectionTypeCode())){
                    accumulatedCheckoutTime += pooledJDBCConnection.getCheckoutTime();
                    if(!conn.getAutoCommit()){
                        conn.rollback();
                    }
                    PooledJDBCConnection neoConn = new PooledJDBCConnection(conn, this);
                    idleConnections.add(neoConn);
                    neoConn.setCreatedTimestamp(pooledJDBCConnection.getCreatedTimestamp());
                    neoConn.setLastUsedTimestamp(pooledJDBCConnection.getLastUsedTimestamp());
                    pooledJDBCConnection.invalidate();
                    log("[JdbcDataSource] Returned connection " + neoConn.getRealHashCode() + " to pool.");
                    POOL_LOCKER.notifyAll();
                }else{
                    accumulatedCheckoutTime += pooledJDBCConnection.getCheckoutTime();
                    try{
                        if(!conn.getAutoCommit()){
                            conn.rollback();
                        }
                    }catch(Exception e){
                        logger.debug("Exception occurred while Connection rollback" + " : " + e);

                    }finally{
                        try{
                            if(conn != null && !conn.isClosed()){
                                conn.close();
                            }
                        }catch(Exception e){
                            logger.debug("Exception occurred while Connection close" + " : " + e);
                        }
                    }
                    log("[JdbcDataSource] Closed connection " + pooledJDBCConnection.getRealHashCode() + ".");
                    pooledJDBCConnection.invalidate();
                }
            }else{
                log("[JdbcDataSource] A bad connection (" + pooledJDBCConnection.getRealHashCode()
                        + ") attempted to return to the pool, discarding connection.");
                badConnectionCount++;
            }
        }
    }

    /**
     * PooledJDBCConnection을 반환한다.
     *
     * @throws SQLException
     */
    private PooledJDBCConnection popConnection(String username, String password) throws SQLException{
        boolean countedWaitFlag = false;
        PooledJDBCConnection pooledJDBCConnection = null;
        final long startTime = System.currentTimeMillis();
        int localBadConnectionCount = 0;

        while(pooledJDBCConnection == null){
            synchronized(POOL_LOCKER){
                final int idleConnectionSize = idleConnections.size();
                if(idleConnectionSize > 0){
                    pooledJDBCConnection = (PooledJDBCConnection)idleConnections.remove(0);
                    log("[JdbcDataSource] Checked out connection " + pooledJDBCConnection.getRealHashCode()
                            + " from pool.");
                }else{

                    final int activeConnectionSize = activeConnections.size();
                    if(activeConnectionSize < poolMaximumActiveConnections){

                        pooledJDBCConnection = new PooledJDBCConnection(DriverManager.getConnection(jdbcUrl, username,
                                password), this);
                        Connection conn = pooledJDBCConnection.getRealConnection();
                        if(conn.getAutoCommit()){
                            conn.setAutoCommit(false);
                        }

                        log("[JdbcDataSource] Created connection " + pooledJDBCConnection.getRealHashCode() + ".");
                    }else{

                        PooledJDBCConnection theOldestActiveConnection = (PooledJDBCConnection)activeConnections.get(0);
                        final long theLongestCheckoutTime = theOldestActiveConnection.getCheckoutTime();
                        if(theLongestCheckoutTime > poolMaximumCheckoutTime){
                            claimedOverdueConnectionCount++;
                            accumulatedCheckoutTimeOfOverdueConnections += theLongestCheckoutTime;
                            accumulatedCheckoutTime += theLongestCheckoutTime;
                            activeConnections.remove(theOldestActiveConnection);

                            Connection conn = theOldestActiveConnection.getRealConnection();
                            if(!conn.getAutoCommit()){
                                conn.rollback();
                            }
                            pooledJDBCConnection = new PooledJDBCConnection(conn, this);
                            theOldestActiveConnection.invalidate();
                            log("[JdbcDataSource] Claimed overdue connection " + pooledJDBCConnection.getRealHashCode()
                                    + ".");
                        }else{
                            try{
                                if(!countedWaitFlag){
                                    hadToWaitCount++;
                                    countedWaitFlag = true;
                                }
                                log("[JdbcDataSource] Waiting as long as " + poolTimeToWait
                                        + " milliseconds for connection.");
                                final long wt = System.currentTimeMillis();
                                POOL_LOCKER.wait(poolTimeToWait);
                                accumulatedWaitTime += System.currentTimeMillis() - wt;
                            }catch(InterruptedException e){
                                break;
                            }
                        }
                    }
                }

                if(pooledJDBCConnection != null && pooledJDBCConnection.isValid()){
                    Connection conn = pooledJDBCConnection.getRealConnection();
                    if(!conn.getAutoCommit()){
                        conn.rollback();
                    }
                    pooledJDBCConnection.setConnectionTypeCode(assembleConnectionTypeCode(jdbcUrl, username, password));
                    pooledJDBCConnection.setCheckoutTimestamp(System.currentTimeMillis());
                    pooledJDBCConnection.setLastUsedTimestamp(System.currentTimeMillis());
                    activeConnections.add(pooledJDBCConnection);
                    requestCount++;
                    accumulatedRequestTime += System.currentTimeMillis() - startTime;
                }else{
                    log("[JdbcDataSource]  JdbcDataSource.popConnection(): A bad connection ("
                            + pooledJDBCConnection.getRealHashCode()
                            + ") was returned from the pool, getting another connection.");
                    badConnectionCount++;
                    localBadConnectionCount++;
                    pooledJDBCConnection = null;
                    if(localBadConnectionCount > (poolMaximumIdleConnections + 3)){
                        log("[JdbcDataSource] Could not get a good connection to the database.");
                        throw new SQLException(
                                "[JdbcDataSource] JdbcDataSource.popConnection(): Could not get a good connection to the database.");
                    }
                }

            }
        }

        if(pooledJDBCConnection == null){
            log("[JdbcDataSource Error] Unknown severe error condition.  The connection pool returned a null connection.");
            throw new SQLException(
                    "[JdbcDataSource Error] Unknown severe error condition.  The connection pool returned a null connection.");
        }

        return pooledJDBCConnection;
    }

    /**
     * PooledJDBCConnection에 대한 ping 테스트를 수행한다. 반환한다.
     *
     * @return 커넥션의 사용가능 여부
     */
    private boolean pingConnection(PooledJDBCConnection conn){
        boolean result = true;

        try{
            result = !conn.getRealConnection().isClosed();
        }catch(SQLException e){
            result = false;
        }

        if(result){
            if(poolPingEnabled){
                if((poolPingConnectionsOlderThan > 0 && conn.getAge() > poolPingConnectionsOlderThan)
                        || (poolPingConnectionsNotUsedFor > 0 && conn.getTimeElapsedSinceLastUse() > poolPingConnectionsNotUsedFor)){

                    Statement statement = null;
                    ResultSet rs = null;
                    try{
                        log("[JdbcDataSource] Testing connection " + conn.getRealHashCode() + "...");
                        statement = conn.createStatement();
                        rs = statement.executeQuery(poolPingQuery);

                        if(!conn.getAutoCommit()){
                            conn.rollback();
                        }

                        result = true;
                        log("[JdbcDataSource] Connection " + conn.getRealHashCode() + " is GOOD!");
                    }catch(Exception e){
                        result = false;
                        log("[JdbcDataSource] Connection " + conn.getRealHashCode() + " is BAD!");
                    }finally{
                        try{
                            if(rs != null){
                                rs.close();
                            }
                        }catch(Exception e2){
                            logger.debug("Exception occurred while ResultSet close" + " : " + e2);
                        }
                        try{
                            if(statement != null){
                                statement.close();
                            }
                        }catch(Exception e2){
                            logger.debug("Exception occurred while Statement close" + " : " + e2);
                        }
                        try{
                            if(conn != null && conn.getRealConnection() != null){
                                conn.getRealConnection().close();
                            }
                        }catch(Exception e2){
                            logger.debug("Exception occurred while Connection close" + " : " + e2);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Pool의 connection 객체가 아닌 real connection 객체를 얻어온다.
     *
     * @param conn
     * @return Connection - real connection
     */
    public static Connection unwrapConnection(Connection conn){
        if(conn instanceof PooledJDBCConnection){
            return ((PooledJDBCConnection)conn).getRealConnection();
        }else{
            return conn;
        }
    }

    /**
     * ---------------------------------------------------------------------------------------
     * PooledJDBCConnection
     * ---------------------------------------------------------------------------------------
     */
    private static class PooledJDBCConnection implements Connection {

        private int hashCode = 0;

        private JdbcDataSource dataSource;

        private Connection realConnection;

        private long checkoutTimestamp;

        private long createdTimestamp;

        private long lastUsedTimestamp;

        private int connectionTypeCode;

        private boolean valid;

        public PooledJDBCConnection(Connection connection, JdbcDataSource dataSource) {
            this.hashCode = connection.hashCode();
            this.realConnection = connection;
            this.dataSource = dataSource;
            this.createdTimestamp = System.currentTimeMillis();
            this.lastUsedTimestamp = System.currentTimeMillis();
            this.valid = true;
        }

        public void invalidate(){
            valid = false;
        }

        public boolean isValid(){
            return valid && realConnection != null && dataSource.pingConnection(this);
        }

        public Connection getRealConnection(){
            return realConnection;
        }

        public int getRealHashCode(){
            if(realConnection == null)
                return 0;
            else
                return realConnection.hashCode();

        }

        public int getConnectionTypeCode(){
            return connectionTypeCode;
        }

        public void setConnectionTypeCode(int connectionTypeCode){
            this.connectionTypeCode = connectionTypeCode;
        }

        public long getCreatedTimestamp(){
            return createdTimestamp;
        }

        public void setCreatedTimestamp(long createdTimestamp){
            this.createdTimestamp = createdTimestamp;
        }

        public long getLastUsedTimestamp(){
            return lastUsedTimestamp;
        }

        public void setLastUsedTimestamp(long lastUsedTimestamp){
            this.lastUsedTimestamp = lastUsedTimestamp;
        }

        public long getTimeElapsedSinceLastUse(){
            return System.currentTimeMillis() - lastUsedTimestamp;
        }

        public long getAge(){
            return System.currentTimeMillis() - createdTimestamp;
        }

        public long getCheckoutTimestamp(){
            return checkoutTimestamp;
        }

        public void setCheckoutTimestamp(long timestamp){
            this.checkoutTimestamp = timestamp;
        }

        public long getCheckoutTime(){
            return System.currentTimeMillis() - checkoutTimestamp;
        }

        private Connection getValidConnection(){
            if(!valid){
                throw new RuntimeException(
                        "[PooledJDBCConnection Error] Connection has been invalidated (probably released back to the pool).");
            }
            return realConnection;
        }

        // **********************************
        // Implemented Connection Methods
        // **********************************

        public Statement createStatement() throws SQLException{
            final Connection conn = getValidConnection();
            Statement stmt = conn.createStatement();
            return stmt;
        }

        public PreparedStatement prepareStatement(String sql) throws SQLException{
            final Connection conn = getValidConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            return stmt;
        }

        public CallableStatement prepareCall(String sql) throws SQLException{
            final Connection conn = getValidConnection();
            CallableStatement cstmt = conn.prepareCall(sql);
            return cstmt;
        }

        public String nativeSQL(String sql) throws SQLException{
            final Connection conn = getValidConnection();
            return conn.nativeSQL(sql);
        }

        public void setAutoCommit(boolean autoCommit) throws SQLException{
            final Connection conn = getValidConnection();
            conn.setAutoCommit(autoCommit);
        }

        public boolean getAutoCommit() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.getAutoCommit();
        }

        public void commit() throws SQLException{
            final Connection conn = getValidConnection();
            conn.commit();
        }

        public void rollback() throws SQLException{
            final Connection conn = getValidConnection();
            conn.rollback();
        }

        public void close() throws SQLException{
            this.dataSource.pushConnection(this);
        }

        public boolean isClosed() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.isClosed();
        }

        public DatabaseMetaData getMetaData() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.getMetaData();
        }

        public void setReadOnly(boolean readOnly) throws SQLException{
            final Connection conn = getValidConnection();
            conn.setReadOnly(readOnly);
        }

        public boolean isReadOnly() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.isReadOnly();
        }

        public void setCatalog(String catalog) throws SQLException{
            final Connection conn = getValidConnection();
            conn.setCatalog(catalog);
        }

        public String getCatalog() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.getCatalog();
        }

        public void setTransactionIsolation(int level) throws SQLException{
            final Connection conn = getValidConnection();
            conn.setTransactionIsolation(level);
        }

        public int getTransactionIsolation() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.getTransactionIsolation();
        }

        public SQLWarning getWarnings() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.getWarnings();
        }

        public void clearWarnings() throws SQLException{
            final Connection conn = getValidConnection();
            conn.clearWarnings();
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException{
            final Connection conn = getValidConnection();
            final Statement stmt = conn.createStatement(resultSetType, resultSetConcurrency);
            return stmt;
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException{
            final Connection conn = getValidConnection();
            final PreparedStatement stmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency);
            return stmt;
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency)
                throws SQLException{
            final Connection conn = getValidConnection();
            final CallableStatement cstmt = conn.prepareCall(sql, resultSetType, resultSetConcurrency);
            return cstmt;
        }

        public Map getTypeMap() throws SQLException{
            return getValidConnection().getTypeMap();
        }

        public void setTypeMap(Map map) throws SQLException{
            getValidConnection().setTypeMap(map);
        }

        public int hashCode(){
            return hashCode;
        }

        public boolean equals(Object obj){
            if(obj instanceof PooledJDBCConnection){
                return realConnection.hashCode() == (((PooledJDBCConnection)obj).realConnection.hashCode());
            }else if(obj instanceof Connection){
                return hashCode == obj.hashCode();
            }else{
                return false;
            }
        }

        // **********************************
        // JDK 1.4 JDBC 3.0 Methods below
        // **********************************

        public void setHoldability(int holdability) throws SQLException{
            final Connection conn = getValidConnection();
            conn.setHoldability(holdability);
        }

        public int getHoldability() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.getHoldability();
        }

        public Savepoint setSavepoint() throws SQLException{
            final Connection conn = getValidConnection();
            return conn.setSavepoint();
        }

        public Savepoint setSavepoint(String name) throws SQLException{
            final Connection conn = getValidConnection();
            return conn.setSavepoint(name);
        }

        public void rollback(Savepoint savepoint) throws SQLException{
            final Connection conn = getValidConnection();
            conn.rollback(savepoint);
        }

        public void releaseSavepoint(Savepoint savepoint) throws SQLException{
            final Connection conn = getValidConnection();
            conn.releaseSavepoint(savepoint);
        }

        public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
                throws SQLException{
            final Connection conn = getValidConnection();
            final Statement stmt = conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
            return stmt;
        }

        public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                int resultSetHoldability) throws SQLException{
            final Connection conn = getValidConnection();
            final PreparedStatement stmt = conn.prepareStatement(sql, resultSetType, resultSetConcurrency,
                    resultSetHoldability);
            return stmt;
        }

        public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                int resultSetHoldability) throws SQLException{
            final Connection conn = getValidConnection();
            final CallableStatement cstmt = conn.prepareCall(sql, resultSetType, resultSetConcurrency,
                    resultSetHoldability);
            return cstmt;
        }

        public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException{
            final Connection conn = getValidConnection();
            final PreparedStatement stmt = conn.prepareStatement(sql, autoGeneratedKeys);
            return stmt;
        }

        public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException{
            final Connection conn = getValidConnection();
            final PreparedStatement stmt = conn.prepareStatement(sql, columnIndexes);
            return stmt;
        }

        public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException{
            final Connection conn = getValidConnection();
            final PreparedStatement stmt = conn.prepareStatement(sql, columnNames);
            return stmt;
        }

        public Array createArrayOf(String typeName, Object[] elements) throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public Blob createBlob() throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public Clob createClob() throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public NClob createNClob() throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public SQLXML createSQLXML() throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public Struct createStruct(String typeName, Object[] attributes) throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public Properties getClientInfo() throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public String getClientInfo(String name) throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isValid(int timeout) throws SQLException{
            // TODO Auto-generated method stub
            return false;
        }

        public void setClientInfo(Properties properties) throws SQLClientInfoException{
            // TODO Auto-generated method stub

        }

        public void setClientInfo(String name, String value) throws SQLClientInfoException{
            // TODO Auto-generated method stub

        }

        public boolean isWrapperFor(Class<?> iface) throws SQLException{
            // TODO Auto-generated method stub
            return false;
        }

        public <T> T unwrap(Class<T> iface) throws SQLException{
            // TODO Auto-generated method stub
            return null;
        }

		@Override
		public void setSchema(String schema) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public String getSchema() throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void abort(Executor executor) throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void setNetworkTimeout(Executor executor, int milliseconds)
				throws SQLException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public int getNetworkTimeout() throws SQLException {
			// TODO Auto-generated method stub
			return 0;
		}
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException{
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException{
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public java.util.logging.Logger getParentLogger()
			throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
