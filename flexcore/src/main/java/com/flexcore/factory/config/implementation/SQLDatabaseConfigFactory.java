package com.flexcore.factory.config.implementation;

import com.flexcore.database.config.implementation.sql.implementation.SQLDatabaseConfig;
import com.flexcore.database.config.interfaces.IDatabaseConfig;
import com.flexcore.factory.config.interfaces.IDatabaseConfigFactory;

public class SQLDatabaseConfigFactory implements IDatabaseConfigFactory {
    @Override
    public IDatabaseConfig createDatabaseConfig() {
        return SQLDatabaseConfig.getInstance();
    }
}