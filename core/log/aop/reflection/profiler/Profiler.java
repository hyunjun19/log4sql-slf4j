package core.log.aop.reflection.profiler;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 10. 26 ¿ÀÈÄ 3:54:02
 * Content  :
 * Usage    :
 */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface Profiler extends InvocationHandler {
    public Object invoke(Object object, Method method, Object[] args) throws Throwable;
}
