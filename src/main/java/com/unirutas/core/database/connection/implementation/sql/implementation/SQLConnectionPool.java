package com.unirutas.core.database.connection.implementation.sql.implementation;

import com.unirutas.core.database.config.implementation.sql.interfaces.ISQLDatabaseConfig;
import com.unirutas.core.database.connection.implementation.sql.interfaces.ISQLConnectionPool;
import com.unirutas.core.database.connection.interfaces.IConnectionPool;
import com.unirutas.core.providers.DatabaseConfigFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLConnectionPool implements ISQLConnectionPool {

    private static final Logger logger = LoggerFactory.getLogger(SQLConnectionPool.class);
    private static SQLConnectionPool instance;
    private final String url;
    private final String user;
    private final String password;
    private final int maxPoolSize; // Maximum pool size
    private final long connectionTimeout; // Connection timeout in milliseconds
    private final List<Connection> availableConnections = new ArrayList<>();
    private final List<Connection> usedConnections = new ArrayList<>();

    private SQLConnectionPool() {
        logger.info("Initializing SQL connection pool");
        ISQLDatabaseConfig config = (ISQLDatabaseConfig) DatabaseConfigFactoryProvider.getFactory().createDatabaseConfig();
        this.url = config.getUrl();
        this.user = config.getUser();
        this.password = config.getPassword();
        this.maxPoolSize = 20;
        this.connectionTimeout = 30000; // 30 seconds
        try {
            Class.forName(config.getDriver());
        } catch (ClassNotFoundException e) {
            handleException("Error loading the database driver", e);
        }
        logger.info("SQL connection pool initialized");
    }

    public static synchronized IConnectionPool<Connection> getInstance() {
        if (instance == null) {
            instance = new SQLConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        Connection connection = null;
        if (availableConnections.isEmpty() && usedConnections.size() < maxPoolSize) {
            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                handleException("Error connecting to the database", e);
            }
        } else {
            connection = waitForConnection();
        }
        usedConnections.add(connection);
        return connection;
    }

    private synchronized Connection waitForConnection() {
        long startTime = System.currentTimeMillis();
        while (availableConnections.isEmpty() && (System.currentTimeMillis() - startTime) < connectionTimeout) {
            try {
                wait(100);
            } catch (InterruptedException e) {
                // Handle interruption
                logger.warn("Thread was interrupted while waiting for a connection to be available to perform the operation");
                Thread.currentThread().interrupt(); // Restore the interrupted status
                return null;
            }
        }
        if (availableConnections.isEmpty()) {
            throw new RuntimeException("Connection timeout. No available connections to perform the operation.");
        }
        return availableConnections.remove(availableConnections.size() - 1);
    }

    public synchronized void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        availableConnections.add(connection);
        notifyAll(); // Notify waiting threads that a connection is available
    }

    public synchronized void closeAllConnections() {
        closeConnections(availableConnections);
        closeConnections(usedConnections);
    }

    private void closeConnections(List<Connection> connections) {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error( "Error closing connection", e);
            }
        }
        connections.clear();
    }

    private void handleException(String message, Exception e) {
        logger.error(message);
        throw new RuntimeException(message, e);
    }
}
