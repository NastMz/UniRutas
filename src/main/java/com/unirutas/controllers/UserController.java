package com.unirutas.controllers;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.User;
import com.unirutas.services.interfaces.UserServices;
import com.unirutas.auth.services.AuthenticationService;

import java.util.List;

public class UserController {

    private final UserServices<? extends User> userServices;
    @Inject
    private AuthenticationService authenticationService;

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

    public void changePassword(User user, String newPassword){
        if (authenticationService != null) {
            authenticationService.changePassword(user, newPassword);
        } else {
            System.out.println("authenticationService is null");
        }
    }

    }




