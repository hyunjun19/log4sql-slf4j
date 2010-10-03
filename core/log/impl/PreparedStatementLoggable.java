package core.log.impl;

import core.log.logger.SL;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 9. 12 ���� 2:15:10
 * Content  :
 * Usage    :
 */
public class PreparedStatementLoggable implements PreparedStatementLoggableInterface {
    private PreparedStatement preparedStatement;
    private Statement statement;
    private List parameterValues=new ArrayList();

    public PreparedStatementLoggable() {
    }

    //for statement
    public PreparedStatementLoggable(Connection connection) throws SQLException {
        this.statement=connection.createStatement();
    }

    //for statement
    public PreparedStatementLoggable(Connection connection, int resultSetType, int resultSetConcurrency) throws SQLException {
        this.statement=connection.createStatement(resultSetType, resultSetConcurrency);
    }

    //for statement
    public PreparedStatementLoggable(Connection connection, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        this.statement=connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    //for preparedstatement
    public PreparedStatementLoggable(Connection connection, String sql) throws SQLException {
        preparedStatement=connection.prepareStatement(sql);
        parameterValues=new ArrayList();
    }

    //for preparedstatement
    public PreparedStatementLoggable(Connection connection, String sql, String columnNames[]) throws SQLException {
        preparedStatement=connection.prepareStatement(sql, columnNames);
        parameterValues=new ArrayList();
    }

    //for preparedstatement
    public PreparedStatementLoggable(Connection connection, String sql, int autoGeneratedKeys) throws SQLException {
        preparedStatement=connection.prepareStatement(sql, autoGeneratedKeys);
        parameterValues=new ArrayList();
    }

    //for preparedstatement
    public PreparedStatementLoggable(Connection connection, String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        preparedStatement=connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        parameterValues=new ArrayList();
    }

    //for preparedstatement
    public PreparedStatementLoggable(Connection connection, String sql, int columnIndexes[]) throws SQLException {
        preparedStatement=connection.prepareStatement(sql, columnIndexes);
        parameterValues=new ArrayList();
    }

    //for preparedstatement
    public PreparedStatementLoggable(Connection connection, String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        preparedStatement=connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        parameterValues=new ArrayList();
    }

    public void setLong(int parameterIndex, long x) throws java.sql.SQLException {
        preparedStatement.setLong(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Long(x));
    }

    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        preparedStatement.setBoolean(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Boolean(x));
    }

    public int executeUpdate() throws SQLException {
        return preparedStatement.executeUpdate();
    }

    public void addBatch() throws SQLException {
        preparedStatement.addBatch();
    }

    public void clearParameters() throws SQLException {
        preparedStatement.clearParameters();
        parameterValues=null;
        parameterValues=new ArrayList();
    }

    public boolean execute() throws SQLException {
        return preparedStatement.execute();
    }

    public void setByte(int parameterIndex, byte x) throws SQLException {
        preparedStatement.setByte(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Byte(x));
    }

    public void setShort(int parameterIndex, short x) throws SQLException {
        preparedStatement.setShort(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Short(x));
    }

    public void setInt(int parameterIndex, int x) throws SQLException {
        preparedStatement.setInt(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Integer(x));
    }

    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        preparedStatement.setNull(parameterIndex, sqlType);
        saveQueryParamValue(parameterIndex, null);
    }

    public void setFloat(int parameterIndex, float x) throws SQLException {
        preparedStatement.setFloat(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Float(x));
    }

    public void setDouble(int parameterIndex, double x) throws SQLException {
        preparedStatement.setDouble(parameterIndex, x);
        saveQueryParamValue(parameterIndex, new Double(x));
    }

    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        preparedStatement.setBigDecimal(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setURL(int parameterIndex, URL x) throws SQLException {
        preparedStatement.setURL(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setArray(int i, Array x) throws SQLException {
        preparedStatement.setArray(i, x);
    }

    public void setBlob(int i, Blob x) throws SQLException {
        preparedStatement.setBlob(i, x);
    }

    public void setClob(int i, Clob x) throws SQLException {
        preparedStatement.setClob(i, x);
    }

    public void setString(int parameterIndex, String x) throws SQLException {
        preparedStatement.setString(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setBytes(int parameterIndex, byte x[]) throws SQLException {
        preparedStatement.setBytes(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setDate(int parameterIndex, java.sql.Date x) throws SQLException {
        preparedStatement.setDate(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public ParameterMetaData getParameterMetaData() throws SQLException {
        return preparedStatement.getParameterMetaData();
    }

    public void setRef(int i, Ref x) throws SQLException {
        preparedStatement.setRef(i, x);
    }

    public void setTime(int parameterIndex, java.sql.Time x) throws SQLException {
        preparedStatement.setTime(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setTimestamp(int parameterIndex, java.sql.Timestamp x) throws SQLException {
        preparedStatement.setTimestamp(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setDate(int parameterIndex, java.sql.Date x, Calendar cal) throws SQLException {
        preparedStatement.setDate(parameterIndex, x, cal);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        preparedStatement.setTime(parameterIndex, x, cal);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        preparedStatement.setTimestamp(parameterIndex, x, cal);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setAsciiStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
        preparedStatement.setAsciiStream(parameterIndex, x, length);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setUnicodeStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
        preparedStatement.setUnicodeStream(parameterIndex, x, length);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        String originSql=null;
        try {
            int ch=0;
            StringWriter sw=new StringWriter();
            while((ch=reader.read()) != -1)
                sw.write((char) ch);
            originSql=sw.toString();
        } catch(IOException e) {
            SL.getInstance().log(e);
        }
        preparedStatement.setCharacterStream(parameterIndex, new StringReader(originSql), length);
        saveQueryParamValue(parameterIndex, originSql);
    }

    public void setBinaryStream(int parameterIndex, java.io.InputStream x, int length) throws SQLException {
        preparedStatement.setBinaryStream(parameterIndex, x, length);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType, int scale) throws SQLException {
        preparedStatement.setObject(parameterIndex, x, targetSqlType, scale);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setNull(int paramIndex, int sqlType, String typeName) throws SQLException {
        preparedStatement.setNull(paramIndex, sqlType, typeName);
    }

    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        preparedStatement.setObject(parameterIndex, x, targetSqlType);
        saveQueryParamValue(parameterIndex, x);
    }

    public void setObject(int parameterIndex, Object x) throws SQLException {
        preparedStatement.setObject(parameterIndex, x);
        saveQueryParamValue(parameterIndex, x);
    }

    public ResultSet executeQuery() throws java.sql.SQLException {
        return preparedStatement.executeQuery();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return preparedStatement.getMetaData();
    }

    public int getFetchDirection() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getFetchDirection();
        else if(statement != null)
            return statement.getFetchDirection();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getFetchSize() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getFetchSize();
        else if(statement != null)
            return statement.getFetchSize();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getMaxFieldSize() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getMaxFieldSize();
        else if(statement != null)
            return statement.getMaxFieldSize();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getMaxRows() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getMaxRows();
        else if(statement != null)
            return statement.getMaxRows();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getQueryTimeout() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getQueryTimeout();
        else if(statement != null)
            return statement.getQueryTimeout();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getResultSetConcurrency() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getResultSetConcurrency();
        else if(statement != null)
            return statement.getResultSetConcurrency();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getResultSetHoldability() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getResultSetHoldability();
        else if(statement != null)
            return statement.getResultSetHoldability();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getResultSetType() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getResultSetType();
        else if(statement != null)
            return statement.getResultSetType();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int getUpdateCount() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getUpdateCount();
        else if(statement != null)
            return statement.getUpdateCount();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public void cancel() throws SQLException {
        if(preparedStatement != null)
            preparedStatement.cancel();
        else if(statement != null)
            statement.cancel();
    }

    public void clearBatch() throws SQLException {
        if(preparedStatement != null)
            preparedStatement.clearBatch();
        else if(statement != null)
            statement.clearBatch();
    }

    public void clearWarnings() throws SQLException {
        if(preparedStatement != null)
            preparedStatement.clearWarnings();
        else if(statement != null)
            statement.clearWarnings();
    }

    public void close() throws SQLException {
        if(preparedStatement != null)
            preparedStatement.close();
        else if(statement != null)
            statement.close();
    }

    public boolean getMoreResults() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getMoreResults();
        else if(statement != null)
            return statement.getMoreResults();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int[] executeBatch() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.executeBatch();
        else if(statement != null)
            return statement.executeBatch();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public void setFetchDirection(int direction) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setFetchDirection(direction);
        else if(statement != null)
            statement.setFetchDirection(direction);
    }

    public void setFetchSize(int rows) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setFetchSize(rows);
        else if(statement != null)
            statement.setFetchSize(rows);
    }

    public void setMaxFieldSize(int max) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setMaxFieldSize(max);
        else if(statement != null)
            statement.setMaxFieldSize(max);
    }

    public void setMaxRows(int max) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setMaxRows(max);
        else if(statement != null)
            statement.setMaxRows(max);
    }

    public void setQueryTimeout(int seconds) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setQueryTimeout(seconds);
        else if(statement != null)
            statement.setQueryTimeout(seconds);
    }

    public boolean getMoreResults(int current) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getMoreResults(current);
        else if(statement != null)
            return statement.getMoreResults(current);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public void setEscapeProcessing(boolean enable) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setEscapeProcessing(enable);
        else if(statement != null)
            statement.setEscapeProcessing(enable);
    }



    public void addBatch(String sql) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.addBatch(sql);
        else if(statement != null)
            statement.addBatch(sql);
    }

    public void setCursorName(String name) throws SQLException {
        if(preparedStatement != null)
            preparedStatement.setCursorName(name);
        else if(statement != null)
            statement.setCursorName(name);
    }





    public Connection getConnection() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getConnection();
        else if(statement != null)
            return statement.getConnection();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public ResultSet getGeneratedKeys() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getGeneratedKeys();
        else if(statement != null)
            return statement.getGeneratedKeys();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public ResultSet getResultSet() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getResultSet();
        else if(statement != null)
            return statement.getResultSet();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public SQLWarning getWarnings() throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.getWarnings();
        else if(statement != null)
            return statement.getWarnings();
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public boolean execute(String sql) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.execute(sql);
        else if(statement != null)
            return statement.execute(sql);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.execute(sql, autoGeneratedKeys);
        else if(statement != null)
            return statement.execute(sql, autoGeneratedKeys);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public boolean execute(String sql, int columnIndexes[]) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.execute(sql, columnIndexes);
        else if(statement != null)
            return statement.execute(sql, columnIndexes);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.executeUpdate(sql, autoGeneratedKeys);
        else if(statement != null)
            return statement.executeUpdate(sql, autoGeneratedKeys);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }



    public int executeUpdate(String sql, int columnIndexes[]) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.executeUpdate(sql, columnIndexes);
        else if(statement != null)
            return statement.executeUpdate(sql, columnIndexes);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int executeUpdate(String sql) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.executeUpdate(sql);
        else if(statement != null)
            return statement.executeUpdate(sql);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public int executeUpdate(String sql, String columnNames[]) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.executeUpdate(sql, columnNames);
        else if(statement != null)
            return statement.executeUpdate(sql, columnNames);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public boolean execute(String sql, String columnNames[]) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.execute(sql, columnNames);
        else if(statement != null)
            return statement.execute(sql, columnNames);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    public ResultSet executeQuery(String sql) throws SQLException {
        if(preparedStatement != null)
            return preparedStatement.executeQuery(sql);
        else if(statement != null)
            return statement.executeQuery(sql);
        else
            throw new SQLException("Confuse!!! Preparedstatment Or Statement?");
    }

    private void saveQueryParamValue(int position, Object obj) {
//		parameterValues.add(position-1, obj);//origin
        parameterValues.add(obj);
    }

    public List getParameterValues() {
        return this.parameterValues;
    }

    public void setParameterValuesInitailize() {
        this.parameterValues=new ArrayList();
    }
}