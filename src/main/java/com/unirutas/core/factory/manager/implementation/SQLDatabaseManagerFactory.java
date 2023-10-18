package com.unirutas.core.factory.manager.implementation;

import com.unirutas.core.database.manager.implementation.sql.implementation.SQLDatabaseManager;
import com.unirutas.core.database.manager.interfaces.IDatabaseManager;
import com.unirutas.core.factory.manager.interfaces.IDatabaseManagerFactory;

public class SQLDatabaseManagerFactory implements IDatabaseManagerFactory {
    @Override
    public IDatabaseManager createDatabaseManager() {
        return SQLDatabaseManager.getInstance();
    }
}
