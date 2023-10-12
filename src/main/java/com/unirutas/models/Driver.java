package com.unirutas.models;

import java.util.List;

public class Driver extends Person {
    private List<Bus> drivers;

    public Driver(String name, String document_number) {
        super(name, document_number);
    }
}
