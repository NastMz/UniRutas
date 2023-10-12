package com.unirutas.repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericDTO<T> {
    private final IDatabaseManager dbManager = DatabaseManager.getInstance();
    private final Class<T> clazz;
    private final String tableName;
    private final List<String> primaryKeyFields;

    public GenericDTO(Class<T> clazz, String tableName, List<String> primaryKeyFields) {
        this.clazz = clazz;
        this.tableName = tableName;
        this.primaryKeyFields = primaryKeyFields;
    }

    private Field[] getAllFields() {
        List<Field> fieldsList = new ArrayList<>();
        Class<?> currentClass = clazz;

        // Recorre las superclases y agrega sus campos a la lista
        while (currentClass != null) {
            Field[] currentFields = currentClass.getDeclaredFields();
            fieldsList.addAll(Arrays.asList(currentFields));
            currentClass = currentClass.getSuperclass();
        }

        // Convierte la lista de campos en un arreglo
        Field[] fields = new Field[fieldsList.size()];
        fields = fieldsList.toArray(fields);

        return fields;
    }


    public void insert(T entity) {
        if (exists(entity)) {
            System.out.println("El " + clazz.getSimpleName() + " ya existe.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder insertSQL = new StringBuilder("INSERT INTO " + tableName + " (");
            StringBuilder values = new StringBuilder(") VALUES (");
            Field[] fields = getAllFields();

            for (Field field : fields) {
                insertSQL.append(field.getName()).append(", ");
                values.append("?, ");
            }

            insertSQL.delete(insertSQL.length() - 2, insertSQL.length()); // Delete last comma and space
            values.delete(values.length() - 2, values.length()); // Delete last comma and space

            insertSQL.append(values).append(")");
            String sql = insertSQL.toString();

            List<Object> valuesList = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                valuesList.add(field.get(entity));
            }

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                System.out.println(clazz.getSimpleName() + " creado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                System.out.println("La creación de " + clazz.getSimpleName() + " falló.");
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(T entity) {
        if (!exists(entity)) {
            System.out.println("El " + clazz.getSimpleName() + " no existe.");
            return;
        }

        try {
            dbManager.connect();
            dbManager.startTransaction();

            String deleteSQL = "DELETE FROM " + tableName + " WHERE code = ?";
            int rowsAffected = dbManager.executeUpdate(deleteSQL, entity);

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                System.out.println(clazz.getSimpleName() + " eliminado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                System.out.println("La eliminación de " + clazz.getSimpleName() + " falló. El usuario no existe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void update(T entity) {
        try {
            dbManager.connect();
            dbManager.startTransaction();

            StringBuilder updateSQL = new StringBuilder("UPDATE " + tableName + " SET ");
            Field[] fields = getAllFields();

            for (Field field : fields) {
                if (primaryKeyFields.contains(field.getName())) {
                    // Saltar campos de clave primaria en la actualización
                    continue;
                }

                updateSQL.append(field.getName()).append(" = ?, ");
            }

            updateSQL.delete(updateSQL.length() - 2, updateSQL.length()); // Elimina la última coma y espacio

            // Construye la cláusula WHERE usando los campos de clave primaria
            updateSQL.append(" WHERE ");
            for (String primaryKeyField : primaryKeyFields) {
                updateSQL.append(primaryKeyField).append(" = ? AND ");
            }
            updateSQL.delete(updateSQL.length() - 5, updateSQL.length()); // Elimina el último "AND"

            String sql = updateSQL.toString();

            List<Object> valuesList = new ArrayList<>();
            for (Field field : fields) {
                field.setAccessible(true);
                valuesList.add(field.get(entity));
            }

            int rowsAffected = dbManager.executeUpdate(sql, valuesList.toArray());

            if (rowsAffected > 0) {
                dbManager.commitTransaction();
                System.out.println(clazz.getSimpleName() + " actualizado exitosamente.");
            } else {
                dbManager.rollbackTransaction();
                System.out.println("La actualización de " + clazz.getSimpleName() + " falló. El registro no existe.");
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean exists(T entity) {
        try {
            dbManager.connect();
            dbManager.startTransaction();

            // SQL para verificar si la entidad existe en la base de datos
            StringBuilder selectSQL = new StringBuilder("SELECT * FROM " + tableName + " WHERE ");
            for (String primaryKeyField : primaryKeyFields) {
                selectSQL.append(primaryKeyField).append(" = ? AND ");
            }
            selectSQL.delete(selectSQL.length() - 5, selectSQL.length()); // Elimina el último "AND"


            // Configurar los valores de los parámetros en la consulta SQL
            Field[] fields = getAllFields();
            List<Object> values = new ArrayList<>();
            for (String primaryKeyField : primaryKeyFields) {
                for (Field field : fields) {
                    if (field.getName().equals(primaryKeyField)) {
                        field.setAccessible(true);
                        values.add(field.get(entity));
                    }
                }
            }

            ResultSet entities = dbManager.executeQuery(String.valueOf(selectSQL), values.toArray());

            if (entities.next()) {
                dbManager.commitTransaction();
                return true;
            } else {
                dbManager.rollbackTransaction();
                return false;
            }
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
            try {
                dbManager.rollbackTransaction();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                dbManager.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    // TODO: Agrega métodos para buscar, etc.
}
