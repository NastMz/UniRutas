package com.unirutas.models;

import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

@Table(name = "StudentSubscription")
public class StudentSubscription {
    @PrimaryKey(name = "student_code")
    private String studentCode;
    @PrimaryKey(name = "service_id")
    private String serviceId;

    public StudentSubscription(String studentCode, String serviceId) {
        this.studentCode = studentCode;
        this.serviceId = serviceId;
    }
}
