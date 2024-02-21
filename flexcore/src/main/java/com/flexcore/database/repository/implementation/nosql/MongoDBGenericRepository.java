package com.flexcore.database.repository.implementation.nosql;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.database.manager.implementation.nosql.implementation.MongoDBDatabaseManager;
import com.flexcore.database.repository.interfaces.IRepository;
import com.flexcore.database.repository.utils.MongoDBRepositoryUtils;
import com.flexcore.database.repository.utils.PrimaryKeyValues;
import com.flexcore.database.repository.utils.RepositoryUtils;
import com.flexcore.providers.DatabaseManagerFactoryProvider;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class to manage generic repository operations for MongoDB.
 *
 * @param <T> The entity type.
 */
public class MongoDBGenericRepository<T> implements IRepository<T> {
    private final MongoDBDatabaseManager dbManager;
    private final Class<T> clazz;
    private final String collectionName;
    private final Logger logger = LoggerFactory.getLogger(MongoDBGenericRepository.class);

    public MongoDBGenericRepository(Class<T> clazz) {
        this.dbManager = (MongoDBDatabaseManager) DatabaseManagerFactoryProvider.getFactory().createDatabaseManager();
        this.clazz = clazz;
        this.collectionName = MongoDBRepositoryUtils.getCollectionName(clazz);
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
                entity = MongoDBRepositoryUtils.mapDocumentToEntity(record, clazz);
            } else {
                logger.warn("Entity not found.");
            }
        } catch (Exception e) {
            logger.error("Error finding entity by id: " + e.getMessage());
        }
        return entity;
    }

    public List<T> findAll() {
        RepositoryUtils.checkAnnotations(clazz);

        List<T> entities = new ArrayList<>();
        try {
            List<Document> records = dbManager.findAll(collectionName);
            for (Document r : records) {
                entities.add(MongoDBRepositoryUtils.mapDocumentToEntity(r, clazz));
            }
        } catch (Exception e) {
            logger.error("Error finding all entities: " + e.getMessage());
        }
        return entities;
    }

    public void save(T entity) {
        RepositoryUtils.checkAnnotations(clazz);

        PrimaryKeyValues primaryKeyValues = RepositoryUtils.getPrimaryKeyValues(entity, clazz);


        if (existsById(primaryKeyValues)) {
            logger.warn("A " + clazz.getSimpleName() + " record with the same primary key (" + primaryKeyValues.getValues().toString() + ") already exists.");
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
            logger.error("Error saving entity: " + e.getMessage());
        }
    }

    public void delete(PrimaryKeyValues id) {
        RepositoryUtils.checkAnnotations(clazz);

        if (!existsById(id)) {
            logger.warn("The entity does not exist.");
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
            logger.error("Error deleting entity: " + e.getMessage());
        }
    }

    public void update(T entity) {
        RepositoryUtils.checkAnnotations(clazz);

        PrimaryKeyValues primaryKeyValues = RepositoryUtils.getPrimaryKeyValues(entity, clazz);

        if (!existsById(primaryKeyValues)) {
            logger.warn("The entity does not exist.");
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
            logger.error("Error updating entity: " + e.getMessage());
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
            logger.error("Error checking if entity exists: " + e.getMessage());
        }

        return false;
    }
}
