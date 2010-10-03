package core.log.jdbc;

import core.log.impl.ConnectionLoggable;
import core.log.logger.SL;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 10. 22 ���� 1:42:30
 * Content  :
 * Usage    :
 */
public abstract class DriverLoggables implements Driver {
    private String banderDriver;
    private Driver driver=null;
    public final String ORACLE_DRIVER="oracle.jdbc.driver.OracleDriver";
    public final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
    public final String SYBASE_DRIVER="com.sybase.jdbc2.jdbc.SybDriver";
    public final String DB2_DRIVER="com.ibm.db2.jcc.DB2Driver";
    public final String INFOMIX_DRIVER="com.informix.jdbc.IfxDriver";
    public final String POSTGRESQL_DRIVER="org.postgresql.Driver";
    public final String MAXDB_DRIVER="com.sap.dbtech.jdbc.DriverSapDB";
    public final String FRONTBASE_DRIVER="com.frontbase.jdbc.FBJDriver";
    public final String HSQL_DRIVER="org.hsqldb.jdbcDriver";
    public final String POINTBASE_DRIVER="com.pointbase.jdbc.jdbcUniversalDriver";
    public final String MIMER_DRIVER="com.mimer.jdbc.Driver";
    public final String PERVASIVE_DRIVER="com.pervasive.jdbc.v2.Driver";
    public final String DAFFODILDB_DRIVER="in.co.daffodil.db.jdbc.DaffodilDBDriver";
    public final String JDATASTORE_DRIVER="com.borland.datastore.jdbc.DataStoreDriver";
    public final String CACHE_DRIVER="com.intersys.jdbc.CacheDriver";
    public final String DERBY_DRIVER="org.apache.derby.jdbc.ClientDriver";
    public final String ALTIBASE_DRIVER="Altibase.jdbc.driver.AltibaseDriver";
    //2007.11.19 added
    public final String MCKOI_DRIVER="com.mckoi.JDBCDriver";
    public final String JSQL_DRIVER="com.jnetdirect.jsql.JSQLDriver";
    public final String JTURBO_DRIVER="com.newatlanta.jturbo.driver.Driver";
    public final String JTDS_DRIVER="net.sourceforge.jtds.jdbc.Driver";
    public final String INTERCLIENT_DRIVER="interbase.interclient.Driver";
    public final String PURE_JAVA_DRIVER="org.firebirdsql.jdbc.FBDriver";
    //2007.12.05 added
    public final String JDBC_ODBC_DRIVER="sun.jdbc.odbc.JdbcOdbcDriver";
    //2009.09.03 added
    public final String CUBRID_DRIVER="cubrid.jdbc.driver.CUBRIDDriver";
    //2007.12.05 modified
    public final String MSSQL_2000_DRIVER="com.microsoft.jdbc.sqlserver.SQLServerDriver";//SQL SERVER 2000
    public final String MSSQL_2005_DRIVER="com.microsoft.sqlserver.jdbc.SQLServerDriver";//SQL SERVER 2005
    //2010.10.01 added by hyunjun19@gmail.com
    public final String SQLITE3="org.sqlite.JDBC";
    private boolean isAcceptable=false;

    public void setDriver(String banderDriver) {
        this.banderDriver=banderDriver;
    }

    public Driver getDriver() {
        try {
            driver=(Driver) Class.forName(banderDriver).newInstance();
        } catch(Throwable t) {
            SL.getInstance().logError(t);
        }
        return driver;
    }

    public int getMajorVersion() {
        return getDriver().getMajorVersion();
    }

    public int getMinorVersion() {
        return getDriver().getMinorVersion();
    }

    public boolean jdbcCompliant() {
        return getDriver().jdbcCompliant();
    }

    public boolean acceptsURL(String url) throws SQLException {
        isAcceptable=getDriver().acceptsURL(url);
        try {
            if(!isAcceptable) {
                Enumeration enumeration=DriverManager.getDrivers();
                while(enumeration.hasMoreElements()) {
                    Object driverObject=enumeration.nextElement();
                    if(driverObject.getClass() == Class.forName(banderDriver)) {
                        DriverManager.deregisterDriver((Driver) driverObject);
                        break;
                    }
                }
            }
        } catch(Throwable t) {
            SL.getInstance().logError(t);
        }
        return isAcceptable;
    }

    public Connection connect(String url, Properties info) throws SQLException {
        acceptsURL(url);
        if(!isAcceptable)
            return null;
        else
            return new ConnectionLoggable(getDriver().connect(url, info));
    }

    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return getDriver().getPropertyInfo(url, info);
    }
}
