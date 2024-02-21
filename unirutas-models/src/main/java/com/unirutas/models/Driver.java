package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

@Table(name = "Driver")
public class Driver implements Person {
    @Column(name = "name")
    private String name;
    @PrimaryKey(name = "document_number")
    private String documentNumber;

    public Driver(String name, String documentNumber) {
        this.name = name;
        this.documentNumber = documentNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return documentNumber;
    }
}
