package core.log.aop.reflection.profiler;

import java.lang.reflect.Method;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 9. 12 ¿ÀÈÄ 2:15:10
 * Content  : AOP AFTER ASPECT
 * Usage    :
 */
public abstract class AfterProfiler implements Profiler {
    public AfterProfiler() {
    }

    public Object invoke(Object object, Method method, Object[] args) throws Throwable {
        Object obj=method.invoke(object, args);
        doAfter(object.getClass(), args, obj);
        return obj;
    }

    public abstract void doAfter(Class targetClass, Object[] arguments, Object methodReturnValue);
}
