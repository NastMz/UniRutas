package com.unirutas.core.factory.builder.implementation;

import com.unirutas.core.builder.query.implementation.sql.SQLCustomQueryBuilder;
import com.unirutas.core.builder.query.interfaces.ICustomQueryBuilder;
import com.unirutas.core.database.enums.NoSQLDatabaseEngine;
import com.unirutas.core.database.enums.SQLDatabaseEngine;
import com.unirutas.core.factory.builder.interfaces.ICustomQueryBuilderFactory;
import com.unirutas.core.utils.ClasspathScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

public class NoSQLCustomQueryBuilderFactory implements ICustomQueryBuilderFactory {
    private static final Logger logger = LoggerFactory.getLogger(NoSQLCustomQueryBuilderFactory.class);
    public ICustomQueryBuilder<?> createCustomQueryBuilder(Class<?> clazz) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new IOException("Database properties file not found.");
            }

            Properties props = new Properties();
            props.load(is);
            return createCustomQueryBuilder(props.getProperty("db.engine"), clazz);
        } catch (IOException e) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error loading database configuration: " + e.getMessage();
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    private ICustomQueryBuilder<?> createCustomQueryBuilder(String databaseEngine, Class<?> clazz) {
        try {
            NoSQLDatabaseEngine noSQLDatabaseEngine = NoSQLDatabaseEngine.valueOf(databaseEngine.toUpperCase());

            Class<?> customQueryBuilderClass = ClasspathScanner.getClass(noSQLDatabaseEngine.getCustomQueryBuilder());

            if (customQueryBuilderClass != null) {
                Constructor<ICustomQueryBuilder<?>> constructor = (Constructor<ICustomQueryBuilder<?>>) customQueryBuilderClass.getDeclaredConstructor(clazz.getClass());
                return constructor.newInstance(clazz);
            } else {
                throw new ClassNotFoundException("Custom query builder class not found: " + noSQLDatabaseEngine.getCustomQueryBuilder());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException ex) {
            // Log the exception and rethrow with a more informative message.
            String errorMessage = "Error creating NoSQL custom query builder: " + ex.getMessage();
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
            throw new IllegalArgumentException("Unsupported database engine: " + databaseEngine +
                    ". Supported database engines are: " + supportedDatabases.toString());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
