package com.unirutas.repository;

import com.unirutas.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static ConnectionPool instance;
    private final String url;
    private final String user;
    private final String password;
    private final List<Connection> availableConnections = new ArrayList<>();
    private final List<Connection> usedConnections = new ArrayList<>();

    private ConnectionPool() {
        DatabaseConfig config = DatabaseConfig.getInstance();
        this.url = config.getUrl();
        this.user = config.getUser();
        this.password = config.getPassword();

        try {
            Class.forName(config.getDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static synchronized ConnectionPool getInstance() {
        if (instance == null) {
            instance = new ConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            Connection connection = DriverManager.getConnection(url, user, password);
            usedConnections.add(connection);
            return connection;
        } else {
            Connection connection = availableConnections.remove(availableConnections.size() - 1);
            usedConnections.add(connection);
            return connection;
        }
    }

    public synchronized void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        availableConnections.add(connection);
    }

    public synchronized void closeAllConnections() throws SQLException {
        for (Connection connection : availableConnections) {
            connection.close();
        }
        availableConnections.clear();

        for (Connection connection : usedConnections) {
            connection.close();
        }
        usedConnections.clear();
    }
}
