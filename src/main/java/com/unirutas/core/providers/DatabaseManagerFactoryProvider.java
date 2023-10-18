package com.unirutas.core.providers;

import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.factory.manager.implementation.NoSQLDatabaseManagerFactory;
import com.unirutas.core.factory.manager.implementation.SQLDatabaseManagerFactory;
import com.unirutas.core.factory.manager.interfaces.IDatabaseManagerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class DatabaseManagerFactoryProvider {
    private static final Logger logger = Logger.getLogger(DatabaseManagerFactoryProvider.class.getName());

    private static final Map<String, IDatabaseManagerFactory> factoryMap = createFactoryMap();

    public static IDatabaseManagerFactory getFactory() {
        String databaseEngine = readDatabaseEngineFromProperties();
        IDatabaseManagerFactory factory = factoryMap.get(databaseEngine);
        if (factory != null) {
            return factory;
        } else {
            // Collect the supported database engines from the properties file
            String supportedDatabaseEngines = String.join(", ", factoryMap.keySet());

            // Log and throw an exception with a descriptive message.
            String errorMessage = "Unsupported database engine: " + databaseEngine +
                    ". Supported database engines are: " + supportedDatabaseEngines;
            logger.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static String readDatabaseEngineFromProperties() {
        try (InputStream is = DatabaseManagerFactoryProvider.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new IOException("Database properties file not found.");
            }

            Properties props = new Properties();
            props.load(is);
            return props.getProperty("db.engine");
        } catch (IOException e) {
            // Log and rethrow the exception with a more informative message.
            String errorMessage = "Error reading database engine from database.properties: " + e.getMessage();
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private static Map<String, IDatabaseManagerFactory> createFactoryMap() {
        Map<String, IDatabaseManagerFactory> factoryMap = new HashMap<>();
        for (SQLDatabaseEngine type : SQLDatabaseEngine.values()) {
            factoryMap.put(type.getEngine(), new SQLDatabaseManagerFactory());
        }
        for (NoSQLDatabaseEngine type : NoSQLDatabaseEngine.values()) {
            factoryMap.put(type.getEngine(), new NoSQLDatabaseManagerFactory());
        }
        return factoryMap;
    }
}
