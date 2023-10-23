package com.unirutas.core.builder.query.utils;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;
import com.unirutas.core.builder.query.types.Tuple;
import com.unirutas.core.database.repository.utils.MongoDBRepositoryUtils;
import com.unirutas.core.database.repository.utils.SQLRepositoryUtils;
import org.bson.Document;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QueryBuilderUtils {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(QueryBuilderUtils.class);

    /**
     * Check if the field exists in the entity.
     *
     * @param field The field to check.
     */
    public static void checkField(String field, Class<?> clazz) {
        boolean isValid = Arrays.stream(clazz.getDeclaredFields())
                .anyMatch(f -> isMatchingColumn(f, field) || isMatchingPrimaryKey(f, field));

        if (!isValid) {
            String message = "The field '" + field + "' does not exist in the entity '" + clazz.getSimpleName() + "'";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check if the field matches the column name.
     *
     * @param f     The field to check.
     * @param field The field name.
     * @return True if the field matches the column name. False otherwise.
     */
    private static boolean isMatchingColumn(Field f, String field) {
        return f.isAnnotationPresent(Column.class) && f.getAnnotation(Column.class).name().equals(field);
    }

    /**
     * Check if the field matches the primary key name.
     *
     * @param f     The field to check.
     * @param field The column name.
     * @return True if the field matches the primary key name. False otherwise.
     */
    private static boolean isMatchingPrimaryKey(Field f, String field) {
        return f.isAnnotationPresent(PrimaryKey.class) && f.getAnnotation(PrimaryKey.class).name().equals(field);
    }

    /**
     * Check if the table exists and if the target field exists in the table.
     *
     * @param entity      The entity to check.
     * @param targetField The target field to check
     * @throws IllegalArgumentException If the table does not exist.
     */
    public static void checkTable(Class<?> entity, String targetField) {
        if (entity.isAnnotationPresent(Table.class)) {
            boolean isValid = Arrays.stream(entity.getDeclaredFields())
                    .anyMatch(f -> isMatchingColumn(f, targetField) || isMatchingPrimaryKey(f, targetField));

            if (!isValid) {
                String message = "The field '" + targetField + "' does not exist in the entity '" + entity.getSimpleName() + "'";
                logger.error(message);
                throw new IllegalArgumentException(message);
            }
        } else {
            String message = "The table '" + entity.getSimpleName() + "' does not match any entity annotated with @Table";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Check if the field exists in any of the entities.
     *
     * @param field    The field to check.
     * @param entities The entities to check.
     * @return True if the field exists in any of the entities. False otherwise.
     */
    public static boolean isFieldInEntities(String field, List<Class<?>> entities) {
        for (Class<?> entity : entities) {
            for (Field entityField : entity.getDeclaredFields()) {
                if (isMatchingColumn(entityField, field) || isMatchingPrimaryKey(entityField, field)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Map the result set to the entity.
     * @param resultSet The result set.
     * @param entities The entities to map the result set to.
     * @return A list of tuples containing the entity name and the entity instance.
     */
    public static List<Tuple<String, Object>> mapResultSetToEntities(ResultSet resultSet, List<Class<?>> entities) {

        List<Tuple<String, Object>> results = new ArrayList<>();

        for (Class<?> entity : entities) {
            Object entityInstance = SQLRepositoryUtils.mapResultSetToEntity(resultSet, entity);
            results.add(new Tuple<>(entity.getSimpleName(), entityInstance));
        }

        return results;
    }

    /**
     * Map the document to the entity.
     * @param document The document.
     * @param entities The entities to map the document to.
     * @return A list of tuples containing the entity name and the entity instance.
     */
    public static List<Tuple<String, Object>> mapDocumentToEntities(Document document, List<Class<?>> entities) {

        List<Tuple<String, Object>> results = new ArrayList<>();

        for (Class<?> entity : entities) {
            Object entityInstance = MongoDBRepositoryUtils.mapDocumentToEntity(document, entity);
            results.add(new Tuple<>(entity.getSimpleName(), entityInstance));
        }

        return results;
    }

    /**
     * Get the name of the table in the database.
     * @param field The field to check.
     * @param entities The entities to check.
     * @return The name of the table in the database if the field exists in any of the entities. Null otherwise.
     */
    public static String getFieldTable(String field, List<Class<?>> entities) {
        for (Class<?> entity : entities) {
            for (Field entityField : entity.getDeclaredFields()) {
                if (isMatchingColumn(entityField, field) || isMatchingPrimaryKey(entityField, field)) {
                    return entity.getAnnotation(Table.class).name();
                }
            }
        }
        return null;
    }
}
