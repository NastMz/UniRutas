package com.unirutas.repository.implementation;

import com.unirutas.models.Student;

public class StudentRepository extends GenericRepository<Student> {
    public StudentRepository() {
        super(Student.class);
    }
}
