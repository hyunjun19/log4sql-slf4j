package core.log.util;

import core.log.exception.InternalException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 12. 7 ¿ÀÀü 9:58:17
 * Content  :
 * Usage    :
 */
public class PatternCheck {
    private static final String ALL_P="*..*";
    private static final String ALL_PACKAGE="ALL_P";
    private static final String ALL_C="*";
    private static final String ALL_CLASS="ALL_C";
    private static final String ALL_M="(..)";
    private static final String ALL_METHOD="ALL_M";
    private static final String ALL=ALL_PACKAGE+"."+ALL_CLASS+"."+ALL_METHOD;

    public PatternCheck() {
    }

    public boolean doLog(String pattern, Throwable throwable) {
        try {
            String replacedPattern=replacePatternString(pattern);
            if(replacedPattern.equals(ALL))
                return true;
            StackTraceElement[] stackTraceElement=throwable.getStackTrace();
            for(int i=0; stackTraceElement != null && i<stackTraceElement.length; i++)
            {
                String packageName=null;
                String[] packageNameArray=null;
                try {
                    packageName=stackTraceElement[i].getClassName().substring(0, stackTraceElement[i].getClassName().lastIndexOf('.'));
                    packageNameArray=ApacheCommonLangStringUtils.split(packageName, ".");
                } catch(Throwable t) {
                    continue;
                }
                String className=ApacheCommonLangStringUtils.replace(stackTraceElement[i].getClassName(), packageName, "").substring(1);
                String methodName=stackTraceElement[i].getMethodName();
                if((getPackageNames(replacedPattern).equals(ALL_PACKAGE) || (ApacheCommonLangStringUtils.contains(getPackageNames(replacedPattern), ".") ?
                                                                             ApacheCommonLangStringUtils.contains(packageName, getPackageNames(replacedPattern)) : isContain(packageNameArray, getPackageNames(replacedPattern)))) &&
                                                                                                                                                                                                                                   (getClassName(replacedPattern).equals(ALL_CLASS) || className.equals(getClassName(replacedPattern))) &&
                                                                                                                                                                                                                                   (getMethodName(replacedPattern).equals(ALL_METHOD) || methodName.equals(getMethodName(replacedPattern))))
                    return true;
            }
        } catch(Throwable t) {
            new InternalException("Check your appointed log format :\n\t\t\t*..* : means all package log\n\t\t\t*    : means all classes\n\t\t\t(..) : means all methods\n\t\t\tSample : *..*.*.(..) -all operation will leave logs.");
        }
        return false;
    }

    private String getPackageNames(String replacedPattern) {
        String packages=replacedPattern.
                substring(0, replacedPattern.lastIndexOf('.'));
        packages=packages.substring(0, packages.lastIndexOf('.'));
        if(packages.equals(ALL_PACKAGE))
            return ALL_PACKAGE;
        if(patternCount(ALL_PACKAGE, packages) == 2) {
            return packages.replaceAll(ALL_PACKAGE, "").
                    substring(1, ApacheCommonLangStringUtils.lastIndexOf(packages.replaceAll(ALL_PACKAGE, ""), "."));
        } else if(patternCount(ALL_PACKAGE, packages) == 1) {
            if(packages.startsWith(ALL_PACKAGE)) {
                return ApacheCommonLangStringUtils.replace(packages, ALL_PACKAGE, "").substring(1, ApacheCommonLangStringUtils.replace(packages, ALL_PACKAGE, "").length());
            } else {
                return ApacheCommonLangStringUtils.replace(packages, ALL_PACKAGE, "").substring(0, ApacheCommonLangStringUtils.replace(packages, ALL_PACKAGE, "").length()-1);
            }
        } else
            return packages;
    }

    private String getClassName(String replacedPattern) {
        if(ApacheCommonLangStringUtils.contains(replacedPattern, ALL_CLASS))
            return ALL_CLASS;
        else
            return replacedPattern.
                    substring(replacedPattern.substring(0, replacedPattern.lastIndexOf(".")).lastIndexOf(".")+1,
                              replacedPattern.lastIndexOf("."));
    }

    private String getMethodName(String replacedPattern) {
        if(ApacheCommonLangStringUtils.contains(replacedPattern, ALL_METHOD))
            return ALL_METHOD;
        else
            return replacedPattern.substring(replacedPattern.lastIndexOf('.')+1);
    }

    private boolean isContain(String[] packageNameArray, String packageName) {
        if(packageNameArray == null || packageName == null)
            return false;
        for(int i=0; i<packageNameArray.length; i++)
            if(packageNameArray[i] != null && packageNameArray[i].equals(packageName))
                return true;
        return false;
    }

    private String replacePatternString(String pattern) {
        return ApacheCommonLangStringUtils.replace(
                ApacheCommonLangStringUtils.replace(
                        ApacheCommonLangStringUtils.replace(pattern, ALL_M, ALL_METHOD),
                        ALL_P, ALL_PACKAGE),
                ALL_C, ALL_CLASS);
    }

    private int patternCount(String pattern, String patternStr) {
        Pattern p=Pattern.compile(pattern);
        Matcher m=p.matcher(patternStr);
        int cnt=0;
        while(m.find()) {
            cnt++;
        }
        return cnt;
    }

    public static void main(String[] arg) {
        PatternCheck patternCheck=new PatternCheck();
        String classNmethod=".*.(..)";
        String method=".(..)";
        String packageNclass="*..*.*.";
        //package
        String pp1=patternCheck.replacePatternString("*..*"+classNmethod);
        String pp2=patternCheck.replacePatternString("com.*..*"+classNmethod);
        String pp3=patternCheck.replacePatternString("*..*.sup.in.song.*..*"+classNmethod);
        String pp4=patternCheck.replacePatternString("*..*.sup.in.song"+classNmethod);
        String pp5=patternCheck.replacePatternString("sup.in.song"+classNmethod);
        //class
        String pp6=patternCheck.replacePatternString("*..*.Something"+method);
        String pp7=patternCheck.replacePatternString("com.*..*.Something"+method);
        String pp8=patternCheck.replacePatternString("sup.in.song.Something"+method);
        String pp9=patternCheck.replacePatternString("sup.in.song.*"+method);
        //method
        String pp10=patternCheck.replacePatternString(packageNclass+"doSomething");
        String pp11=patternCheck.replacePatternString("sup.in.song.Something.doSomething");
        String pp12=patternCheck.replacePatternString("sup.in.song.Something.(..)");
        System.out.println(pp12);
        System.out.println(patternCheck.getMethodName(pp12));
    }
}
