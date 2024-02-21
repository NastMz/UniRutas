package com.flexcore.builder.query.types;

public class JoinInfo {
    private final String targetTable;
    private final String sourceField;
    private final String targetField;

    public JoinInfo(String targetTable, String sourceField, String targetField) {
        this.targetTable = targetTable;
        this.sourceField = sourceField;
        this.targetField = targetField;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public String getSourceField() {
        return sourceField;
    }

    public String getTargetField() {
        return targetField;
    }
}