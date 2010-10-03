package core.log.impl;

import core.log.aop.handler.DaoInfo;
import core.log.aop.reflection.ProfilingFactory;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;

/**
 * Author   : song insup
 * email    : insup74@empal.com
 * home page: http://log4sql.sourceforge.net
 * version  : 1.0
 * Date Time: 2007. 10. 17 ���� 4:23:29
 * Content  :
 * Usage    :
 */
public class ConnectionLoggable implements Connection {
    private Connection connection;

    public Statement createStatement() throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection));
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, resultSetType, resultSetConcurrency));
    }

    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, sql));
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, sql, autoGeneratedKeys));
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, sql, resultSetType, resultSetConcurrency));
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, sql, resultSetType, resultSetConcurrency, resultSetHoldability));
    }

    public PreparedStatement prepareStatement(String sql, int columnIndexes[]) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, sql, columnIndexes));
    }

    public PreparedStatement prepareStatement(String sql, String columnNames[]) throws SQLException {
        return (PreparedStatementLoggableInterface) ProfilingFactory.sqlTraceProfiling(
                new DaoInfo(connection, sql, columnNames));
    }

    public ConnectionLoggable(Connection connection) {
        this.connection=connection;
    }

    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    public void close() throws SQLException {
        connection.close();
    }

    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    public void setHoldability(int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    public void setTransactionIsolation(int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    public void setCatalog(String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    public Map getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    public void setTypeMap(Map map) throws SQLException {
        connection.setTypeMap(map);
    }

    public String nativeSQL(String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        return connection.setSavepoint(name);
    }
}