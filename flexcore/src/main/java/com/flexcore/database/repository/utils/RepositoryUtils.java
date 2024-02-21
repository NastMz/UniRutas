package com.flexcore.database.repository.utils;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RepositoryUtils {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryUtils.class);

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
                    PrimaryKey primaryKeyAnnotation = field.getAnnotation(PrimaryKey.class);
                    field.setAccessible(true);
                    primaryKeyValues.put(primaryKeyAnnotation.name(), field.get(entity));
                }
            }
        } catch (IllegalAccessException e) {
            logger.error("Error getting primary key values: " + e.getMessage());
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
    private static <T> boolean checkColumnAnnotations(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasColumnAnnotation = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                hasColumnAnnotation = true;
                break; // If a field annotated with @Column is found, there is no need to check for more fields.
            }
        }

        return hasColumnAnnotation;
    }

    /**
     * Check if the entity has any field annotated with @Column that is also part of the primary key.
     *
     * @throws IllegalArgumentException If the entity has a field annotated with @Column that is also part of the primary key.
     */
    private static <T> boolean checkPrimaryKeyAnnotations(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        boolean hasColumnAnnotation = false;

        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                hasColumnAnnotation = true;
                break; // If a field annotated with @PrimaryKey is found, there is no need to check for more fields.
            }
        }

        return hasColumnAnnotation;
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

        if (!checkPrimaryKeyAnnotations(clazz)) {
            throw new IllegalArgumentException("The entity " + clazz.getSimpleName() + " does not have a primary key defined.");
        }

        if (!checkColumnAnnotations(clazz)) {
            logger.warn("The entity " + clazz.getSimpleName() + " does not have any field annotated with @Column.");
        }
    }

    /**
     * Check if the primary key values map contains all the primary key columns in the entity.
     * @param idValues The primary key values map.
     * @param clazz The entity class.
     * @param <T> The entity type.
     */
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
                throw new IllegalArgumentException("The primary key values map contains a column (" + idColumnName + ") that is not part of the primary key in the entity " + clazz.getSimpleName() + ".");
            }
        }
    }

    /**
     * Instantiate an entity.
     * @param clazz The entity class.
     * @param entity The entity to instantiate.
     * @return The instantiated entity.
     * @param <T> The entity type.
     * @throws InstantiationException If an error occurs while instantiating the entity.
     * @throws IllegalAccessException If an error occurs while instantiating the entity.
     * @throws InvocationTargetException If an error occurs while instantiating the entity.
     * @throws NoSuchMethodException If an error occurs while instantiating the entity.
     */
    public static <T> T instanceEntity(Class<T> clazz, T entity) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
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

        Object[] fieldsNull = getFieldsNull(fieldsTypes);

        entity = clazz.getDeclaredConstructor(fieldsTypes).newInstance(fieldsNull);

        return entity;
    }

    /**
     * Get an array of null values for the fields of an entity.
     * @param fieldsTypes The fields types.
     * @return An array of null values for the fields of an entity.
     * @throws InstantiationException If an error occurs while instantiating the entity.
     * @throws IllegalAccessException If an error occurs while instantiating the entity.
     * @throws InvocationTargetException If an error occurs while instantiating the entity.
     * @throws NoSuchMethodException If an error occurs while instantiating the entity.
     */
    private static Object[] getFieldsNull(Class<?>[] fieldsTypes) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        // Set all fields to null to instantiate the entity
        Object [] fieldsNull = new Object[fieldsTypes.length];
        for (int i = 0; i < fieldsTypes.length; i++) {
            if (fieldsTypes[i].isPrimitive()){
                // If the field is a primitive type, instantiate it with the default value of the type (0 for int, false for boolean, etc.)
                if (fieldsTypes[i].equals(int.class)) {
                    fieldsNull[i] = 0;
                } else if (fieldsTypes[i].equals(boolean.class)) {
                    fieldsNull[i] = false;
                } else if (fieldsTypes[i].equals(long.class)) {
                    fieldsNull[i] = 0L;
                } else if (fieldsTypes[i].equals(float.class)) {
                    fieldsNull[i] = 0.0f;
                } else if (fieldsTypes[i].equals(double.class)) {
                    fieldsNull[i] = 0.0d;
                } else if (fieldsTypes[i].equals(byte.class)) {
                    fieldsNull[i] = 0;
                } else if (fieldsTypes[i].equals(short.class)) {
                    fieldsNull[i] = 0;
                } else if (fieldsTypes[i].equals(char.class)) {
                    fieldsNull[i] = '\u0000';
                }
            } else {
                fieldsNull[i] = fieldsTypes[i].getConstructor().newInstance();
            }
        }
        return fieldsNull;
    }
}
