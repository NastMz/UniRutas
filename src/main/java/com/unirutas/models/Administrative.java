package com.unirutas.models;

import com.unirutas.core.annotations.Column;
import com.unirutas.core.annotations.PrimaryKey;
import com.unirutas.core.annotations.Table;

@Table(name = "Administrative")
public class Administrative implements User {
    @Column(name = "name")
    private String name;
    @PrimaryKey(name = "code")
    private String code;
    @Column(name = "username")
    private final String username;
    @Column(name = "password")
    private String password;
    @Column(name = "security_phrase")
    private String securityPhrase;

    public Administrative(String name, String code, String username, String password) {
        this.name = name;
        this.code = code;
        this.username = username;
        this.password = password;
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
    public void changePassword(String newPassword) {
        this.password=newPassword;
    }

    @Override
    public void setSecurityPhrase(String securityPhrase) {
        this.securityPhrase = securityPhrase;
    }
}

