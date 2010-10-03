package core.log.aop.reflection;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 10. 26 ¿ÀÈÄ 3:47:57
 * Content  :
 * Usage    :
 */
import core.log.aop.reflection.profiler.Profiler;
import core.log.impl.PreparedStatementLoggable;

import java.lang.reflect.Proxy;

public class ProfilingFactory {
    public static Object getInstance(Class target, Profiler handler) {
        return Proxy.newProxyInstance(target.getClassLoader(),
                                      target.getInterfaces(),
                                      handler);
    }

    public static Object sqlTraceProfiling(Profiler handler) {
        return Proxy.newProxyInstance(PreparedStatementLoggable.class.getClassLoader(),
                                      PreparedStatementLoggable.class.getInterfaces(),
                                      handler);
    }
}