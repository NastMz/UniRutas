package com.unirutas.controllers;

import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.StudentSubscription;
import com.unirutas.services.implementation.StudentSubscriptionServices;

import java.util.List;

public class StudentSubscriptionController {
    @Inject
    private StudentSubscriptionServices studentSubscriptionServices;

    public void create(StudentSubscription studentSubscription) {
        studentSubscriptionServices.create(studentSubscription);
    }

    public void update(StudentSubscription studentSubscription) {
        studentSubscriptionServices.update(studentSubscription);
    }

    public void delete(String studentCode, String serviceId) {
        studentSubscriptionServices.delete(studentCode, serviceId);
    }

    public StudentSubscription findById(String studentCode, String serviceId) {
        return studentSubscriptionServices.findById(studentCode, serviceId);
    }

    public boolean existsById(String studentCode, String serviceId) {
        return studentSubscriptionServices.existsById(studentCode, serviceId);
    }

    public List<StudentSubscription> findAll() {
        return studentSubscriptionServices.findAll();
    }

}
