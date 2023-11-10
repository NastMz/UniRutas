package com.unirutas.controllers;

import com.unirutas.models.User;
import com.unirutas.services.interfaces.UserServices;

import java.util.List;

public class UserController {

    private final UserServices<? extends User> userServices;

    // Inyección de dependencias por constructor
    public UserController(UserServices<? extends User> userServices) {
        this.userServices = userServices;
    }


    public void createUser(User user) {
        userServices.create(user);
    }

    public void updateUser(User user) {
        userServices.update(user);
    }

    public List<User> findAllUsers() {
        return (List<User>) userServices.findAll();
    }

    public User findByUsername(String username) {
        return userServices.findByUsername(username);
    }

}


