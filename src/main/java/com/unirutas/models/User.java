package com.unirutas.models;

public interface User extends Person {

    public String getUsername();

    public String getPassword();

    public void changePassword(String newPassword);

}
