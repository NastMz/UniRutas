package com.unirutas.core.database.manager.implementation.sql.implementation;

import com.unirutas.core.database.connection.implementation.sql.interfaces.ISQLConnectionPool;
import com.unirutas.core.database.manager.implementation.sql.interfaces.ISQLDatabaseManager;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * A class to manage database operations.
 */
public class SQLDatabaseManager implements ISQLDatabaseManager {
    private static SQLDatabaseManager instance;
    private Connection connection;
    private final ISQLConnectionPool connectionPool;
    private static final Logger logger = LoggerFactory.getLogger(SQLDatabaseManager.class);

    private SQLDatabaseManager() {
        connectionPool = (ISQLConnectionPool) ConnectionPoolFactoryProvider.getFactory().createConnectionPool();
    }

    /**
     * Get the singleton instance of the database manager.
     *
     * @return The singleton instance of the database manager.
     */
    public static SQLDatabaseManager getInstance() {
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
     * Get the date from the database engine.
     * <p>
     *     We use the CURRENT_DATE function from the database engine.
     *     A function that is an ANSI SQL standard. It is supported by
     *     most database engines.
     */
    public void getDate() {
        Statement statement = null;
        try {
            connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT CURRENT_DATE");

            if (resultSet.next()) {
                String message = "Database Engine Date: " + resultSet.getString(1);
                logger.info(message);
            } else {
                logger.warn("No date found in database engine");
            }

            disconnect();
        } catch (Exception e) {
            handleException("Error obtaining date from database engine", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    handleException("Error closing statement", e);
                }
            }

            disconnect();
        }
    }

    /**
     * Get the hour from the database engine.
     * <p>
     *     We use the CURRENT_TIME function from the database engine.
     *     A function that is an ANSI SQL standard. It is supported by
     *     most database engines.
     */
    public void getHour() {
        Statement statement = null;
        try {
            connect();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT CURRENT_TIME");

            if (resultSet.next()) {
                String message = "Database Engine Hour: " + resultSet.getString(1);
                logger.info(message);
            } else {
                logger.warn("No hour found in database engine");
            }

            disconnect();
        } catch (Exception e) {
            handleException("Error obtaining hour from database engine", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    handleException("Error closing statement", e);
                }
            }
            disconnect();
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
        logger.error(message, e);
    }
}
