package com.unirutas.repository.implementation;

import com.unirutas.core.ConnectionPool;
import com.unirutas.repository.interfaces.IDatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager implements IDatabaseManager {
    private static DatabaseManager instance;

    private Connection connection;
    private final ConnectionPool connectionPool;

    private DatabaseManager() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public void connect() throws SQLException {
        connection = connectionPool.getConnection();
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
            connectionPool.releaseConnection(connection);
        }
    }

    public ResultSet executeQuery(String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }

        return preparedStatement.executeQuery();
    }

    public int executeUpdate(String sql, Object... parameters) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < parameters.length; i++) {
            preparedStatement.setObject(i + 1, parameters[i]);
        }

        return preparedStatement.executeUpdate();
    }

    public void startTransaction() throws SQLException {
        connection.setAutoCommit(false);
    }

    public void commitTransaction() throws SQLException {
        connection.commit();
        connection.setAutoCommit(true);
    }

    public void rollbackTransaction() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true);
    }
}
