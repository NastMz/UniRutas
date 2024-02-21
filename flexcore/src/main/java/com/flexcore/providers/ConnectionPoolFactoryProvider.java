package com.flexcore.providers;

import com.flexcore.database.enums.NoSQLDatabaseEngine;
import com.flexcore.database.enums.SQLDatabaseEngine;
import com.flexcore.factory.connection.implementation.NoSQLConnectionPoolFactory;
import com.flexcore.factory.connection.implementation.SQLConnectionPoolFactory;
import com.flexcore.factory.connection.interfaces.IConnectionPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectionPoolFactoryProvider {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPoolFactoryProvider.class);

    private static final Map<String, IConnectionPoolFactory> factoryMap = createFactoryMap();

    public static IConnectionPoolFactory getFactory() {
        checkDatabaseProperties();
        String databaseEngine = readDatabaseEngineFromProperties();
        IConnectionPoolFactory factory = factoryMap.get(databaseEngine);
        if (factory != null) {
            return factory;
        } else {
            // Collect the supported database engines from the enums
            String supportedDatabaseEngines = Stream.concat(
                    Arrays.stream(SQLDatabaseEngine.values()).map(SQLDatabaseEngine::getEngine),
                    Arrays.stream(NoSQLDatabaseEngine.values()).map(NoSQLDatabaseEngine::getEngine)
            ).collect(Collectors.joining(", "));

            // Log and throw an exception with a descriptive message.
            String errorMessage = "Unsupported database engine: " + databaseEngine +
                    ". Supported database engines are: " + supportedDatabaseEngines;
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private static String readDatabaseEngineFromProperties() {
        try (InputStream is = ConnectionPoolFactoryProvider.class.getClassLoader().getResourceAsStream("database.properties")) {
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

    private static Map<String, IConnectionPoolFactory> createFactoryMap() {
        Map<String, IConnectionPoolFactory> factoryMap = new HashMap<>();
        for (SQLDatabaseEngine type : SQLDatabaseEngine.values()) {
            factoryMap.put(type.getEngine(), new SQLConnectionPoolFactory());
        }
        for (NoSQLDatabaseEngine type : NoSQLDatabaseEngine.values()) {
            factoryMap.put(type.getEngine(), new NoSQLConnectionPoolFactory());
        }
        return factoryMap;
    }

    public static void checkDatabaseProperties() {
        Properties requiredProperties = new Properties();
        requiredProperties.setProperty("db.host", "");
        requiredProperties.setProperty("db.port", "");
        requiredProperties.setProperty("db.database", "");
        requiredProperties.setProperty("db.user", "");
        requiredProperties.setProperty("db.password", "");
        requiredProperties.setProperty("db.engine", "");

        try (InputStream is = ConnectionPoolFactoryProvider.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(is);

            for (String propertyName : requiredProperties.stringPropertyNames()) {
                String propertyValue = props.getProperty(propertyName);
                if (propertyValue == null || propertyValue.trim().isEmpty()) {
                    String errorMessage = "Missing config property: " + propertyName + " in database.properties";
                    logger.error(errorMessage);
                    throw new IllegalArgumentException(errorMessage);
                }
            }
        } catch (IOException e) {
            logger.error("Error reading database.properties file: " + e.getMessage());
            throw new RuntimeException("Error reading database.properties file: " + e.getMessage(), e);
        }
    }
}
