package core.log.jdbc.driver;

import core.log.jdbc.DriverLoggables;
import core.log.logger.SL;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 10. 19 ¿ÀÈÄ 5:09:48
 * Content  :
 * Usage    :
 */
public class Mssql2005Driver extends DriverLoggables {
    public Mssql2005Driver() {
        setDriver(MSSQL_2005_DRIVER);
    }

    static {
        try {
            DriverManager.registerDriver(new Mssql2005Driver());
        } catch(SQLException e) {
            SL.getInstance().logError(e);
        }
    }
}
