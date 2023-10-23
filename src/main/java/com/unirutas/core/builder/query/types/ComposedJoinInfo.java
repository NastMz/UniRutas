package com.unirutas.core.builder.query.types;

public class ComposedJoinInfo extends JoinInfo {
    private final String sourceTable;

    public ComposedJoinInfo(String sourceTable, String sourceField, String targetTable, String targetField){
        super(targetTable, sourceField, targetField);
        this.sourceTable = sourceTable;
    }

    public String getSourceTable() {
        return sourceTable;
    }
}
