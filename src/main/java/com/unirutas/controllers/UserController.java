package com.unirutas.controllers;

import com.unirutas.models.User;
import com.unirutas.services.interfaces.UserServices;

public class UserController {

    private UserServices<? extends User> userServices;

    // Inyección de dependencias por constructor
    public UserController(UserServices<? extends User> userServices) {
        this.userServices = userServices;
    }


    public void createUser(User user) {
        userServices.create(user.getName(), user.getCode(), user.getUsername(), user.getPassword());
    }

    public void updateUser(User user) {
        userServices.update(user.getName(), user.getCode(), user.getUsername(), user.getPassword());
    }

//    public void marcarAlerta(Alert alerta, User usuario) {
//        // TODO: Implementar la lógica para añadirlo en la DB
//        if (usuario != null) {
//            usuario.marcarAlertaLeida(alerta);
//            System.out.println("Alerta marcada como leída por el usuario: " + usuario.getNombre());
//        } else {
//            System.out.println("Error: Usuario no encontrado.");
//        }
//    }

//    public void subscribeService(Student student, Service service) {
//        // TODO: Implementar la lógica para añadirlo en la DB
//        if (student != null && service != null) {
////            student.suscribirAServicio(service);
//            System.out.println(student.getName() + " se ha suscrito al service " + service);
//        } else {
//            System.out.println("Error: Estudiante o service no encontrados.");
//        }
//    }

}


