package com.unirutas.models;

public class StudentSubscription {
    private String serviceCode;
    private String studentCode;

    public StudentSubscription(String studentCode, String serviceCode) {
        this.studentCode = studentCode;
        this.serviceCode = serviceCode;
    }

}
