package tz.common.metaData.service.support;

/**
 * <pre>
 * ---------------------------------------------------------------
 * 업무구분 : 공통
 * 프로그램 : JndiDataSource
 * 설    명 : WAS에서 제공하는 Data Source의 Connection을 지원
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
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.xa.XAResource;

import tz.extend.util.StringUtil;

public class JndiDataSource {

    private Object generalDataSource = null;

    private String contextFactory = null;

    private String providerUrl = null;

    private String jndiName = null;

    private String jndiUsername = null;

    private String jndiPassword = null;

    private String jndiDriver = null;

    private XAResource xaResource = null;

    /**
     * configuration 화일에 설정된 Data Source URL과 사용자 id/password를 읽어온다.
     *
     * @throws SQLException configuration 화일에 해당정보가 없을 경우 발생.
     */
    private void initialize(String spec, Properties appProperties){
        jndiUsername = appProperties.getProperty("com.tz." + spec + ".database.user");
        jndiPassword = appProperties.getProperty("com.tz." + spec + ".database.password");
        jndiDriver = appProperties.getProperty("com.tz." + spec + ".database.driverClass");

        jndiName = appProperties.getProperty("com.tz." + spec + ".database.jndiName");
        contextFactory = appProperties.getProperty("com.tz." + spec + ".database.contextFactory");
        providerUrl = appProperties.getProperty("com.tz." + spec + ".database.providerUrl");
    }

    /**
     * Data Source Spec 식별자에 해당하는 제품공급업체의 context와 url정보를 configuration 화일에서
     * 읽어와서 해당 서버의 context를 생성한 후 Data Source를 look up한다.
     *
     * @param dbSpec 해당 Data Source Spec 식별자
     * @throws Exception
     */
    public JndiDataSource(String dbSpec, Properties appProperties) throws Exception {
        super();
        Properties props = null;
        InitialContext ctx = null;

        if(generalDataSource == null){
            try{
                this.initialize(dbSpec, appProperties);

                if(!StringUtil.getText(this.contextFactory).equals("") && !StringUtil.getText(this.providerUrl).equals("")){
                    props = new Properties();
                    props.put(Context.INITIAL_CONTEXT_FACTORY, this.contextFactory);
                    props.put(Context.PROVIDER_URL, this.providerUrl);
                    ctx = new InitialContext(props); // obtain initial
                }else{
                    ctx = new InitialContext();
                }
                generalDataSource = ctx.lookup(this.jndiName);
            }catch(Exception de){
                System.out.println(de);
            }finally{
                try{
                    if(ctx != null){
                        ctx.close();
                    }
                }catch(NamingException ne){
                    System.out.println(ne);
                }
            }
        }
    }

    /**
     * configuration 화일에 설정된 사용자 id와 password로 해당 DB의 Data Source에서 connection을
     * 가져온다.
     *
     * @return 가용한 Connection을 리턴
     * @throws Exception Connection을 얻지못하는 경우 발생.
     */
    public Connection getConnection() throws Exception{
        Connection conn = null;
        try{
            if(this.jndiUsername == null || this.jndiPassword == null){
                if(this.jndiDriver == null || "datasource".equals(this.jndiDriver)){
                    conn = ((DataSource)this.generalDataSource).getConnection();
                }else if("xadatasource".equals(this.jndiDriver)){
                    xaResource = ((XADataSource)this.generalDataSource).getXAConnection().getXAResource();
                    conn = ((XADataSource)this.generalDataSource).getXAConnection().getConnection();
                }else{
                    System.out.println("getConnection()");
                }
            }else{
                if(this.jndiDriver == null || "datasource".equals(this.jndiDriver) || "com.p6spy.engine.spy.P6SpyDriver".equals(this.jndiDriver)){
                    conn = ((DataSource)this.generalDataSource).getConnection(this.jndiUsername, this.jndiPassword);
                }else if("xadatasource".equals(this.jndiDriver)){
                    xaResource = ((XADataSource)this.generalDataSource).getXAConnection(this.jndiUsername,
                            this.jndiPassword).getXAResource();
                    conn = ((XADataSource)this.generalDataSource).getXAConnection(this.jndiUsername, this.jndiPassword)
                            .getConnection();
                }else{
                    System.out.println("getConnection()");
                }
            }
        }catch(SQLException se){
            System.out.println(se);
        }
        return conn;
    }

    public XAResource getXAResource(){
        return this.xaResource;
    }
}
