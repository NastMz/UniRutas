package com.unirutas.core.database.repository.implementation.nosql;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;
import com.unirutas.core.database.manager.implementation.nosql.implementation.MongoDBDatabaseManager;
import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.database.repository.utils.RepositoryUtils;
import com.unirutas.core.providers.DatabaseManagerFactoryProvider;
import org.bson.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Class to manage generic repository operations for MongoDB.
 *
 * @param <T> The entity type.
 */
public class MongoDBGenericRepository<T> implements IRepository<T> {
    private final MongoDBDatabaseManager dbManager;
    private final Class<T> clazz;
    private final String collectionName;
    private final Logger logger = Logger.getLogger(MongoDBGenericRepository.class.getName());

    public MongoDBGenericRepository(Class<T> clazz) {
        this.dbManager = (MongoDBDatabaseManager) DatabaseManagerFactoryProvider.getFactory().createDatabaseManager();
        this.clazz = clazz;
        this.collectionName = getCollectionName(clazz);
    }

    private String getCollectionName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        }
        return null;
    }

    private T mapDocumentToEntity(Document document) {
        T entity = null;
        try {
            entity = clazz.getDeclaredConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    field.setAccessible(true);
                    if (document.containsKey(columnName)) {
                        field.set(entity, document.get(columnName));
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            logger.severe("Error mapping Document to entity: " + e.getMessage());
        }
        return entity;
    }

    public void getDatabaseEngineDate() {
        dbManager.getDate();
    }

    public void getDatabaseEngineHour() {
        dbManager.getHour();
    }

    public T findById(PrimaryKeyValues id) {
        RepositoryUtils.checkAnnotations(clazz);

        T entity = null;
        try {
            Document document = new Document();

            for (Map.Entry<String, Object> entry : id.getValues().entrySet()) {
                document.append(entry.getKey(), entry.getValue());
            }

            Document record = dbManager.find(collectionName, document);

            if (record != null) {
                entity = mapDocumentToEntity(record);
            } else {
                logger.warning("Entity not found.");
            }
        } catch (Exception e) {
            logger.severe("Error finding entity by id: " + e.getMessage());
        }
        return entity;
    }

    public List<T> findAll() {
        RepositoryUtils.checkAnnotations(clazz);

        List<T> entities = new ArrayList<>();
        try {
            List<Document> records = dbManager.findAll(collectionName);
            for (Document r : records) {
                entities.add(mapDocumentToEntity(r));
            }
        } catch (Exception e) {
            logger.severe("Error finding all entities: " + e.getMessage());
        }
        return entities;
    }

    public void save(T entity) {
        RepositoryUtils.checkAnnotations(clazz);

        PrimaryKeyValues primaryKeyValues = RepositoryUtils.getPrimaryKeyValues(entity, clazz);

        if (existsById(primaryKeyValues)) {
            logger.warning("A record with the same primary key already exists.");
            return;
        }

        try {
            Document document = new Document();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    field.setAccessible(true);
                    document.append(columnName, field.get(entity));
                } else if (field.isAnnotationPresent(PrimaryKey.class)) {
                    PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                    String columnName = primaryKeyAnnotation.name();
                    field.setAccessible(true);
                    document.append(columnName, field.get(entity));
                }
            }

            dbManager.insert(collectionName, document);
            logger.info("Entity " + collectionName + " saved successfully.");
        } catch (Exception e) {
            logger.severe("Error saving entity: " + e.getMessage());
        }
    }

    public void delete(PrimaryKeyValues id) {
        RepositoryUtils.checkAnnotations(clazz);

        if (!existsById(id)) {
            logger.warning("The entity does not exist.");
            return;
        }

        try {
            Document document = new Document();

            for (Map.Entry<String, Object> entry : id.getValues().entrySet()) {
                document.append(entry.getKey(), entry.getValue());
            }

            dbManager.delete(collectionName, document);

            logger.info("Entity " + collectionName + " deleted successfully.");
        } catch (Exception e) {
            logger.severe("Error deleting entity: " + e.getMessage());
        }
    }

    public void update(T entity) {
        RepositoryUtils.checkAnnotations(clazz);

        PrimaryKeyValues primaryKeyValues = RepositoryUtils.getPrimaryKeyValues(entity, clazz);

        if (!existsById(primaryKeyValues)) {
            logger.warning("The entity does not exist.");
            return;
        }

        try {
            Document document = new Document();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    field.setAccessible(true);
                    document.append(columnName, field.get(entity));
                }
            }

            Document filter = new Document();

            for (Map.Entry<String, Object> entry : primaryKeyValues.getValues().entrySet()) {
                filter.append(entry.getKey(), entry.getValue());
                document.append(entry.getKey(), entry.getValue());
            }

            dbManager.update(collectionName, filter, document);

            logger.info("Entity " + collectionName + " updated successfully.");
        } catch (Exception e) {
            logger.severe("Error updating entity: " + e.getMessage());
        }
    }

    public boolean existsById(PrimaryKeyValues id) {
        RepositoryUtils.checkAnnotations(clazz);

        try {
            Document document = new Document();

            for (Map.Entry<String, Object> entry : id.getValues().entrySet()) {
                document.append(entry.getKey(), entry.getValue());
            }

            return dbManager.find(collectionName, document) != null;
        } catch (Exception e) {
            logger.severe("Error checking if entity exists: " + e.getMessage());
        }

        return false;
    }
}
