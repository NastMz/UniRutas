package com.flexcore.factory.manager.implementation;

import com.flexcore.database.manager.implementation.sql.implementation.SQLDatabaseManager;
import com.flexcore.database.manager.interfaces.IDatabaseManager;
import com.flexcore.factory.manager.interfaces.IDatabaseManagerFactory;

public class SQLDatabaseManagerFactory implements IDatabaseManagerFactory {
    @Override
    public IDatabaseManager createDatabaseManager() {
        return SQLDatabaseManager.getInstance();
    }
}
