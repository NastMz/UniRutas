package com.unirutas.core.database.repository.implementation.sql;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;
import com.unirutas.core.database.manager.implementation.sql.implementation.SQLDatabaseManager;
import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.database.repository.utils.RepositoryUtils;
import com.unirutas.core.database.repository.utils.SQLRepositoryUtils;
import com.unirutas.core.providers.DatabaseManagerFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Class to manage generic repository operations.
 *
 * @param <T> The entity type.
 */
public class SQLGenericRepository<T> implements IRepository<T> {
    private final SQLDatabaseManager dbManager;
    private final Class<T> clazz; // The class of the entity
    private final String tableName; // Name of the table in the database
    private final Logger logger = LoggerFactory.getLogger(SQLGenericRepository.class);

    public SQLGenericRepository(Class<T> clazz) {
        this.dbManager = (SQLDatabaseManager) DatabaseManagerFactoryProvider.getFactory().createDatabaseManager();
        this.clazz = clazz;
        this.tableName = SQLRepositoryUtils.getTableName(clazz);
    }

    /**
     * Rollback the transaction and handle errors.
     */
    private void rollbackAndHandleErrors() {
        dbManager.rollbackTransaction();
    }

    /**
     * Disconnect from the database and handle errors.
     */
    private void disconnectAndHandleErrors() {
        dbManager.disconnect();
    }

    public void save(T entity) {
        RepositoryUtils.checkAnnotations(clazz);

        PrimaryKeyValues primaryKeyValues =  RepositoryUtils.getPrimaryKeyValues(entity, clazz);

        if (existsById(primaryKeyValues)) {
            logger.warn("A " + clazz.getSimpleName() + " record with the same primary key (" + primaryKeyValues.getValues().toString() + ") already exists.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder values = new StringBuilder(") VALUES (");

            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();

                    insertSQL.append(columnName).append(", ");
                    values.append("?, ");
                }
            }

            // append primary key column names
            for (String idColumnName : primaryKeyValues.getValues().keySet()) {
                insertSQL.append(idColumnName).append(", ");
                values.append("?, ");
            }

            insertSQL.setLength(insertSQL.length() - 2);
            values.setLength(values.length() - 2);

            insertSQL.append(values).append(")");
            String sql = insertSQL.toString();

            List<Object> valuesList = new ArrayList<>();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);
                    valuesList.add(field.get(entity));
                }
            }

            // append primary key values
            valuesList.addAll(primaryKeyValues.getValues().values());

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                logger.info(clazz.getSimpleName() + " saved successfully.");
            } else {
                logger.warn("Saving " + clazz.getSimpleName() + " failed.");
                rollbackAndHandleErrors();
            }
        } catch (IllegalAccessException e) {
            logger.error("Error saving entity: " + e.getMessage());
            rollbackAndHandleErrors();
        } finally {
            disconnectAndHandleErrors();
        }
    }

    public void delete(PrimaryKeyValues idValues) {

        RepositoryUtils.checkAnnotations(clazz);
        RepositoryUtils.checkPrimaryKeyValuesMap(idValues, clazz);

        if (!existsById(idValues)) {
            logger.warn("The " + clazz.getSimpleName() + " record does not exist, it cannot be deleted.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder deleteSQL = new StringBuilder("DELETE FROM " + tableName + " WHERE ");

            // In idValues.getValues() the key is the column name and the value is the value of the column
            for (String idColumnName : idValues.getValues().keySet()) {
                deleteSQL.append(idColumnName).append(" = ? AND ");
            }

            deleteSQL.setLength(deleteSQL.length() - 5);

            String sql = deleteSQL.toString();

            Object[] idValuesArray = idValues.getValues().values().toArray();

            int rowsAffected = dbManager.executeUpdate(sql, idValuesArray);

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                logger.info(clazz.getSimpleName() + " deleted successfully.");
            } else {
                logger.warn("Deleting " + clazz.getSimpleName() + " failed. The record has not been deleted or does not exist.");
                rollbackAndHandleErrors();
            }
        } finally {
            disconnectAndHandleErrors();
        }
    }

    public void update(T entity) {
        RepositoryUtils.checkAnnotations(clazz);

        PrimaryKeyValues primaryKeyValuesMap = RepositoryUtils.getPrimaryKeyValues(entity, clazz);
        RepositoryUtils.checkPrimaryKeyValuesMap(primaryKeyValuesMap, clazz);

        if (!existsById(primaryKeyValuesMap)) {
            logger.warn("The " + clazz.getSimpleName() + " record does not exist, and cannot be updated.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
            Field[] fields = clazz.getDeclaredFields();
            List<Object> valuesList = new ArrayList<>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    updateSQL.append(columnName).append(" = ?, ");
                    field.setAccessible(true);
                    valuesList.add(field.get(entity));
                }
            }

            updateSQL.setLength(updateSQL.length() - 2);

            updateSQL.append(" WHERE ");

            // In idValues.getValues() the key is the column name and the value is the value of the column
            for (String idColumnName : primaryKeyValuesMap.getValues().keySet()) {
                updateSQL.append(idColumnName).append(" = ? AND ");
            }

            updateSQL.setLength(updateSQL.length() - 5);

            String sql = updateSQL.toString();

            // append primary key values
            valuesList.addAll(primaryKeyValuesMap.getValues().values());

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                logger.info(clazz.getSimpleName() + " updated successfully.");
            } else {
                logger.warn("Updating " + clazz.getSimpleName() + " failed. The record was not updated or does not exist.");
                rollbackAndHandleErrors();
            }
        } catch (IllegalAccessException e) {
            logger.error("Error updating entity: " + e.getMessage());
            rollbackAndHandleErrors();
        } finally {
            disconnectAndHandleErrors();
        }
    }

    public boolean existsById(PrimaryKeyValues idValues) {

        RepositoryUtils.checkAnnotations(clazz);
        RepositoryUtils.checkPrimaryKeyValuesMap(idValues, clazz);

        ResultSet resultSet = null;

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder selectSQL = new StringBuilder("SELECT COUNT(*) FROM " + tableName + " WHERE ");

            // In idValues.getValues() the key is the column name and the value is the value of the column
            for (String idColumnName : idValues.getValues().keySet()) {
                selectSQL.append(idColumnName).append(" = ? AND ");
            }

            selectSQL.delete(selectSQL.length() - 5, selectSQL.length());

            String sql = selectSQL.toString();

            Object[] idValuesArray = idValues.getValues().values().toArray();

            resultSet = dbManager.executeQuery(sql, idValuesArray);
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            } else {
                rollbackAndHandleErrors();
            }

        } catch (SQLException e) {
            logger.error("Error checking if entity exists: " + e.getMessage());
            rollbackAndHandleErrors();
        } finally {
            disconnectAndHandleErrors();
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet: " + e.getMessage());
                }
            }
        }
        return false;
    }

    public void getDatabaseEngineDate() {
        dbManager.getDate();
    }

    public void getDatabaseEngineHour() {
        dbManager.getHour();
    }

    public T findById(PrimaryKeyValues idValues) {

        RepositoryUtils.checkAnnotations(clazz);
        RepositoryUtils.checkPrimaryKeyValuesMap(idValues, clazz);

        T entity = null;
        ResultSet resultSet = null;

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder selectSQL = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");

            // In idValues.getValues() the key is the column name and the value is the value of the column
            for (String idColumnName : idValues.getValues().keySet()) {
                selectSQL.append(idColumnName).append(" = ? AND ");
            }

            selectSQL.delete(selectSQL.length() - 5, selectSQL.length());

            String sql = selectSQL.toString();

            Object[] idValuesArray = idValues.getValues().values().toArray();

            resultSet = dbManager.executeQuery(sql, idValuesArray);

            if (resultSet.next()) {
                entity = SQLRepositoryUtils.mapResultSetToEntity(resultSet, clazz);
            }

            dbManager.commitTransaction();
        } catch (SQLException e) {
            logger.error("Error finding entity by id: " + e.getMessage());
            rollbackAndHandleErrors();
        } finally {
            disconnectAndHandleErrors();
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    logger.error("Error closing ResultSet: " + e.getMessage());
                }
            }
        }

        return entity;
    }

    public List<T> findAll() {

        RepositoryUtils.checkAnnotations(clazz);

        List<T> entities = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            dbManager.connect();
            dbManager.startTransaction();

            String sql = "SELECT * FROM " + tableName;

            resultSet = dbManager.executeQuery(sql);

            while (resultSet.next()) {
                T entity = SQLRepositoryUtils.mapResultSetToEntity(resultSet, clazz);
                entities.add(entity);
            }

            dbManager.commitTransaction();
        } catch (SQLException e) {
            logger.error("Error finding all entities: " + e.getMessage());
            rollbackAndHandleErrors();
        } finally {
            disconnectAndHandleErrors();
        }

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing ResultSet.", e);
            }
        }

        return entities;
    }
}

