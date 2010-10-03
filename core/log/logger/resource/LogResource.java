package core.log.logger.resource;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 12. 11 ¿ÀÀü 10:37:37
 * Content  :
 * Usage    :
 */
public class LogResource {
    private boolean isSelect;
    private String convertedSql;
    private String elapsedTime;
    private String result;
    private Throwable t;

    public LogResource(boolean isSelect, Throwable throwable) {
        this.t=throwable;
        this.isSelect=isSelect;
    }

    public void setConvertedSql(String convertedSql) {
        this.convertedSql=convertedSql;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime=elapsedTime;
    }

    public void setResult(String result) {
        this.result=result;
    }

    public String getConvertedSql() {
        return this.convertedSql;
    }

    public String getElapsedTime() {
        return this.elapsedTime;
    }

    public String getResult() {
        return this.result;
    }

    public Throwable getThrowable() {
        return this.t;
    }

    public boolean isSelect() {
        return this.isSelect;
    }
}
