package core.log.conf.reloadable;

import core.log.exception.InternalException;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.HashSet;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 11. 2 ���� 9:44:32
 * Content  :
 * Usage    :
 */
public class ReloadableConfiguration extends HashMap {
    private static ReloadableConfiguration reloadableConfiguration;
    private static Set fielteredMap=new HashSet();
    private static final String PROPERTY_POSITION="log4sql";
    static String logLevel;
    static String fixedSelectPosition;
    static String fixedNoneSelectPosition;
    static String selectPosition;
    static String noneSelectPosition;
    static String viewPosition;
    static String viewInternalException;
    static String viewAppoint;
    static String asynchronous;
    static String useSlf4j;

    private ReloadableConfiguration() {
        try {
            super.put("log.level", new Integer(ReloadableConfiguration.logLevel));
        } catch(Throwable t) {
            new InternalException("log.level value must be Integer");
        }
        try {
            super.put("fixed.select", new Boolean(ReloadableConfiguration.fixedSelectPosition));
        } catch(Throwable t) {
            new InternalException("fixed.select value must be Boolean [true or false]");
        }
        try {
            super.put("fixed.none_select", new Boolean(ReloadableConfiguration.fixedNoneSelectPosition));
        } catch(Throwable t) {
            new InternalException("fixed.none_select value must be Boolean [true or false]");
        }
        try {
            super.put("select", new Integer(ReloadableConfiguration.selectPosition));
        } catch(Throwable t) {
            new InternalException("select value must be Integer");
        }
        try {
            super.put("none_select", new Integer(ReloadableConfiguration.noneSelectPosition));
        } catch(Throwable t) {
            new InternalException("none_select value must be Integer");
        }
        try {
            super.put("view.position", new Boolean(ReloadableConfiguration.viewPosition));
        } catch(Throwable t) {
            new InternalException("view.position value must be Boolean [true or false]");
        }
        try {
            super.put("view.internal.exception", new Boolean(ReloadableConfiguration.viewInternalException));
        } catch(Throwable t) {
            new InternalException("view.internal.exception value must be Boolean [true or false]");
        }
        try {
            super.put("view.appoint", viewAppoint);
        } catch(Throwable t) {
            new InternalException(t);
        }
        try {
            super.put("log.asynchronous", new Boolean(ReloadableConfiguration.asynchronous));
        } catch(Throwable t) {
            new InternalException("query.logging.asynchronous value must be Boolean [true or false]");
        }
        try {
        	super.put("log.slf4j.use", new Boolean(ReloadableConfiguration.useSlf4j));
        } catch(Throwable t) {
        	new InternalException("log.slf4j.use value must be Boolean [true or false]");
        }

        /**
         * fieltering method name
         */
        fielteredMap.add("hashCode");//for hibernate added
        fielteredMap.add("getMaxRows");//for hibernate added
        fielteredMap.add("getQueryTimeout");//for hibernate added
        fielteredMap.add("getFetchSize");//for weblogic8.1
        fielteredMap.add("getMaxFieldSize");//for weblogic8.1
        fielteredMap.add("getFetchDirection");//for weblogic8.1
        fielteredMap.add("closePreparedStatements");//for ibatis
        fielteredMap.add("closeConnection");//for ibatis
    }

    static {
        try {
            logLevel=ResourceBundle.getBundle(PROPERTY_POSITION).getString("log.level");
            fixedSelectPosition=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.position.fixed.select");
            fixedNoneSelectPosition=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.position.fixed.none_select");
            selectPosition=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.position.select");
            noneSelectPosition=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.position.none_select");
            viewPosition=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.view.position");
            viewInternalException=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.view.internal.exception");
            viewAppoint=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.view.appoint");
            asynchronous=ResourceBundle.getBundle(PROPERTY_POSITION).getString("query.logging.asynchronous");
            useSlf4j=ResourceBundle.getBundle(PROPERTY_POSITION).getString("log.slf4j.use");
        } catch(Throwable t) {
            new InternalException(t);
        }
    }

    public static ReloadableConfiguration getInstance() {
        if(reloadableConfiguration == null)
            return reloadableConfiguration=new ReloadableConfiguration();
        return reloadableConfiguration;
    }

    public int getLogLevel() {
        return ((Integer) super.get("log.level")).intValue();
    }

    public boolean getFixedSelectPosition() {
        return ((Boolean) super.get("fixed.select")).booleanValue();
    }

    public boolean getFixedNoneSelectPosition() {
        return ((Boolean) super.get("fixed.none_select")).booleanValue();
    }

    public int getSelectPosition() {
        return ((Integer) super.get("select")).intValue();
    }

    public int getNoneSelectPosition() {
        return ((Integer) super.get("none_select")).intValue();
    }

    public boolean getViewPosition() {
        return ((Boolean) super.get("view.position")).booleanValue();
    }

    public boolean getViewInternalException() {
        return ((Boolean) super.get("view.internal.exception")).booleanValue();
    }

    public String getViewAppoint() {
        return (String) super.get("view.appoint");
    }

    public boolean getAsynchronous() {
        return ((Boolean) super.get("log.asynchronous")).booleanValue();
    }
    
    public boolean getUseSlf4j() {
    	return ((Boolean) super.get("log.slf4j.use")).booleanValue();
    }

    public void setLogLevel(int val) {
        super.put("log.level", new Integer(val));
    }

    public void setFixedSelectPosition(boolean bool) {
        super.put("fixed.select", new Boolean(bool));
    }

    public void setFixedNoneSelectPosition(boolean bool) {
        super.put("fixed.none_select", new Boolean(bool));
    }

    public void setSelectPosition(int val) {
        super.put("select", new Integer(val));
    }

    public void setNoneSelectPosition(int val) {
        super.put("none_select", new Integer(val));
    }

    public void setViewPosition(boolean bool) {
        super.put("view.position", new Boolean(bool));
    }

    public void setViewInternalException(boolean bool) {
        super.put("view.internal.exception", new Boolean(bool));
    }

    public void setViewAppoint(String earMark) {
        super.put("view.appoint", earMark);
    }

    public void setAsynchronous(boolean asynchronous) {
        super.put("log.asynchronous", new Boolean(asynchronous));
    }
    
    public void setUseSlf4j(boolean useSlf4j) {
    	super.put("log.slf4j.use", new Boolean(useSlf4j));
    }

    public boolean isFieltered(String fielterdMethodName) {
        return !fielteredMap.contains(fielterdMethodName);
    }
}
