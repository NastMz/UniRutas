package com.unirutas.core.factory.config.implementation;

import com.unirutas.core.database.config.interfaces.IDatabaseConfig;
import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.factory.config.interfaces.IDatabaseConfigFactory;
import com.unirutas.core.utils.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

public class NoSQLDatabaseConfigFactory implements IDatabaseConfigFactory {
    private static final Logger logger = LoggerFactory.getLogger(NoSQLDatabaseConfigFactory.class);

    @Override
    public IDatabaseConfig createDatabaseConfig() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new IOException("Database properties file not found.");
            }

            Properties props = new Properties();
            props.load(is);
            return createDatabaseConfig(props.getProperty("db.engine"));
        } catch (IOException e) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error loading database configuration: " + e.getMessage();
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private IDatabaseConfig createDatabaseConfig(String databaseEngine) {
        try {
            NoSQLDatabaseEngine noSQLDatabaseEngine = NoSQLDatabaseEngine.valueOf(databaseEngine.toUpperCase());

            Class<?> configClass = ClasspathScanner.getClass(noSQLDatabaseEngine.getConfig());

            if (configClass != null) {
                Method getInstanceMethod = configClass.getMethod("getInstance");
                return (IDatabaseConfig) getInstanceMethod.invoke(null);
            } else {
                throw new ClassNotFoundException("Database config class not found: " + noSQLDatabaseEngine.getConfig());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error creating NoSQL database config: " + ex.getMessage();
            logger.error(errorMessage);
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
                    ". Supported engines are: " + supportedDatabases.toString());
        }
    }
}
