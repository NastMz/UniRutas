package com.flexcore.builder.query.implementation.sql.helpers;

import com.flexcore.builder.query.types.ComposedJoinInfo;
import com.flexcore.builder.query.types.JoinInfo;
import com.flexcore.builder.query.types.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLQueryBuilder {
    private final String table;
    private final Map<String, Object> filters = new HashMap<>();
    private final List<String> fields = new ArrayList<>();
    private final List<JoinInfo> joinConditions = new ArrayList<>();
    private final List<ComposedJoinInfo> composedJoinConditions = new ArrayList<>();

    private final List<Tuple<String, String>> joinFields = new ArrayList<>();
    public SQLQueryBuilder(String table) {
        this.table = table;
    }

    public void addFilter(String field, Object value) {
        filters.put(field, value);
    }

    public void addField(String field) {
        fields.add(field);
    }

    public void addAnd(String field, Object value) {
        filters.put("AND " + field, value);
    }

    public void addOr(String field, Object value) {
        filters.put("OR " + field, value);
    }

    public void addJoin(String sourceField, String targetTable, String targetField) {
        joinConditions.add(new JoinInfo(targetTable, sourceField, targetField));
    }

    public void addJoin(String sourceTable, String sourceField, String targetTable, String targetField) {
        composedJoinConditions.add(new ComposedJoinInfo(sourceTable, sourceField, targetTable, targetField));
    }

    public void addJoinField(String targetTable, String targetField){
        joinFields.add(new Tuple<>(targetTable, targetField));
    }

    public List<Object> build() {
        StringBuilder query = new StringBuilder();
        List<Object> values = new ArrayList<>();

        if (fields.isEmpty() && joinFields.isEmpty()) {
            query.append("SELECT * FROM ").append(table);
        }

        else {
            StringBuilder fieldsString = new StringBuilder();
            for (String field : fields) {
                fieldsString.append(table).append(".").append(field).append(", ");
            }

            joinFields.forEach(joinField -> fieldsString.append(joinField.getKey()).append(".").append(joinField.getValue()).append(", "));

            fieldsString.delete(fieldsString.length() - 2, fieldsString.length());
            query.append("SELECT ").append(fieldsString).append(" FROM ").append(table);
        }

        if (!filters.isEmpty()) {
            StringBuilder filter = new StringBuilder();

            List<String> keys = new ArrayList<>(filters.keySet());

            // Separate the keys into 'AND' and 'OR' keys
            List<String> andKeys = new ArrayList<>();
            List<String> orKeys = new ArrayList<>();

            if (!joinConditions.isEmpty()) {
                for (JoinInfo joinInfo : joinConditions) {
                    query.append(" INNER JOIN ")
                            .append(joinInfo.getTargetTable())
                            .append(" ON ")
                            .append(table).append(".") // Use the table name as a prefix
                            .append(joinInfo.getSourceField())
                            .append(" = ")
                            .append(joinInfo.getTargetTable()).append(".")
                            .append(joinInfo.getTargetField());
                }
            }

            if (!composedJoinConditions.isEmpty()){
                for (ComposedJoinInfo joinInfo : composedJoinConditions){
                    query.append(" INNER JOIN ")
                            .append(joinInfo.getTargetTable())
                            .append(" ON ")
                            .append(joinInfo.getSourceTable()).append(".") // Use the table name as a prefix
                            .append(joinInfo.getSourceField())
                            .append(" = ")
                            .append(joinInfo.getTargetTable()).append(".")
                            .append(joinInfo.getTargetField());
                }
            }

            for (String key : keys) {
                if (key.startsWith("AND")) {
                    andKeys.add(key);
                } else if (key.startsWith("OR")) {
                    orKeys.add(key);
                }
            }

            // First add the non AND/OR keys
            for (String key : keys) {
                if (!key.startsWith("AND") && !key.startsWith("OR")) {
                    filter.append(table).append(".").append(key).append(" = ").append("?").append(" ");
                    values.add(filters.get(key));
                }
            }

            // Then add the AND keys

            if (!andKeys.isEmpty()) {
                filter.append("AND ");
                for (String key : andKeys) {
                    // Remove the 'AND' prefix
                    String newKey = key.substring(4);
                    filter.append(table).append(".").append(newKey).append(" = ").append("?").append(" ");
                    values.add(filters.get(key));
                    filter.append("AND ");
                }
                filter.delete(filter.length() - 4, filter.length());
            }

            // Then add the OR keys

            if (!orKeys.isEmpty()) {
                filter.append("OR ");
                for (String key : orKeys) {
                    // Remove the 'OR' prefix
                    String newKey = key.substring(3);
                    filter.append(table).append(".").append(newKey).append(" = ").append("?").append(" ");
                    values.add(filters.get(key));
                    filter.append("OR ");
                }
                filter.delete(filter.length() - 3, filter.length());
            }

            query.append(" WHERE ").append(filter);
        }

        List<Object> result = new ArrayList<>();

        result.add(query.toString());

        // Convert the list of values to an Object array

        Object[] valuesArray = new Object[values.size()];

        for (int i = 0; i < values.size(); i++) {
            valuesArray[i] = values.get(i);
        }

        result.add(valuesArray);

        return result;
    }
}
