package core.log.exception;

import core.log.logger.SL;

/**
 * User: insup-e
 * home page: http://log4sql.sourceforge.net
 * Date Time: 2007. 9. 20 ¿ÀÈÄ 1:59:03
 * Content :
 */
public class ServiceException extends Throwable {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable t) {
        super(message, t);
        SL.getInstance().log(t);
    }

    public ServiceException(Throwable t) {
        super(t);
        SL.getInstance().log(t);
    }
}
