package com.unirutas.core.providers;

import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.factory.builder.implementation.NoSQLCustomQueryBuilderFactory;
import com.unirutas.core.factory.builder.implementation.SQLCustomQueryBuilderFactory;
import com.unirutas.core.factory.builder.interfaces.ICustomQueryBuilderFactory;
import com.unirutas.core.factory.config.implementation.NoSQLDatabaseConfigFactory;
import com.unirutas.core.factory.config.implementation.SQLDatabaseConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CustomQueryBuilderProvider {
    private static final Logger logger = LoggerFactory.getLogger(CustomQueryBuilderProvider.class);

    private static final Map<String, ICustomQueryBuilderFactory> factoryMap = createFactoryMap();

    public static ICustomQueryBuilderFactory getFactory() {
        String databaseEngine = readDatabaseEngineFromProperties();
        ICustomQueryBuilderFactory factory = factoryMap.get(databaseEngine);
        if (factory != null) {
            return factory;
        } else {
            // Collect the supported database engines from the properties file
            String supportedDatabaseEngines = String.join(", ", factoryMap.keySet());

            // Log and throw an exception with a descriptive message.
            String errorMessage = "Unsupported database engine: " + databaseEngine +
                    ". Supported database engines are: " + supportedDatabaseEngines;
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static String readDatabaseEngineFromProperties() {
        try (InputStream is = DatabaseConfigFactoryProvider.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new IOException("Database properties file not found.");
            }

            Properties props = new Properties();
            props.load(is);
            return props.getProperty("db.engine");
        } catch (IOException e) {
            // Log and rethrow the exception with a more informative message.
            String errorMessage = "Error reading database engine from database.properties: " + e.getMessage();
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private static Map<String, ICustomQueryBuilderFactory> createFactoryMap() {
        Map<String, ICustomQueryBuilderFactory> factoryMap = new HashMap<>();
        for (SQLDatabaseEngine type : SQLDatabaseEngine.values()) {
            factoryMap.put(type.getEngine(), new SQLCustomQueryBuilderFactory());
        }
        for (NoSQLDatabaseEngine type : NoSQLDatabaseEngine.values()) {
            factoryMap.put(type.getEngine(), new NoSQLCustomQueryBuilderFactory());
        }
        return factoryMap;
    }
}
