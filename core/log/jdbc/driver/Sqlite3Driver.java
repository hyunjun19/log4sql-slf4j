package core.log.jdbc.driver;

import core.log.logger.SL;
import core.log.jdbc.DriverLoggables;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Author   : hyunjun19
 * email    : hyunjun19@gmail.com
 * home page: http://westzero.net
 * version  : 1.0
 * Date Time: 2010.10.01
 * Content  :
 * Usage    :
 */
public class Sqlite3Driver extends DriverLoggables {
    public Sqlite3Driver() {
        setDriver(SQLITE3);
    }

    static {
        try {
            DriverManager.registerDriver(new Sqlite3Driver());
        } catch(SQLException e) {
            SL.getInstance().logError(e);
        }
    }
}
