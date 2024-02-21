package com.unirutas.services.implementation;

import com.flexcore.database.repository.utils.PrimaryKeyValues;
import com.flexcore.dependency.annotations.Inject;
import com.unirutas.models.StudentAlert;
import com.unirutas.repository.StudentAlertRepository;

import java.util.List;
import java.util.Map;

public class StudentAlertServices {
    @Inject
    private StudentAlertRepository studentAlertRepository;
    
    public void create(StudentAlert studentAlert) {
        studentAlertRepository.save(studentAlert);
    }

    public void update(StudentAlert studentAlert) {
        studentAlertRepository.update(studentAlert);
    }

    public void delete(String studentCode, String alertId) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("student_code", studentCode, "alert_id", alertId));
        studentAlertRepository.delete(primaryKeyValues);
    }

    public StudentAlert findById(String studentCode, String alertId) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("student_code", studentCode, "alert_id", alertId));
        return studentAlertRepository.findById(primaryKeyValues);
    }

    public boolean existsById(String studentCode, String alertId) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("student_code", studentCode, "alert_id", alertId));
        return studentAlertRepository.existsById(primaryKeyValues);
    }

    public List<StudentAlert> findAll() {
        return studentAlertRepository.findAll();
    }
}
