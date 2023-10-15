package com.unirutas.repository.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IDatabaseManager {
    void connect() throws SQLException;
    void disconnect() throws SQLException;
    ResultSet executeQuery(String sql, Object... parameters) throws SQLException;
    int executeUpdate(String sql, Object... parameters) throws SQLException;
    void startTransaction() throws SQLException;
    void commitTransaction() throws SQLException;
    void rollbackTransaction() throws SQLException;
}

