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
public abstract class BeforeProfiler implements Profiler {
    public BeforeProfiler() {
    }

    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        doBefore(object.getClass(), args);
        return method.invoke(object, args);
    }

    public abstract void doBefore(Class targetClass, Object[] arguments);
}
