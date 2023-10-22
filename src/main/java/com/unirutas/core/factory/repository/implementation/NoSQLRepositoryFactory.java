package com.unirutas.core.factory.repository.implementation;

import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.factory.repository.interfaces.IRepositoryFactory;
import com.unirutas.core.utils.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.Properties;

public class NoSQLRepositoryFactory implements IRepositoryFactory {
    private static final Logger logger = LoggerFactory.getLogger(NoSQLRepositoryFactory.class);

    @Override
    public <T> IRepository<T> createRepository(Class<T> entityClass) {
        String databaseEngine = readDatabaseEngineFromProperties();
        return createNoSQLGenericRepository(databaseEngine, entityClass);
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

    private <T> IRepository<T> createNoSQLGenericRepository(String databaseEngine, Class<T> entityClass) {
        try {
            NoSQLDatabaseEngine noSQLDatabaseEngine = NoSQLDatabaseEngine.valueOf(databaseEngine.toUpperCase());

            Class<?> repositoryClass = ClasspathScanner.getClass(noSQLDatabaseEngine.getRepository());

            if (repositoryClass != null) {
                // Get the appropriate constructor for the entity class
                Constructor<IRepository<T>> constructor = (Constructor<IRepository<T>>) repositoryClass.getDeclaredConstructor(Class.class);
                return constructor.newInstance(entityClass);
            } else {
                throw new ClassNotFoundException("Database repository class not found: " + noSQLDatabaseEngine.getRepository());
            }
        } catch (ReflectiveOperationException ex) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error creating NoSQL database repository: " + ex.getMessage();
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
