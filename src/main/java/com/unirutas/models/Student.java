package com.unirutas.models;

import com.unirutas.repository.GenericDTO;

import java.util.List;

public class Student extends User {
    public Student(String name, String code, String username, String password) {
        super(name, code, username, password);
    }

    @Override
    public void insert(User user) {
        GenericDTO<Student> estudianteDTO = new GenericDTO<>(Student.class, "Student", List.of("code"));
        estudianteDTO.insert((Student) user);
    }

    @Override
    public void update(User user) {
        GenericDTO<Student> estudianteDTO = new GenericDTO<>(Student.class, "Student", List.of("code"));
        estudianteDTO.update((Student) user);
    }
}

