package core.log.jdbc.driver;

import core.log.logger.SL;
import core.log.jdbc.DriverLoggables;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2009. 9. 3 ¿ÀÈÄ 5:27:50
 * Content  :
 * Usage    :
 */
public class CUBRIDDriver extends DriverLoggables {
    public CUBRIDDriver() {
        setDriver(CUBRID_DRIVER);
    }

    static {
        try {
            DriverManager.registerDriver(new CUBRIDDriver());
        } catch(SQLException e) {
            SL.getInstance().logError(e);
        }
    }
}
