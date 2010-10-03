package core.log.triggers;

import core.log.exception.InternalException;
import core.log.exception.ServiceException;
import core.log.util.ApacheCommonLangStringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 10. 19 ¿ÀÀü 11:24:32
 * Content  :
 * Usage    :
 */
//public class TriggerRegister extends FastHashMap {
public class TriggerRegister {
    HashMap map;

    private TriggerRegister() {
        map=new HashMap();
    }

    private static TriggerRegister triggerRegister;
    private static boolean useTriggering;

    public static TriggerRegister getInstance() {
        if(triggerRegister == null)
            return triggerRegister=new TriggerRegister();
        return triggerRegister;
    }

    public void setUseTriggering(boolean useTrigger) {
        useTriggering=useTrigger;
    }

    public boolean getUseTriggering() {
        return useTriggering;
    }

    public boolean check(Object tableName) throws ServiceException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(map.containsKey(tableName))
            if(map.get(tableName) instanceof Boolean)
                return ((Boolean) map.get(tableName)).booleanValue();
            else if(map.get(tableName) instanceof Method) {
                Method method=(Method) map.get(tableName);
                method.invoke(method.getClass().newInstance(),
                              (Object[]) map.get(method));
                return true;
            } else
                throw new ServiceException("You Should Putting Value is not Boolean Type!");
        return false;
    }

    public void put(Object tableName, Class clazz, String methodName, Object[] params)
            throws ServiceException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        inputClassValidation(clazz);
        Class[] classes=new Class[params.length];
        for(int i=0; i<params.length; i++)
            classes[i]=params[i].getClass();
        Method method=clazz.getMethod(methodName, classes);
        if(tableName instanceof List) {
            for(int i=0; i<((List) tableName).size(); i++)
                map.put((String) ((List) tableName).get(i), method);
            map.put(method, params);
        } else if(tableName instanceof String)
            map.put(tableName, method);
        else
            throw new ServiceException("Table Name Type Must Be List Or String!");
    }

    public void put(Object tableName)
            throws ServiceException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if(tableName instanceof List)
            for(int i=0; i<((List) tableName).size(); i++)
                map.put((String) ((List) tableName).get(i), new Boolean(false));
        else if(tableName instanceof String)
            map.put(tableName, new Boolean(false));
        else
            throw new ServiceException("Table Name Type Must Be List Or String!");
    }

    public void unload(Object tableName) throws ServiceException {
        if(tableName instanceof List)
            for(int i=0; i<((List) tableName).size(); i++)
                map.remove((String) ((List) tableName).get(i));
        else if(tableName instanceof String)
            map.remove(tableName);
        else
            throw new ServiceException("Table Name Type Must Be List Or String!");
    }

    private void inputClassValidation(Class key) throws ServiceException {
        boolean hasDefaultContructor=false;
        Constructor[] constructors=key.getDeclaredConstructors();
        for(int i=0; i<constructors.length; i++)
            if(constructors[i].getParameterTypes().length == 0)
                hasDefaultContructor=true;
        if(!hasDefaultContructor)
            throw new ServiceException(key.getName()+" Class Must Have DefaultConstructor!");
    }

    public static synchronized String changeNotify(String sql) throws InternalException {
        String dummySql=null;
        String[] args=null;
        String tableName=null;
        String whatCud="";
        dummySql=sql
                .replaceAll("\n", "")
                .replaceAll("\t", " ")
                .replaceAll("[ ]", "^")
                .replaceAll("[iI][nN][tT][oO]", "into")
                .replaceAll("[fF][rR][oO][mM]", "from")
                .replaceAll("[iI][nN][sS][eE][rR][tT]", "insert")
                .replaceAll("[uU][pP][dD][aA][tT][eE]", "update")
                .replaceAll("[dD][eE][lL][eE][Tt][eE]", "delete");
        try {
            args=ApacheCommonLangStringUtils.split(dummySql, "^");
            if(args[0].equalsIgnoreCase("INSERT")) {
                tableName=args[2];
                whatCud="INSERT";
            } else if(args[0].equalsIgnoreCase("UPDATE")) {
                tableName=args[1];
                whatCud="UPDATE";
            } else if(args[0].equalsIgnoreCase("DELETE")) {
                if(args[1].equalsIgnoreCase("FROM"))
                    tableName=args[2];
                else
                    tableName=args[1];
                whatCud="DELETE";
            }
            //for triggering
//            if(TriggerRegister.getInstance().size()>0 &&
//               TriggerRegister.getInstance().getUseTriggering())
//                TriggerRegister.getInstance().check(tableName);
        } catch(Throwable t) {
            throw new InternalException("TriggerRegister Error!", t);
        }
        return whatCud;
    }
}
