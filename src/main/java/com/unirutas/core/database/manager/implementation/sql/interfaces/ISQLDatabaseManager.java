package com.unirutas.core.database.manager.implementation.sql.interfaces;

import com.unirutas.core.database.manager.interfaces.IDatabaseManager;

import java.sql.ResultSet;

public interface ISQLDatabaseManager extends IDatabaseManager {
    ResultSet executeQuery(String sql, Object... parameters);
    int executeUpdate(String sql, Object... parameters);
    void startTransaction();
    void commitTransaction();
    void rollbackTransaction();
}
