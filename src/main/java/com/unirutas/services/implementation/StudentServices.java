package com.unirutas.services.implementation;

import com.unirutas.core.database.repository.interfaces.IRepository;
import com.unirutas.core.database.repository.utils.PrimaryKeyValues;
import com.unirutas.core.providers.RepositoryFactoryProvider;
import com.unirutas.models.Student;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;
import java.util.Map;

public class StudentServices implements UserServices<Student> {
    private final IRepository<Student> studentRepository;

    public StudentServices() {
        this.studentRepository = RepositoryFactoryProvider.getFactory().createRepository(Student.class);
    }

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
