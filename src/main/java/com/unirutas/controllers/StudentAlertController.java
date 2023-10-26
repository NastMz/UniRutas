package com.unirutas.controllers;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.StudentAlert;
import com.unirutas.services.implementation.StudentAlertServices;

import java.util.List;

public class StudentAlertController {
    @Inject
    private StudentAlertServices studentAlertServices;

    public void create(StudentAlert studentAlert) {
        studentAlertServices.create(studentAlert);
    }

    public void update(StudentAlert studentAlert) {
        studentAlertServices.update(studentAlert);
    }

    public void delete(String studentCode, String alertId) {
        studentAlertServices.delete(studentCode, alertId);
    }

    public StudentAlert findById(String studentCode, String alertId) {
        return studentAlertServices.findById(studentCode, alertId);
    }

    public boolean existsById(String studentCode, String alertId) {
        return studentAlertServices.existsById(studentCode, alertId);
    }

    public List<StudentAlert> findAll() {
        return studentAlertServices.findAll();
    }

}
