package com.flexcore.builder.query.implementation.sql;

import com.flexcore.annotations.Table;
import com.flexcore.builder.query.implementation.sql.helpers.SQLQueryBuilder;
import com.flexcore.builder.query.interfaces.ICustomQueryBuilder;
import com.flexcore.builder.query.types.Tuple;
import com.flexcore.builder.query.utils.QueryBuilderUtils;
import com.flexcore.database.manager.implementation.sql.implementation.SQLDatabaseManager;
import com.flexcore.database.repository.utils.SQLRepositoryUtils;
import com.flexcore.providers.DatabaseManagerFactoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SQLCustomQueryBuilder implements ICustomQueryBuilder {
    private final SQLDatabaseManager dbManager;
    private final SQLQueryBuilder sqlQueryBuilder;
    private final Class<?> clazz;
    private static final Logger logger = LoggerFactory.getLogger(SQLCustomQueryBuilder.class);
    private boolean hasJoined;  // Flag to track if a join has been called
    private final List<Class<?>> entities = new ArrayList<>();  // List of entities that have been joined

    public SQLCustomQueryBuilder(Class<?> clazz) {
        this.dbManager = (SQLDatabaseManager) DatabaseManagerFactoryProvider.getFactory().createDatabaseManager();
        this.sqlQueryBuilder = new SQLQueryBuilder(SQLRepositoryUtils.getTableName(clazz));
        this.clazz = clazz;
        this.hasJoined = false;
        entities.add(clazz);
    }

    public ICustomQueryBuilder select() {
        return this;
    }

    public ICustomQueryBuilder fields(String... fields) {
        for (String field : fields) {
            QueryBuilderUtils.checkField(field, clazz);
            sqlQueryBuilder.addField(field);
        }
        return this;
    }

    public ICustomQueryBuilder where(String field, Object value) {
        QueryBuilderUtils.checkField(field, clazz);
        sqlQueryBuilder.addFilter(field, value);
        return this;
    }

    public ICustomQueryBuilder and(String field, Object value) {
        QueryBuilderUtils.checkField(field, clazz);
        sqlQueryBuilder.addAnd(field, value);
        return this;
    }

    public ICustomQueryBuilder or(String field, Object value) {
        QueryBuilderUtils.checkField(field, clazz);
        sqlQueryBuilder.addOr(field, value);
        return this;
    }

    public ICustomQueryBuilder join(String sourceField, Class<?> targetEntity, String targetField) {
        QueryBuilderUtils.checkField(sourceField, clazz);
        QueryBuilderUtils.checkTable(targetEntity, targetField);

        if (entities.contains(targetEntity)) {
            String message = "You can't join the same entity '" + targetEntity.getSimpleName() + "' twice.";
            logger.error(message);
            throw new IllegalStateException(message);
        } else {
            entities.add(targetEntity);
            sqlQueryBuilder.addJoin(sourceField, targetEntity.getAnnotation(Table.class).name(), targetField);
        }

        if (!hasJoined) {
            hasJoined = true;
        }

        return this;
    }

    public ICustomQueryBuilder join(Class<?> sourceEntity, String sourceField, Class<?> targetEntity, String targetField) {
        if (!hasJoined) {
            String message = "You can't chain a new join before calling the initial join.";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        QueryBuilderUtils.checkTable(sourceEntity, sourceField);
        QueryBuilderUtils.checkTable(targetEntity, targetField);

        if (!entities.contains(sourceEntity)) {
            String message = "You can't use the entity '" + sourceEntity.getSimpleName() + "' as the source table because it hasn't been joined.";
            logger.error(message);
            throw new IllegalStateException(message);
        }

        if (!entities.contains(targetEntity)) {
            entities.add(targetEntity);
        }

        sqlQueryBuilder.addJoin(sourceEntity.getAnnotation(Table.class).name(), sourceField, targetEntity.getAnnotation(Table.class).name(), targetField);

        return this;
    }

    public ICustomQueryBuilder joinFields(String... fields) {

        if (!hasJoined) {
            throwIllegalStateException("You can't add join fields before calling the initial join.");
        }

        for (String field : fields) {
            if (!QueryBuilderUtils.isFieldInEntities(field, entities)) {
                StringBuilder joinedEntities = new StringBuilder();
                for (Class<?> entity : entities) {
                    joinedEntities.append(entity.getSimpleName()).append(", ");
                }

                joinedEntities.delete(joinedEntities.length() - 2, joinedEntities.length() - 1);

                throwIllegalStateException("The field '" + field + "' doesn't exist in any of the entities (" + joinedEntities + ") that have been joined.");
            } else {
                String targetTable = QueryBuilderUtils.getFieldTable(field, entities);
                sqlQueryBuilder.addJoinField(targetTable, field);
            }
        }

        return this;
    }

    private void throwIllegalStateException(String message) {
        logger.error(message);
        throw new IllegalStateException(message);
    }

    public List<List<Tuple<String, Object>>> execute() {
        List<Object> queryData = sqlQueryBuilder.build();
        String sql = (String) queryData.get(0);
        Object[] values = (Object[]) queryData.get(1);

        List<List<Tuple<String, Object>>> results = new ArrayList<>();
        ResultSet resultSet = null;

        try {
            dbManager.connect();
            dbManager.startTransaction();
            resultSet = dbManager.executeQuery(sql, values);

            // Convert the results to a list of entities
            while (resultSet.next()) {
                results.add(QueryBuilderUtils.mapResultSetToEntities(resultSet, entities));
            }
            dbManager.commitTransaction();
        } catch (Exception e) {
            String message = "Error executing query '" + sql + "' : \n\t\t" + e.getMessage();
            logger.error(message);
            dbManager.rollbackTransaction();
        } finally {
            dbManager.disconnect();
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    String message = "Error closing result set";
                    logger.error(message);
                }
            }
        }

        return results;
    }
}
