package com.unirutas.models;

import com.unirutas.annotations.Column;

public interface User extends Person {

    public String getUsername();

    public String getPassword();

    public void changePassword(String newPassword);

}
