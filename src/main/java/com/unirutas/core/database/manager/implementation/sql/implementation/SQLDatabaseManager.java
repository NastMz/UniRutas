package com.unirutas.core.database.manager.implementation.sql.implementation;

import com.unirutas.core.database.connection.implementation.sql.interfaces.ISQLConnectionPool;
import com.unirutas.core.database.manager.implementation.sql.interfaces.ISQLDatabaseManager;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class to manage database operations.
 */
public class SQLDatabaseManager implements ISQLDatabaseManager {
    private static SQLDatabaseManager instance;
    private Connection connection;
    private final ISQLConnectionPool connectionPool;
    private static final Logger logger = Logger.getLogger(SQLDatabaseManager.class.getName());

    private SQLDatabaseManager() {
        connectionPool = (ISQLConnectionPool) ConnectionPoolFactoryProvider.getFactory().createConnectionPool();
    }

    /**
     * Get the instance of the DatabaseManager.
     *
     * @return The DatabaseManager instance.
     */
    public static synchronized SQLDatabaseManager getInstance() {
        if (instance == null) {
            instance = new SQLDatabaseManager();
        }
        return instance;
    }

    /**
     * Connect to the database.
     */
    public void connect() {
        connection = connectionPool.getConnection();
    }

    /**
     * Disconnect from the database.
     */
    public void disconnect() {
        if (connection != null) {
            connectionPool.releaseConnection(connection);
        }
    }

    /**
     * Execute a query and return the result set.
     *
     * @param sql        The query to execute.
     * @param parameters The parameters to the query.
     * @return The result set.
     */
    public ResultSet executeQuery(String sql, Object... parameters) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }
            return preparedStatement.executeQuery();
        } catch (SQLException e) {
            handleException("Error executing the query: " + sql, e);
        }
        return null;
    }

    /**
     * Execute an update or insert.
     *
     * @param sql        The update or insert to execute.
     * @param parameters The parameters to the update or insert.
     * @return The number of rows affected.
     */
    public int executeUpdate(String sql, Object... parameters) {
        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(sql);

            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setObject(i + 1, parameters[i]);
            }

            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            handleException("Error executing the update: " + sql, e);
        }
        return 0;
    }

    /**
     * Start a transaction.
     *
     * @throws RuntimeException if the transaction cannot be started.
     */
    public void startTransaction() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            handleException("Error starting the transaction", e);
        }
    }

    /**
     * Commit a transaction.
     *
     * @throws RuntimeException if the transaction cannot be committed.
     */
    public void commitTransaction() {
        try {
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            handleException("Error committing the transaction", e);
        }
    }

    /**
     * Rollback a transaction.
     *
     * @throws RuntimeException if the transaction cannot be rolled back.
     */
    public void rollbackTransaction() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            handleException("Error rolling back the transaction", e);
        }
    }

    private void handleException(String message, Exception e) {
        logger.log(Level.SEVERE, message, e);
    }
}
