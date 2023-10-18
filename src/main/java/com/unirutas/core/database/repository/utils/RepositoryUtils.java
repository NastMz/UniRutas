package com.unirutas.core.database.repository.utils;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public class RepositoryUtils {

    private static final Logger logger = Logger.getLogger(RepositoryUtils.class.getName());

    /**
     * Get the values of the primary key of an entity.
     *
     * @param entity The entity.
     * @return The values of the primary key.
     * @throws IllegalArgumentException If the entity does not have a primary key defined.
     */
    public static <T> PrimaryKeyValues getPrimaryKeyValues(T entity, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> primaryKeyValues = new HashMap<>();

        try {
            for (Field field : fields) {
                if (field.isAnnotationPresent(PrimaryKey.class)) {
                    field.setAccessible(true);
                    primaryKeyValues.put(field.getName(), field.get(entity));
                }
            }
        } catch (IllegalAccessException e) {
            logger.severe("Error getting primary key values: " + e.getMessage());
        }

        if (primaryKeyValues.isEmpty()) {
            throw new IllegalArgumentException("The entity " + clazz.getSimpleName() + " does not have a primary key defined.");
        }

        return new PrimaryKeyValues(primaryKeyValues);
    }

    /**
     * Check if the entity has at least one field annotated with @Column.
     *
     * @throws IllegalArgumentException If the entity does not have any field annotated with @Column.
     * @see Column
     */
    private static <T> void checkColumnAnnotations(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasColumnAnnotation = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                hasColumnAnnotation = true;
                break; // If a field annotated with @Column is found, there is no need to check for more fields.
            }
        }

        if (!hasColumnAnnotation) {
            throw new IllegalArgumentException("The entity " + clazz.getSimpleName() + " does not have any columns associated.");
        }
    }

    /**
     * Check if the entity has any field annotated with @Column that is also part of the primary key.
     *
     * @throws IllegalArgumentException If the entity has a field annotated with @Column that is also part of the primary key.
     */
    private static <T> void checkPrimaryKeyAnnotations(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasColumnAnnotation = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                hasColumnAnnotation = true;
                break; // If a field annotated with @PrimaryKey is found, there is no need to check for more fields.
            }
        }

        if (!hasColumnAnnotation) {
            throw new IllegalArgumentException("The entity " + clazz.getSimpleName() + " does not have any primary key columns associated.");
        }
    }

    /**
     * Check if the entity is annotated with all the required annotations (@Table, @PrimaryKey, @Column).
     *
     * @throws IllegalArgumentException If the entity is not annotated with any of the required annotations.
     */
    public static <T> void checkAnnotations(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("The entity does not have a table name defined.");
        }

        checkColumnAnnotations(clazz);

        checkPrimaryKeyAnnotations(clazz);
    }

    public static <T> void checkPrimaryKeyValuesMap(PrimaryKeyValues idValues, Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Set<String> idColumnNames = new HashSet<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                String idColumnName = primaryKeyAnnotation.name();
                idColumnNames.add(idColumnName);
            }
        }

        if (idValues.getValues().keySet().size() != idColumnNames.size()) {
            throw new IllegalArgumentException("The primary key values map does not contain all the primary key columns in the entity " + clazz.getSimpleName() + ".");
        }

        for (String idColumnName : idValues.getValues().keySet()) {
            if (!idColumnNames.contains(idColumnName)) {
                throw new IllegalArgumentException("The primary key values map contains a column that is not part of the primary key in the entity " + clazz.getSimpleName() + ".");
            }
        }
    }
}
