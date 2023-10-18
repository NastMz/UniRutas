package com.unirutas.services.implementation;

import com.unirutas.models.Student;
import com.unirutas.repository.implementation.StudentRepository;
import com.unirutas.repository.interfaces.IRepository;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;

public class StudentServices implements UserServices<Student> {
    private final IRepository<Student, Object> studentRepository = new StudentRepository();

    public void create(String name, String code, String username, String password) {
        studentRepository.save(new Student(name, code, username, password));
    }

    public void update(String name, String code, String username, String password) {
        studentRepository.update(new Student(name, code, username, password));
    }

    public void delete(String code) {
        studentRepository.delete(code);
    }

    public Student findByCode(String code) {
        return studentRepository.findById(code);
    }

    public boolean existsByCode(String code) {
        return studentRepository.existsById(code);
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }
}
