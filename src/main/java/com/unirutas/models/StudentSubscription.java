package com.unirutas.models;

import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

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
