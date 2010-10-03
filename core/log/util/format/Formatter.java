package core.log.util.format;

import core.log.util.StringHelper;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

public class Formatter {
    private static final Set BEGIN_CLAUSES=new HashSet();
    private static final Set END_CLAUSES=new HashSet();
    private static final Set LOGICAL=new HashSet();
    private static final Set QUANTIFIERS=new HashSet();
    private static final Set DML=new HashSet();
    private static final Set MISC=new HashSet();

    static {
        BEGIN_CLAUSES.add("left");
        BEGIN_CLAUSES.add("right");
        BEGIN_CLAUSES.add("inner");
        BEGIN_CLAUSES.add("outer");
        BEGIN_CLAUSES.add("group");
        BEGIN_CLAUSES.add("order");
        END_CLAUSES.add("where");
        END_CLAUSES.add("set");
        END_CLAUSES.add("having");
        END_CLAUSES.add("join");
        END_CLAUSES.add("from");
        END_CLAUSES.add("by");
        END_CLAUSES.add("join");
        END_CLAUSES.add("into");
        END_CLAUSES.add("union");
        LOGICAL.add("and");
        LOGICAL.add("or");
        LOGICAL.add("when");
        LOGICAL.add("else");
        LOGICAL.add("end");
        QUANTIFIERS.add("in");
        QUANTIFIERS.add("all");
        QUANTIFIERS.add("exists");
        QUANTIFIERS.add("some");
        QUANTIFIERS.add("any");
        DML.add("insert");
        DML.add("update");
        DML.add("delete");
        MISC.add("select");
        MISC.add("on");
        //MISC.add("values");
    }

    String indentString="    ";
    String initial="\n    ";
    boolean beginLine=true;
    boolean afterBeginBeforeEnd=false;
    boolean afterByOrSetOrFromOrSelect=false;
    boolean afterValues=false;
    boolean afterOn=false;
    boolean afterBetween=false;
    boolean afterInsert=false;
    int inFunction=0;
    int parensSinceSelect=0;
    private LinkedList parenCounts=new LinkedList();
    private LinkedList afterByOrFromOrSelects=new LinkedList();
    int indent=1;
    StringBuffer result=new StringBuffer();
    StringTokenizer tokens;
    String lastToken;
    String token;
    String lcToken;

    public Formatter(String sql) {
        tokens=new StringTokenizer(
                sql,
                new StringBuffer().append("()+*/-=<>'`\"[],").append(StringHelper.WHITESPACE).toString(),
                true
        );
    }

    public Formatter setInitialString(String initial) {
        this.initial=initial;
        return this;
    }

    public Formatter setIndentString(String indent) {
        this.indentString=indent;
        return this;
    }

    public String format() {
        result.append(initial);
        while(tokens.hasMoreTokens()) {
            token=tokens.nextToken();
            lcToken=token.toLowerCase();
            if("'".equals(token)) {
                String t;
                do {
                    t=tokens.nextToken();
                    token+=t;
                }
                while(!"'".equals(t) && tokens.hasMoreTokens()); // cannot handle single quotes
            } else if("\"".equals(token)) {
                String t;
                do {
                    t=tokens.nextToken();
                    token+=t;
                }
                while(!"\"".equals(t));
            }
            if(afterByOrSetOrFromOrSelect && ",".equals(token)) {
                commaAfterByOrFromOrSelect();
            } else if(afterOn && ",".equals(token)) {
                commaAfterOn();
            } else if("(".equals(token)) {
                openParen();
            } else if(")".equals(token)) {
                closeParen();
            } else if(BEGIN_CLAUSES.contains(lcToken)) {
                beginNewClause();
            } else if(END_CLAUSES.contains(lcToken)) {
                endNewClause();
            } else if("select".equals(lcToken)) {
                select();
            } else if(DML.contains(lcToken)) {
                updateOrInsertOrDelete();
            } else if("values".equals(lcToken)) {
                values();
            } else if("on".equals(lcToken)) {
                on();
            } else if(afterBetween && lcToken.equals("and")) {
                misc();
                afterBetween=false;
            } else if(LOGICAL.contains(lcToken)) {
                logical();
            } else if(isWhitespace(token)) {
                white();
            } else {
                misc();
            }
            if(!isWhitespace(token))
                lastToken=lcToken;
        }
        return result.toString();
    }

    private void commaAfterOn() {
        out();
        indent--;
        newline();
        afterOn=false;
        afterByOrSetOrFromOrSelect=true;
    }

    private void commaAfterByOrFromOrSelect() {
        out();
        newline();
    }

    private void logical() {
        if("end".equals(lcToken))
            indent--;
        newline();
        out();
        beginLine=false;
    }

    private void on() {
        indent++;
        afterOn=true;
        newline();
        out();
        beginLine=false;
    }

    private void misc() {
        out();
        if("between".equals(lcToken)) {
            afterBetween=true;
        }
        if(afterInsert) {
            newline();
            afterInsert=false;
        } else {
            beginLine=false;
            if("case".equals(lcToken)) {
                indent++;
            }
        }
    }

    private void white() {
        if(!beginLine) {
            result.append(" ");
        }
    }

    private void updateOrInsertOrDelete() {
        out();
        indent++;
        beginLine=false;
        if("update".equals(lcToken))
            newline();
        if("insert".equals(lcToken))
            afterInsert=true;
    }

    private void select() {
        out();
        indent++;
        newline();
        parenCounts.addLast(new Integer(parensSinceSelect));
        afterByOrFromOrSelects.addLast(new Boolean(afterByOrSetOrFromOrSelect));
        parensSinceSelect=0;
        afterByOrSetOrFromOrSelect=true;
    }

    private void out() {
        result.append(token);
    }

    private void endNewClause() {
        if(!afterBeginBeforeEnd) {
            indent--;
            if(afterOn) {
                indent--;
                afterOn=false;
            }
            newline();
        }
        out();
        if(!"union".equals(lcToken))
            indent++;
        newline();
        afterBeginBeforeEnd=false;
        afterByOrSetOrFromOrSelect="by".equals(lcToken)
                                   || "set".equals(lcToken)
                                   || "from".equals(lcToken);
    }

    private void beginNewClause() {
        if(!afterBeginBeforeEnd) {
            if(afterOn) {
                indent--;
                afterOn=false;
            }
            indent--;
            newline();
        }
        out();
        beginLine=false;
        afterBeginBeforeEnd=true;
    }

    private void values() {
        indent--;
        newline();
        out();
        indent++;
        newline();
        afterValues=true;
    }

    private void closeParen() {
        parensSinceSelect--;
        if(parensSinceSelect<0) {
            indent--;
            parensSinceSelect=((Integer) parenCounts.removeLast()).intValue();
            afterByOrSetOrFromOrSelect=((Boolean) afterByOrFromOrSelects.removeLast()).booleanValue();
        }
        if(inFunction>0) {
            inFunction--;
            out();
        } else {
            if(!afterByOrSetOrFromOrSelect) {
                indent--;
                newline();
            }
            out();
        }
        beginLine=false;
    }

    private void openParen() {
        if(isFunctionName(lastToken) || inFunction>0) {
            inFunction++;
        }
        beginLine=false;
        if(inFunction>0) {
            out();
        } else {
            out();
            if(!afterByOrSetOrFromOrSelect) {
                indent++;
                newline();
                beginLine=true;
            }
        }
        parensSinceSelect++;
    }

    private static boolean isFunctionName(String tok) {
        final char begin=tok.charAt(0);
        final boolean isIdentifier=Character.isJavaIdentifierStart(begin) || '"' == begin;
        return isIdentifier &&
               !LOGICAL.contains(tok) &&
               !END_CLAUSES.contains(tok) &&
               !QUANTIFIERS.contains(tok) &&
               !DML.contains(tok) &&
               !MISC.contains(tok);
    }

    private static boolean isWhitespace(String token) {
        return StringHelper.WHITESPACE.indexOf(token)>=0;
    }

    private void newline() {
        result.append("\n");
        for(int i=0; i<indent; i++) {
            result.append(indentString);
        }
        beginLine=true;
    }

    public static void main(String[] args) {
        String query=
                " SELECT T.PLANID, T.PLANGBN, DECODE(T.PLANID, 'ONLINE', '', FN_COM_GETCODENAME(T.PLANGBN)) PLANGBNM, T.SECFLAG, T.SECGBN, T.SECAREA, T.OPENFLAG, T.REPEATKIND, T.RSDATE, T.REDATE, T.MONTH, T.WEEK, T.WEEKNAME, T.DAY, T.AUTOFLAG, T.INID, T.INNAME, T.EXEFLAG_LOGINID, T.REPEATID, T.PLANDATE, T.TITLE, T.PLACE, T.PLAN_ORDER, T.STARTTIME, T.ENDTIME, '' CONTENTS, T.UNCHFLAG, T.DELFLAG, T.ATTACHID, T.PERSONID, T.PERSONNM, T.EXEFLAG, T.OWNER_DEPTID, 'Y' IS_OPEN, T.RESULTID, T.REFLAG, T.DEPTID, T.DEPTNAME, T.OWNERID, T.OWNERNAME, T.DUTYNAME, T.POSITIONID, T.GRADEID, T.RESULT_TITLE, '' RESULT_CONTENTS, T.RESULTDATE, '' ETC, T.RESULT_ATTACHID, T.RESULT_INDT, T.RNUM, T.TASKCARDID, JT.CARDNAME TASKCARDNM, JT.STATUS TASKCARD_STATUS, T.PJTCARDID, JP.CARDNAME PJTCARDNM, JP.STATUS PJTCARD_STATUS, T.ORGCARDID, JO.CARDNAME ORGCARDNM, JO.STATUS ORGCARD_STATUS, JT.DELFLAG TASKCARD_DELFLAG, JP.DELFLAG PJTCARD_DELFLAG, JO.DELFLAG ORGCARD_DELFLAG FROM ( SELECT A.PLANID, A.PLANGBN, A.SECFLAG, A.SECGBN, A.SECAREA, A.OPENFLAG, A.REPEATKIND, A.RSDATE, A.REDATE, A.MONTH, A.WEEK, A.WEEKNAME, A.DAY, A.AUTOFLAG, A.INID, A.INNAME, A.EXEFLAG_LOGINID, A.REPEATID, A.PLANDATE, A.TITLE, A.PLACE, A.PLAN_ORDER, A.STARTTIME, A.ENDTIME, A.UNCHFLAG, A.DELFLAG, A.ATTACHID, A.PERSONID, A.PERSONNM, A.EXEFLAG, A.OWNER_DEPTID, A.RESULTID, A.REFLAG, A.DEPTID, A.DEPTNAME, A.OWNERID, A.OWNERNAME, A.DUTYNAME, A.POSITIONID, A.GRADEID, A.RESULT_TITLE, A.RESULTDATE, A.RESULT_ATTACHID, A.RESULT_INDT, IT.PJTCARDID TASKCARDID, IP.PJTCARDID PJTCARDID, IO.PJTCARDID ORGCARDID, ROW_NUMBER() OVER( ORDER BY A.PLANDATE, A.STARTTIME, A.ENDTIME, A.PLANID, A.REPEATID, A.RESULTDATE, A.RESULT_INDT, A.RESULTID) AS RNUM FROM ( SELECT A.PLANID, A.PLANGBN, A.SECFLAG, A.SECGBN, A.SECAREA, A.OPENFLAG, A.REPEATKIND, A.RSDATE, A.REDATE, A.MONTH , A.WEEK, A.WEEKNAME, A.DAY, A.AUTOFLAG, A.INID, A.INNAME, C.EXEFLAG EXEFLAG_LOGINID, B.REPEATID, B.PLANDATE, B.TITLE, B.PLACE, DECODE( B.UNCHFLAG , 'Y', 1, 2 ) PLAN_ORDER, B.STARTTIME, B.ENDTIME, B.UNCHFLAG, B.DELFLAG, B.ATTACHID, C.PERSONID, C.OWNERNAME PERSONNM, C.EXEFLAG, C.DEPTID OWNER_DEPTID, NVL(D.RESULTID, '0') RESULTID, D.REFLAG, D.DEPTID, D.DEPTNAME, D.OWNERID, D.OWNERNAME, D.DUTYNAME, D.POSITIONID, D.GRADEID, D.TITLE RESULT_TITLE, D.RESULTDATE, D.ATTACHID RESULT_ATTACHID, D.INDT RESULT_INDT FROM BMS_TASK_PERPLAN_BASIC A, BMS_TASK_PERPLAN_DETAIL B, BMS_TASK_PEROWNER C, BMS_TASK_PERRESULT D WHERE C.PERSONID = 'b7594604abf340288755060412104305' /**P*/ AND C.REDATE >= '20080411' /**P*/ AND C.RSDATE <= '20080411' /**P*/ AND C.DELFLAG = 'N' AND A.PLANID = C.PLANID AND A.DELFLAG = 'N' AND A.SECFLAG IN ( 'N', 'M' ) AND A.REPEATKIND NOT IN ( 'DY' ) AND ( C.EXEFLAG = 'Y' OR A.OPENFLAG = 'Y' ) AND B.PLANID = C.PLANID AND B.PLANDATE < C.INVALIDDT AND B.PLANDATE BETWEEN '20080411' /**P*/ AND '20080411' /**P*/ AND B.DELFLAG = 'N' AND D.REPEATID(+) = B.REPEATID AND D.DELFLAG (+) = 'N' UNION ALL SELECT 'ONLINE' PLANID, '' PLANGBN, '' SECFLAG, '' SECGBN, '' SECAREA, '' OPENFLAG, 'N' REPEATKIND, '' RSDATE, '' REDATE, '' MONTH, '' WEEK, '' WEEKNAME, '' DAY, '' AUTOFLAG, '' INID, '' INNAME, '' EXEFLAG_LOGINID, A.CONFTYPEID, B.OPENDATE, A.CONFTYPENAME, B.CONFLOCATION, 2, B.OPENSTIME, B.OPENETIME, 'N', 'N', null, '', '', 'Y', '', NVL(B.CONFID, '0') RESULTID, 'A' REFLAG, B.MANAGEDEPTID DEPTID, B.MANAGEDEPTNAME DEPTNAME, B.CHARGEUSERID OWNERID, B.CHARGEUSERNAME OWNERNAME, B.CHARGEDUTYNAME DUTYNAME, B.CHARGEPOSITIONID POSITIONID, B.CHARGEGRADEID GRADEID, B.CONFROUND||'|'||B.CONFNAME RESULT_TITLE, B.OPENDATE RESULTDATE, '', B.INDT FROM BMS_AGD_ACONFTYPE A, BMS_AGD_CONF B WHERE A.CONFTYPEID = B.CONFTYPEID AND B.CONFID IN ( SELECT ASUB.CONFID FROM BMS_AGD_CONF ASUB, BMS_AGD_AGENDA BSUB WHERE ASUB.CONFID = BSUB.CONFID AND BSUB.CONFID > ' ' AND BSUB.ISCANCEL = 'N' AND BSUB.ISDUMMY = 'N' AND ( ASUB.CHARGEUSERID = 'b7594604abf340288755060412104305' /**P*/ OR EXISTS ( SELECT 'X' FROM BMS_AGD_AGENDA_ATTE CSUB WHERE BSUB.AGDID = CSUB.AGDID AND CSUB.USERID = 'b7594604abf340288755060412104305' /**P*/ ) ) AND ASUB.OPENDATE BETWEEN '20080411' /**P*/ AND '20080411' /**P*/ ) ) A, BMS_TASK_CARD_HIS IT, BMS_TASK_CARD_HIS IP, BMS_TASK_CARD_HIS IO WHERE A.PLANID = IT.PLANID (+) AND IT.CARDTYPE (+) = 'T' AND IT.ORIGINFLAG (+) = 'Y' AND A.PLANID = IP.PLANID (+) AND IP.CARDTYPE (+) = 'P' AND IP.ORIGINFLAG (+) = 'Y' AND A.PLANID = IO.PLANID (+) AND IO.CARDTYPE (+) = 'O' AND IO.ORIGINFLAG (+) = 'Y' ) T, BMS_PJT_PJTCARD JT, BMS_PJT_PJTCARD JP, BMS_PJT_PJTCARD JO WHERE T.TASKCARDID = JT.PJTCARDID (+) AND T.PJTCARDID = JP.PJTCARDID (+) AND T.ORGCARDID = JO.PJTCARDID (+) AND RNUM BETWEEN 1 /**P*/ AND 10 /**P*/";
        System.out.println(
                new Formatter(query).format());
    }
}
