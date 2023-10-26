package com.unirutas.core.factory.manager.implementation;

import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.database.manager.interfaces.IDatabaseManager;
import com.unirutas.core.factory.manager.interfaces.IDatabaseManagerFactory;
import com.unirutas.core.utils.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class NoSQLDatabaseManagerFactory implements IDatabaseManagerFactory {
    private static final Logger logger = LoggerFactory.getLogger(NoSQLDatabaseManagerFactory.class);

    @Override
    public IDatabaseManager createDatabaseManager() {
        String databaseEngine = readDatabaseEngineFromProperties();

        return createNoSQLDatabaseManager(databaseEngine);
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
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private IDatabaseManager createNoSQLDatabaseManager(String databaseEngine) {
        try {
            NoSQLDatabaseEngine noSQLDatabaseEngine = NoSQLDatabaseEngine.valueOf(databaseEngine.toUpperCase());

            Class<?> managerClass = ClasspathScanner.getClass(noSQLDatabaseEngine.getManager());

            if (managerClass != null) {
                Method getInstanceMethod = managerClass.getMethod("getInstance");
                return (IDatabaseManager) getInstanceMethod.invoke(null);
            } else {
                throw new ClassNotFoundException("Database manager class not found: " + noSQLDatabaseEngine.getManager());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error creating NoSQL database manager: " + ex.getMessage();
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage, ex);
        } catch (IllegalArgumentException e) {
            StringBuilder supportedEngines = new StringBuilder();
            for (SQLDatabaseEngine db : SQLDatabaseEngine.values()) {
                supportedEngines.append(db.getEngine()).append(", ");
            }
            for (NoSQLDatabaseEngine db : NoSQLDatabaseEngine.values()) {
                supportedEngines.append(db.getEngine()).append(", ");
            }

            // Throw an exception with a more descriptive message.
            throw new IllegalArgumentException("Invalid database engine: " + databaseEngine +
                    ". Supported engines are: " + supportedEngines.toString());
        }
    }
}
