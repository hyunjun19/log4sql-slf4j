package core.log.aop.reflection.profiler;

import java.lang.reflect.Method;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 9. 12 ¿ÀÈÄ 2:15:10
 * Content  :
 * Usage    :
 */
public abstract class AroundProfiler implements Profiler {
    public AroundProfiler() {
    }

    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        return doAround(object, method, args);
    }

    public abstract Object doAround(Object object,
                                    Method method,
                                    Object[] args) throws Throwable;
}
