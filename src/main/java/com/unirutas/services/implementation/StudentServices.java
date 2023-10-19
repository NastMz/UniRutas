package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.dependency.annotations.Implementation;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Student;
import com.unirutas.repository.StudentRepository;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;
import java.util.Map;

@Implementation
public class StudentServices implements UserServices<Student> {
    @Inject
    private StudentRepository studentRepository;

//    public StudentServices() {
//        studentRepository.getDatabaseEngineDate();
//        studentRepository.getDatabaseEngineHour();
//    }

    public void create(String name, String code, String username, String password) {
        studentRepository.save(new Student(name, code, username, password));
    }

    public void update(String name, String code, String username, String password) {
        studentRepository.update(new Student(name, code, username, password));
    }

    public void delete(String code) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("code", code));
        studentRepository.delete(primaryKeyValues);
    }

    public Student findByCode(String code) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("code", code));
        return studentRepository.findById(primaryKeyValues);
    }

    public boolean existsByCode(String code) {
        PrimaryKeyValues primaryKeyValues = new PrimaryKeyValues(Map.of("code", code));
        return studentRepository.existsById(primaryKeyValues);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
