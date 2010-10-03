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
public class DerbyDriver extends DriverLoggables {
    public DerbyDriver() {
        setDriver(DERBY_DRIVER);
    }

    static {
        try {
            DriverManager.registerDriver(new DerbyDriver());
        } catch(SQLException e) {
            SL.getInstance().logError(e);
        }
    }
}
