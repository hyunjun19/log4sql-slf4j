package core.log.logger;

import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.log.conf.reloadable.ReloadableConfiguration;
import core.log.exception.InternalException;
import core.log.logger.resource.LogResource;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 9. 12 오후 2:15:10
 * Content  :
 * Usage    :
 */
public class SL {
    private static SL sl;
    public static final int DEBUG=0;
    public static final int INFO=1;
    public static final int WARNING=2;
    public static final int FATAL=3;
    public static final int ERROR=4;
    public static final int LOG_OFF=5;
    public final int[] LEVEL={DEBUG, INFO, WARNING, FATAL, ERROR, LOG_OFF};
    private final int[] LOG_INDEX={1, 3, 2};//{logger(),logger(int[boolean]},logger(object)}
    private final int PARAMMAP_LOG_INDEX=3;
    private final int ERROR_LOG_INDEX=0;
    private final int TRACE_LOG_INDEX=2;
    private String dateFormat=null;
    private final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    private final String UNIQUE_STRING="################################################";
    
    Logger logger = LoggerFactory.getLogger("log4sql");

    private SL() {
        try {
            ReloadableConfiguration.getInstance().put("debug", new Integer(DEBUG));
            ReloadableConfiguration.getInstance().put("info", new Integer(INFO));
            ReloadableConfiguration.getInstance().put("warning", new Integer(WARNING));
            ReloadableConfiguration.getInstance().put("fatal", new Integer(FATAL));
            ReloadableConfiguration.getInstance().put("error", new Integer(ERROR));
            ReloadableConfiguration.getInstance().put("log_level", LEVEL);
            ReloadableConfiguration.getInstance().put("log_off", new Integer(LOG_OFF));
            ReloadableConfiguration.getInstance().put(new Integer(DEBUG), "DEBUG");
            ReloadableConfiguration.getInstance().put(new Integer(INFO), "INFO");
            ReloadableConfiguration.getInstance().put(new Integer(WARNING), "WARNING");
            ReloadableConfiguration.getInstance().put(new Integer(FATAL), "FATAL");
            ReloadableConfiguration.getInstance().put(new Integer(ERROR), "ERROR");
            ReloadableConfiguration.getInstance().put(new Integer(LOG_OFF), "LOG_OFF");
        } catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public static SL getInstance() {
        if(sl == null)
            return sl=new SL();
        return sl;
    }

    private String now() {
        return (new java.text.SimpleDateFormat(
                this.dateFormat == null ? DEFAULT_DATE_FORMAT : dateFormat)).format(new java.util.Date());
    }

    public int get(String logLevel) {
        return ReloadableConfiguration.getInstance().getLogLevel();
    }

    public Object put(Object obj, Object value) {
        //do nothing
        return null;
    }

    public void putAll(Map map) {
        //do nothing
    }

    public Map getLogLevelMap() {
        return ReloadableConfiguration.getInstance();
    }

    public void setDateFormat(String format) {
        this.dateFormat=format;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public int getDebug() {
        return SL.DEBUG;
    }

    public int getInfo() {
        return SL.INFO;
    }

    public int getWarning() {
        return SL.WARNING;
    }

    public int getFatal() {
        return SL.FATAL;
    }

    public int getError() {
        return SL.ERROR;
    }

    public String getDebugName() {
        return (String) ReloadableConfiguration.getInstance().get(new Integer(DEBUG));
    }

    public String getInfoName() {
        return (String) ReloadableConfiguration.getInstance().get(new Integer(INFO));
    }

    public String getWarningName() {
        return (String) ReloadableConfiguration.getInstance().get(new Integer(WARNING));
    }

    public String getFatalName() {
        return (String) ReloadableConfiguration.getInstance().get(new Integer(FATAL));
    }

    public String getErrorName() {
        return (String) ReloadableConfiguration.getInstance().get(new Integer(ERROR));
    }

    public String getLogLevelName(int logLevel) {
        return (String) ReloadableConfiguration.getInstance().get(new Integer(logLevel));
    }

    /**
     * 로그레벨을 지정한다.
     * @param logLevel
     */
    public void setLogLevel(int logLevel) {
        if(logLevel>5 || logLevel<0)
            logLevel=0;
        ReloadableConfiguration.getInstance().setLogLevel(logLevel);
        ReloadableConfiguration.getInstance().put("now", new Integer(logLevel));
        System.out.println("#######################################################");
        System.out.println("#           LOG LEVEL CHANGED ["+ReloadableConfiguration.getInstance().getLogLevel()+"]");
        System.out.println("#######################################################");
    }

    public void setLogOff() {
        ReloadableConfiguration.getInstance().setLogLevel(5);
    }

    private boolean checkLogLevel(int logLevel) {
        return ReloadableConfiguration.getInstance().getLogLevel()<=logLevel;
    }

    public PositionInfo postionInfo(Throwable t, int index) {
        PositionInfo positionInfo=null;
        try {
            positionInfo=new PositionInfo(t.getStackTrace().length<=index ? t.getStackTrace()[t.getStackTrace().length-1] : t.getStackTrace()[index],
                                          t.getMessage());
        } catch(Throwable _t) {
            new InternalException(_t);
        }
        return positionInfo;
    }

    public PositionInfo postionInfo(Throwable t) {
        return new PositionInfo(t.getStackTrace(), TRACE_LOG_INDEX);
    }

    public PositionInfo postionInfoForPositionCheck(Throwable t) {
        return new PositionInfo(t.getStackTrace(), 0);
    }

    private void getMessageThrowable(Object obj) throws Throwable {
        if(obj instanceof String)
            throw new Throwable((String) obj);
        else if(obj instanceof Integer)
            throw new Throwable(((Integer) obj).toString());
        else if(obj instanceof Boolean)
            throw new Throwable(((Boolean) obj).toString());
        else if(obj instanceof Throwable)
            throw new Throwable((Throwable) obj);
        else if(obj instanceof Properties)
            throw new Throwable(((Properties) obj).values().toString());
        else if(obj == null)
            throw new Throwable("null");
        else
            throw new Throwable(obj.toString());
    }

    public Throwable getMessageThrowableForThread(Object obj) {
        if(obj instanceof String)
            return new Throwable((String) obj);
        else if(obj instanceof Integer)
            return new Throwable(((Integer) obj).toString());
        else if(obj instanceof Boolean)
            return new Throwable(((Boolean) obj).toString());
        else if(obj instanceof Throwable)
            return new Throwable((Throwable) obj);
        else if(obj instanceof Properties)
            return new Throwable(((Properties) obj).values().toString());
        else if(obj == null)
            return new Throwable("null");
        else
            return new Throwable(obj.toString());
    }

    public boolean getMessageThrowableForThread(String packages, Throwable throwable) {
        StackTraceElement[] stackTraceElement=throwable.getStackTrace();
        for(int i=0; stackTraceElement != null && i<stackTraceElement.length; i++)
            if(stackTraceElement[i].getClassName().startsWith(packages))
                return true;
        return false;
    }

    public void log() {
        if(checkLogLevel(getDebug()))
            try {
                throw new Throwable(UNIQUE_STRING);
            } catch(Throwable t) {
                rowLogging(t, getDebug(), LOG_INDEX[0]);
            }
    }

    public void log(int message) {
        log(new Integer(message), true);
    }

    public void log(int message, int logLevel) {
        log(new Integer(message), logLevel, true);
    }

    public void log(boolean bool) {
        log(new Boolean(bool), true);
    }

    public void log(boolean bool, int logLevel) {
        log(new Boolean(bool), logLevel, true);
    }

    public void log(Object obj) {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLogging(t, getDebug(), LOG_INDEX[2]);
            }
    }

    public void log(Object obj, boolean isPrimitive) {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLogging(t, getDebug(), LOG_INDEX[1]);
            }
    }

    public void log(Object obj, String message) {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLogging(t, getDebug(), LOG_INDEX[2], message);
            }
    }

    public void log(Object obj, int logLevel) {
        if(checkLogLevel(logLevel))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLogging(t, logLevel, LOG_INDEX[2]);
            }
    }

    public void log(Object obj, int logLevel, boolean isPrimitive) {
        if(checkLogLevel(logLevel))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLogging(t, logLevel, LOG_INDEX[1]);
            }
    }

    public void logTrace() {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(null);
            } catch(Throwable t) {
                rowLoggingTrace(t, getDebug());
            }
    }

    public void logTrace(Object obj) {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLoggingTrace(t, getDebug(), t.getMessage());
            }
    }

    public void logTrace(Object obj, int logLevel) {
        if(checkLogLevel(logLevel))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLoggingTrace(t, logLevel);
            }
    }

    public void logTrace(Object obj, String message) {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLoggingTrace(t, getDebug(), message);
            }
    }

    public void logTrace(Object obj, String message, int logLevel) {
        if(checkLogLevel(logLevel))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLoggingTrace(t, logLevel, message);
            }
    }

    public void logSql(LogResource logResource) {
        if(checkLogLevel(getDebug())) {
            rowLoggingSql(logResource, getDebug(), ReloadableConfiguration.getInstance().getSelectPosition(), logResource.getElapsedTime(), logResource.getResult());
        }
    }

    public void logSql(Object obj, String elapsedTime, String queryResult, int logLevel) {
        if(checkLogLevel(logLevel))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLoggingSql(t, logLevel, ReloadableConfiguration.getInstance().getSelectPosition(), elapsedTime, queryResult);
            }
    }

    public void logOnlyLog(Object obj) {
        if(checkLogLevel(getDebug()))
            System.out.println(obj.toString());
    }

    public void logOnlyLog(Object obj, int logLevel) {
        if(checkLogLevel(logLevel))
            if(checkLogLevel(getDebug()))
                System.out.println(obj.toString());
    }

    public void logError(Throwable e) {
        try {
            if(checkLogLevel(getError()))
                systemLogging(new PositionInfo(e.getCause() != null && e.getCause().getStackTrace().length>=ERROR_LOG_INDEX ? e.getCause().getStackTrace()[ERROR_LOG_INDEX] : e.getStackTrace().length>0 ? e.getStackTrace()[e.getStackTrace().length-1] : e.getStackTrace()[0],
                                               e.getCause() != null && e.getCause().getStackTrace().length>=ERROR_LOG_INDEX ? e.getCause().getStackTrace() : e.getStackTrace(),
                                               e.getCause() != null && e.getCause().getStackTrace().length>=ERROR_LOG_INDEX ? e.getCause().getMessage() : e.getMessage()),
                              e.getCause() != null ? e.getCause().getClass().getName() : e.getClass().getName());
        } catch(Throwable _t) {
            new InternalException(_t);
        }
    }

    public void logError(Throwable e, String message) {
        try{
        if(checkLogLevel(getError()))
            systemLogging(new PositionInfo(e.getCause() != null && e.getCause().getStackTrace().length>=ERROR_LOG_INDEX ? e.getCause().getStackTrace()[ERROR_LOG_INDEX] : e.getStackTrace().length>0 ? e.getStackTrace()[e.getStackTrace().length-1] : e.getStackTrace()[0],
                                           e.getCause() != null && e.getCause().getStackTrace().length>0 ? e.getCause().getStackTrace() : e.getStackTrace(),
                                           e.getCause() != null && e.getCause().getStackTrace().length>0 ? e.getCause().getMessage() : e.getMessage()),
                          e.getCause() != null ? e.getCause().getClass().getName() : e.getClass().getName(),
                          message);
            }catch(Throwable _t){
            new InternalException(_t);
        }
    }

    public void logParamMap(Object obj) {
        if(checkLogLevel(getDebug()))
            try {
                getMessageThrowable(obj);
            } catch(Throwable t) {
                rowLoggingSql(t, getDebug(), PARAMMAP_LOG_INDEX);
            }
    }

    public void logTraceForPositionCheck(Throwable t, String message) {
        rowLoggingTraceForPositionCheck(t, message);
    }

    private void rowLogging(Throwable t, int logLevel, int logIndex) {
        try {
            systemLogging(logLevel, postionInfo(t, logIndex));
        } catch(Throwable _t) {
            new InternalException(_t);
        }
    }

    private void rowLogging(Throwable t, int logLevel, int logIndex, String message) {
        try {
            systemLogging(logLevel, postionInfo(t, logIndex), message);
        } catch(Throwable _t) {
            new InternalException(_t);
        }
    }

    private void rowLoggingSql(Throwable t, int logLevel, int logIndex) {
        try {
            systemLogging(logLevel, postionInfo(t, logIndex));
        } catch(Throwable _t) {
            new InternalException(_t);
        }
    }

    private void rowLoggingSql(Throwable t, int logLevel, int logIndex, String elapsedTime, String result) {
        try {
            systemLogging(logLevel, postionInfo(t, logIndex), elapsedTime, result);
        } catch(Throwable _t) {
            new InternalException(_t);
        }
    }

    private void rowLoggingSql(LogResource logResource, int logLevel, int logIndex, String elapsedTime, String result) {
        try {
            systemLogging(logLevel, logResource, postionInfo(logResource.getThrowable(), logIndex), elapsedTime, result);
        } catch(Throwable _t) {
            new InternalException(_t);
        }
    }

    private void rowLoggingTrace(Throwable t, int logLevel) {
        systemLogging(logLevel, postionInfo(t));
    }

    private void rowLoggingTrace(Throwable t, int logLevel, String message) {
        systemLogging(logLevel, postionInfo(t), message);
    }

    private void rowLoggingTraceForPositionCheck(Throwable t, String message) {
        systemLoggingForPositionCheck(postionInfoForPositionCheck(t), message);
    }

    private void systemLogging(PositionInfo positionInfo, String exceptionClassName) {
        outter(new StringBuffer()
//                .append("[").append(now()).append("]")
//                .append(" [").append(getErrorName()).append("]")
                .append("『").append(positionInfo.getClassName()).append(":")
                .append(positionInfo.getMethodName()).append("(").append(positionInfo.getLineNumber()).append(")』\n")
                .append(exceptionClassName).append("\n")
                .append(positionInfo.getMsg() != null ? positionInfo.getMsg() : "").append("\n")
                .toString());
    }

    private void systemLogging(PositionInfo positionInfo, String exceptionClassName, String message) {
        outter(new StringBuffer()
//                .append("[").append(now()).append("]")
//                .append(" [").append(getErrorName()).append("]")
                .append("『").append(positionInfo.getClassName()).append(":")
                .append(positionInfo.getMethodName()).append("(").append(positionInfo.getLineNumber()).append(")』\n")
                .append(exceptionClassName).append("\n")
                .append(message).append("\n")
                .append(positionInfo.getMsg() != null ? positionInfo.getMsg() : "").append("\n")
                .toString());
    }

    private void systemLogging(int logLevel, PositionInfo positionInfo) {
        outter(new StringBuffer()
//                .append("[").append(now()).append("]")
//                .append(" [").append(getLogLevelName(logLevel)).append("]")
                .append("『").append(positionInfo.getClassName()).append(":")
                .append(positionInfo.getMethodName()).append("(").append(positionInfo.getLineNumber()).append(")』\n")
                .append(positionInfo.getMsg() != null ? positionInfo.getMsg() : "").append("\n")
                .toString());
    }

    private void systemLogging(int logLevel, PositionInfo positionInfo, String message, String result) {
        outter(new StringBuffer()
//                .append("[").append(now()).append("]")
//                .append(" [").append(getLogLevelName(logLevel)).append("]")
                .append("『").append(positionInfo.getClassName()).append(":")
                .append(positionInfo.getMethodName()).append("(").append(positionInfo.getLineNumber()).append(")』")
                .append(" Elapsed Time [").append(message).append("]\n")
                .append(positionInfo.getMsg() != null ? positionInfo.getMsg() : "").append("\n")
                .append(result != null ? "Query Result"+result+"row\n" : "\n")
                .toString());
    }

    private void systemLogging(int logLevel, LogResource logResource, PositionInfo positionInfo, String message, String result) {
        StringBuffer sb=new StringBuffer();
//        sb.append("[").append(now()).append("]");
//        sb.append(" [").append(getLogLevelName(logLevel)).append("]");
        sb.append("『").append(positionInfo.getClassName()).append(":");
        sb.append(positionInfo.getMethodName()).append("(").append(positionInfo.getLineNumber()).append(")』");
        sb.append(" Elapsed Time [").append(message).append("]\n");
        sb.append(positionInfo.getMsg() != null ? positionInfo.getMsg() : "").append("\n");
        if(logResource.isSelect() && !ReloadableConfiguration.getInstance().getFixedSelectPosition())
            sb.append(postionInfoForPositionCheck(logResource.getThrowable()).getMsg());
        if(!logResource.isSelect() && !ReloadableConfiguration.getInstance().getFixedNoneSelectPosition())
            sb.append(postionInfoForPositionCheck(logResource.getThrowable()).getMsg());
        sb.append(result != null ? "Query Result"+result+"row\n" : "\n");
        outter(sb.toString());
    }

    private void systemLogging(int logLevel, PositionInfo positionInfo, String message) {
        outter(new StringBuffer()
//                .append("[").append(now()).append("]")
//                .append(" [").append(getLogLevelName(logLevel)).append("]")
                .append("『").append(positionInfo.getClassName()).append(":")
                .append(positionInfo.getMethodName()).append("(").append(positionInfo.getLineNumber()).append(")』\n")
                .append(message).append("\n")
                .append(positionInfo.getMsg()).append("\n")
                .toString());
    }

    private void systemLoggingForPositionCheck(PositionInfo positionInfo, String message) {
        outter(new StringBuffer()
                .append("[Position Check Start")
                .append(message != null ? " :\n"+message : "").append("]\n")
                .append(positionInfo.getMsg()).append("\n")
                .append("[Position Check End]\n")
                .toString());
    }

    /**
     * 2010.10.03 slf4j 적용
     * @param message
     */
	private void outter(String message) {
		switch (ReloadableConfiguration.getInstance().getLogLevel()) {
		case DEBUG:
			logger.debug(message);
			break;
		case INFO:
			logger.info(message);
			break;
		case WARNING:
			logger.warn(message);
			break;
		case FATAL:
			logger.trace(message);
			break;
		case ERROR:
			logger.error(message);
			break;
		default:
			// no message
		}
	}

    private class PositionInfo {
        private final String TRACE_MESSAGE="############## BELOW MESSAGE IS TRACE. NO ERROR! ##############\n";
        private final String msg;
        private final String className;
        private final String methodName;
        private final int lineNumber;

        public PositionInfo(StackTraceElement stackTraceElement, String message) {
            this.className=stackTraceElement.getClassName();
            this.methodName=stackTraceElement.getMethodName();
            this.lineNumber=stackTraceElement.getLineNumber();
            this.msg=message;
        }

        public PositionInfo(StackTraceElement[] stackTraceElements, int traceLoggingIndex) {
            StringBuffer msg=new StringBuffer();
            for(int i=traceLoggingIndex; i<stackTraceElements.length; i++)
                msg.append("\t").append("[").append(i).append("] at ").append(stackTraceElements[i]).append("\n");
            this.className=stackTraceElements[traceLoggingIndex].getClassName();
            this.methodName=stackTraceElements[traceLoggingIndex].getMethodName();
            this.lineNumber=stackTraceElements[traceLoggingIndex].getLineNumber();
            this.msg=new StringBuffer().append(TRACE_MESSAGE).append(msg).toString();
        }

        public PositionInfo(StackTraceElement[] stackTraceElements, String message, int traceLoggingIndex) {
            StringBuffer msg=new StringBuffer();
            msg.append(message).append("\n");
            for(int i=traceLoggingIndex; i<stackTraceElements.length; i++)
                msg.append("\t").append("at ").append(stackTraceElements[i]).append("\n");
            this.className=stackTraceElements[traceLoggingIndex].getClassName();
            this.methodName=stackTraceElements[traceLoggingIndex].getMethodName();
            this.lineNumber=stackTraceElements[traceLoggingIndex].getLineNumber();
            this.msg=new StringBuffer().append(TRACE_MESSAGE).append(msg).toString();
        }

        public PositionInfo(StackTraceElement stackTraceElement,
                            StackTraceElement[] stackTraceElements, String message) {
            StringBuffer msg=new StringBuffer();
            msg.append(message).append("\n");
            for(int i=0; stackTraceElements != null && i<stackTraceElements.length; i++)
                msg.append("\t").append("at ").append(stackTraceElements[i]).append("\n");
            this.className=stackTraceElement.getClassName();
            this.methodName=stackTraceElement.getMethodName();
            this.lineNumber=stackTraceElement.getLineNumber();
            this.msg=msg.toString();
        }

        public String getMsg() {
            return msg;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public int getLineNumber() {
            return lineNumber;
        }
    }
}
