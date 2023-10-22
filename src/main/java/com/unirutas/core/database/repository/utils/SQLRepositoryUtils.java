package com.unirutas.core.database.repository.utils;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLRepositoryUtils {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(SQLRepositoryUtils.class.getName());
    /**
     * Map a ResultSet to an entity.
     *
     * @param resultSet The ResultSet.
     * @return The entity.
     * @throws SQLException If an error occurs while mapping the ResultSet.
     */
    public static <T> T mapResultSetToEntity(ResultSet resultSet, Class<T> clazz) throws SQLException {
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

                    if (!isColumnInResultSet(resultSet, columnName)) continue; // Skip columns not in the query result

                    field.setAccessible(true);
                    field.set(entity, resultSet.getObject(columnName));
                } else if (field.isAnnotationPresent(PrimaryKey.class)){
                    PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                    String idColumnName = primaryKeyAnnotation.name();

                    if (!isColumnInResultSet(resultSet, idColumnName)) continue; // Skip columns not in the query result

                    if (resultSet.getObject(idColumnName) == null) continue; // Skip null values (not in the query result)
                    field.setAccessible(true);
                    field.set(entity, resultSet.getObject(idColumnName));
                }
            }
        } catch (ReflectiveOperationException e) {
            logger.severe("Error mapping ResultSet to entity " + clazz.getSimpleName() + " : " + e.getMessage());
        }

        return entity;
    }

    private static boolean isColumnInResultSet(ResultSet resultSet, String columnName) throws SQLException {
        // Check if the column is in the result set to avoid an exception
        boolean columnInResultSet = false;

        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            if (resultSet.getMetaData().getColumnName(i).equals(columnName)) {
                columnInResultSet = true;
                break;
            }
        }
        return columnInResultSet;
    }

    /**
     * Get the name of the table in the database.
     *
     * @param clazz The class of the entity.
     * @return The name of the table in the database if the entity is annotated with @Table, null otherwise.
     */
    public static String getTableName(Class<?> clazz) {
        RepositoryUtils.checkAnnotations(clazz);
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            return tableAnnotation.name();
        }
        return null;
    }
}
