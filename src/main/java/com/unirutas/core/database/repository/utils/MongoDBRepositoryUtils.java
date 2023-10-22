package com.unirutas.core.database.repository.utils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;
import com.unirutas.core.providers.ConnectionPoolFactoryProvider;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Properties;

public class MongoDBRepositoryUtils {
    private static final Logger logger = LoggerFactory.getLogger(MongoDBRepositoryUtils.class);
    public static <T> T mapDocumentToEntity(Document document, Class<T> clazz) {
        RepositoryUtils.checkAnnotations(clazz);

        T entity = null;
        try {

            // Get fields types to instantiate the entity

            Constructor<?>[] constructors = clazz.getDeclaredConstructors();

            Constructor<?> constructor = null;

            for (Constructor<?> c : constructors) {
                if (c.getParameterCount() > 0) {
                    constructor = c;
                    break;
                }
            }

            Class<?>[] fieldsTypes = constructor.getParameterTypes();

            // Set all fields to null to instantiate the entity
            Object [] fieldsNull = new Object[fieldsTypes.length];
            for (int i = 0; i < fieldsTypes.length; i++) {
                fieldsNull[i] = null;
            }

            entity = clazz.getDeclaredConstructor(fieldsTypes).newInstance(fieldsNull);

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    field.setAccessible(true);
                    if (document.containsKey(columnName)) {
                        field.set(entity, document.get(columnName));
                    }
                } else if (field.isAnnotationPresent(PrimaryKey.class)) {
                    PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                    String columnName = primaryKeyAnnotation.name();
                    field.setAccessible(true);
                    if (document.containsKey(columnName)) {
                        field.set(entity, document.get(columnName));
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            logger.error("Error mapping Document to entity: " + e.getMessage());
        }
        return entity;
    }

    public static MongoDatabase loadDatabase(MongoClient client) {
        MongoDatabase database = null;
        try (InputStream is = ConnectionPoolFactoryProvider.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            props.load(is);
            String dbName = props.getProperty("db.database");
            database = client.getDatabase(dbName);
        } catch (IOException e) {
            String message = "Error loading database: " + e.getMessage();
            logger.error(message, e);
        }
        return database;
    }

    public static String getCollectionName(Class<?> clazz) {
        RepositoryUtils.checkAnnotations(clazz);
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        }
        return null;
    }
}
