package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.dependency.annotations.Implementation;
import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Student;
import com.unirutas.models.User;
import com.unirutas.repository.StudentRepository;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;
import java.util.Map;

@Implementation
public class StudentServices implements UserServices<Student> {
    @Inject
    private StudentRepository studentRepository;
    public void create(User student) {
        studentRepository.save((Student) student);
    }

    public void update(User student) {
        studentRepository.update((Student) student);
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

    public Student findByUsername(String username) {
        return studentRepository.findByUsername(username);
    }
}
