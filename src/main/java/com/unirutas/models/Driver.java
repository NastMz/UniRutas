package com.unirutas.models;

import com.unirutas.annotations.Column;
import com.unirutas.annotations.PrimaryKey;
import com.unirutas.annotations.Table;

@Table(name = "Driver")
@PrimaryKey({"document_number"})
public class Driver implements Person {
    @Column(name = "name")
    private String name;
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
