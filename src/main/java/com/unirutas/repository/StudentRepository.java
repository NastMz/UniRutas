package com.unirutas.repository;

import com.unirutas.core.database.repository.implementation.sql.SQLGenericRepository;
import com.unirutas.models.Student;

public class StudentRepository extends SQLGenericRepository<Student> {
    public StudentRepository() {
        super(Student.class);
    }
    // TODO: Implements specific methods for Student
}
