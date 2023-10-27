package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.StudentSubscription;
import com.unirutas.repository.StudentSubscriptionRepository;

import java.util.List;
import java.util.Map;

public class StudentSubscriptionServices {
    @Inject
    private StudentSubscriptionRepository studentSubscriptionRepository;
    
    public void create(StudentSubscription studentSubscription) {
        studentSubscriptionRepository.save(studentSubscription);
    }

    public void update(StudentSubscription studentSubscription) {
        studentSubscriptionRepository.update(studentSubscription);
    }

    public void delete(String studentCode, String serviceId) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("student_code", studentCode, "service_id", serviceId));
        studentSubscriptionRepository.delete(primaryKeyValues);
    }

    public StudentSubscription findById(String studentCode, String serviceId) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("student_code", studentCode, "service_id", serviceId));
        return studentSubscriptionRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String studentCode, String serviceId) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("student_code", studentCode, "service_id", serviceId));
        return studentSubscriptionRepository.existsById(primaryKeyValues);
    }

    public List<StudentSubscription> findAll() {
        return studentSubscriptionRepository.findAll();
    }
}
