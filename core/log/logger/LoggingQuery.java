package core.log.logger;

import core.log.conf.reloadable.ReloadableConfiguration;
import core.log.exception.InternalException;
import core.log.util.ApacheCommonLangStringUtils;
import core.log.util.format.Formatter;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 2. 17 ¿ÀÈÄ 9:49:54
 * Content  :
 * Usage    :
 */
public class LoggingQuery {
    private static final String parameterPositionCharacter="/**P*/";

    private LoggingQuery() {
    }

    public static String log(String query, Object obj) {
        return log(ApacheCommonLangStringUtils.
                replace(query.
                replaceAll("#\\w+#", "\\?").
                replaceAll("[^'\\w+]\\(:\\w+,", "(\\?,").
                replaceAll("[^'\\w+]:\\w+,", "\\?,").
                replaceAll("[^'\\w+]:\\w+", "\\?"),
                        parameterPositionCharacter, ""), (Object[]) obj);
    }

    private static String log(String query, Object[] obj) {
        if(ReloadableConfiguration.getInstance().getViewPosition())
            query=ApacheCommonLangStringUtils.replace(query, "?", new StringBuffer("?").append(parameterPositionCharacter).toString());
        return queryLog(query, (Object[]) obj);
    }

    private static String queryLog(String query, Object[] objArray) {
        StringBuffer buf=new StringBuffer();
        String queryRe=query;
        if(!ApacheCommonLangStringUtils.contains(queryRe, '\n'))
            queryRe=new Formatter(query).format();
        try {
            if(objArray == null || objArray.length == 0) {
                return buf.append(queryRe).toString();
            } else {
                int sidx=0;
                for(int i=0; i<objArray.length; i++) {
                    int idx=queryRe.indexOf('?', sidx);
                    if(idx != -1) {
                        if(objArray[i] instanceof String)
                            buf.append(queryRe.substring(sidx, idx))
                                    .append(" '").append((String) objArray[i]).append("' ");
                        else if(objArray[i] instanceof Integer)
                            buf.append(queryRe.substring(sidx, idx))
                                    .append(" ").append(((Integer) objArray[i]).intValue()).append(" ");
                        else if(objArray[i] instanceof Float)
                            buf.append(queryRe.substring(sidx, idx))
                                    .append(" ").append(((Float) objArray[i]).floatValue()).append(" ");
                        else if(objArray[i] instanceof Double)
                            buf.append(queryRe.substring(sidx, idx))
                                    .append(" ").append(((Double) objArray[i]).doubleValue()).append(" ");
                        else if(objArray[i] instanceof Long)
                            buf.append(queryRe.substring(sidx, idx))
                                    .append(" ").append(((Long) objArray[i]).longValue()).append(" ");
                        else
                            buf.append(queryRe.substring(sidx, idx))
                                    .append(" ").append(objArray[i]).append(" ");
                        sidx=idx+1;
                    } else
                        buf=new StringBuffer(buf.toString().replaceAll("[### Parameters count not matched ###]", ""))
                                .append("[### Parameters count not matched ###]\n")
                                .append(buf);
                }
                buf.append(queryRe.substring(sidx));
                return buf.toString();
            }
        } catch(Exception ex) {
            return new InternalException(ex).getMessage();
        }
    }

    public static void main(String[] args) {
        String temp="VALUES ( :SESSION_ID, :ACCESS_LOG_ID, TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), :PAGE_REQ_SEQ, :IS_WORKFLOW)";
//		String temp="TO_CHAR(TO_DATE(substr(concat_col,0,14),'YYYY/MM/DD hh24:mi:ss'),'YYYY/MM/DD hh24:mi:ss') AS rfid_work_date";
        SL.getInstance().log(temp.replaceAll("[^'\\w+]\\(:\\w+,", "(\\?,").replaceAll("[^'\\w+]:\\w+,", "\\?,").replaceAll("[^'\\w+] :\\w+", "\\?"));
    }
}
