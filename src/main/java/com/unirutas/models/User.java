package com.unirutas.models;

public abstract class User extends Person {
    private final String username;
    private String password;

    public User(String name, String code, String username, String password) {
        super(name, code);
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void insert(User user) {}

    public void update(User user) {}

}
