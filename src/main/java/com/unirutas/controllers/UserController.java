package com.unirutas.controllers;

import com.unirutas.models.*;

public class UserController {

    public void createUser(User user) {
        user.insert(user);
    }

    public void updateUser(User user) {
        user.update(user);
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

    public void subscribeService(Student student, Service service) {
        // TODO: Implementar la lógica para añadirlo en la DB
        if (student != null && service != null) {
//            student.suscribirAServicio(service);
            System.out.println(student.getName() + " se ha suscrito al service " + service);
        } else {
            System.out.println("Error: Estudiante o service no encontrados.");
        }
    }

}


