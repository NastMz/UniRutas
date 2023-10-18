package com.unirutas.core.factory.connection.implementation;

import com.unirutas.core.database.connection.interfaces.IConnectionPool;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.factory.connection.interfaces.IConnectionPoolFactory;
import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.utils.ClasspathScanner;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.logging.Logger;

public class NoSQLConnectionPoolFactory implements IConnectionPoolFactory {

    private static final Logger logger = Logger.getLogger(NoSQLConnectionPoolFactory.class.getName());

    @Override
    public IConnectionPool<?> createConnectionPool() {
        String databaseEngine = readDatabaseEngineFromProperties();

        return createNoSQLConnectionPool(databaseEngine);
    }

    private String readDatabaseEngineFromProperties() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new IOException("Database properties file not found.");
            }

            Properties props = new Properties();
            props.load(is);
            return props.getProperty("db.engine");
        } catch (IOException e) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error loading database configuration: " + e.getMessage();
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private IConnectionPool<?> createNoSQLConnectionPool(String databaseEngine) {
        try {
            NoSQLDatabaseEngine noSQLDatabaseEngine = NoSQLDatabaseEngine.valueOf(databaseEngine.toUpperCase());

            Class<?> connectionClass = ClasspathScanner.getClass(noSQLDatabaseEngine.getConnection());

            if (connectionClass != null) {
                Method getInstanceMethod = connectionClass.getMethod("getInstance");
                return (IConnectionPool<?>) getInstanceMethod.invoke(null);
            } else {
                throw new ClassNotFoundException("Connection pool class not found: " + noSQLDatabaseEngine.getConnection());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error creating NoSQL database connection pool: " + ex.getMessage();
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage, ex);
        } catch (IllegalArgumentException e) {
            StringBuilder supportedDatabases = new StringBuilder();
            for (SQLDatabaseEngine db : SQLDatabaseEngine.values()) {
                supportedDatabases.append(db.getEngine()).append(", ");
            }
            for (NoSQLDatabaseEngine db : NoSQLDatabaseEngine.values()) {
                supportedDatabases.append(db.getEngine()).append(", ");
            }

            // Throw an exception with a more descriptive message.
            throw new IllegalArgumentException("Invalid database engine: " + databaseEngine +
                    ". Supported engines are: " + supportedDatabases);
        }
    }
}
