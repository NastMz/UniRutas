package com.unirutas.models;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

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
