package com.unirutas.models;

import com.unirutas.annotations.Column;
import com.unirutas.annotations.PrimaryKey;
import com.unirutas.annotations.Table;

@Table(name = "Student")
@PrimaryKey(value = {"code"})
public class Student implements User {
    @Column(name = "name")
    private String name;
    private String code;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    public Student(String name, String code, String username, String password) {
        this.name = name;
        this.code = code;
        this.username = username;
        this.password = password;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void changePassword(String newPassword) {
        this.password=newPassword;
    }
}

