package com.unirutas.core.factory.config.implementation;

import com.unirutas.core.database.config.implementation.sql.implementation.SQLDatabaseConfig;
import com.unirutas.core.database.config.interfaces.IDatabaseConfig;
import com.unirutas.core.factory.config.interfaces.IDatabaseConfigFactory;

public class SQLDatabaseConfigFactory implements IDatabaseConfigFactory {
    @Override
    public IDatabaseConfig createDatabaseConfig() {
        return SQLDatabaseConfig.getInstance();
    }
}