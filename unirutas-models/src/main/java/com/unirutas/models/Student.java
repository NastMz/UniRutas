package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

@Table(name = "Student")
public class Student implements User {
    @Column(name = "name")
    private String name;

    @PrimaryKey(name = "code")
    private String code;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "phone")
    private String phone;

    @Column(name = "security_phrase")
    private String securityPhrase;

    public Student(String name, String code, String username, String password) {
        this.name = name;
        this.code = code;
        this.username = username;
        this.password = password;
        this.phone = null;
        this.securityPhrase = null;
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
    public String getPhone() {
        return phone;
    }

    @Override
    public String getSecurityPhrase() {
        return securityPhrase;
    }

    @Override
    public void changePassword(String newPassword) {
        this.password=newPassword;
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public void setSecurityPhrase(String securityPhrase) {
        this.securityPhrase = securityPhrase;
    }
}

