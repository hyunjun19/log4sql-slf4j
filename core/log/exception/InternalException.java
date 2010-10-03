package core.log.exception;

import core.log.conf.reloadable.ReloadableConfiguration;
import core.log.logger.SL;

/**
 * User: song insup
 * home page: http://log4sql.sourceforge.net
 * Date Time: 2007. 9. 20 ¿ÀÈÄ 1:59:03
 * Content :
 */
public class InternalException extends Throwable {
    private String message;
    private Throwable t;

    public InternalException() {
        super();
    }

    public InternalException(String message, Throwable t) {
        super(message, t);
        this.message=message;
        this.t=t;
        if(ReloadableConfiguration.getInstance().getViewInternalException())
            SL.getInstance().logError(t, message);
    }

    public InternalException(String message) {
        super();
        this.message=message;
        if(ReloadableConfiguration.getInstance().getViewInternalException())
            SL.getInstance().log(message, SL.ERROR);
    }

    public InternalException(Throwable t) {
        super(t);
        this.t=t;
        message=t.getMessage();
        if(ReloadableConfiguration.getInstance().getViewInternalException())
            SL.getInstance().logError(t);
    }

    public String getMessage() {
        return message;
    }

    public Throwable getThrowableCause() {
        return t;
    }
}
