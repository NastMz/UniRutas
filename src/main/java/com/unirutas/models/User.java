package com.unirutas.models;

public interface User extends Person {

    public String getUsername();

    public String getPassword();

    public String getPhone();

    public String getSecurityPhrase();

    public void changePassword(String newPassword);

    public void setPhone(String phone);

    public void setSecurityPhrase(String securityPhrase);
}
