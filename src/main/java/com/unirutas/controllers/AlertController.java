package com.unirutas.controllers;

import com.unirutas.models.Alert;
import com.unirutas.models.User;

import java.util.List;

public class AlertController {
    public void notifyAlert(Alert alert, List<User> users) {
        // Llama al método enviarAlerta de la alert para notificar a los users
//        alert.enviarAlerta(users);
        System.out.println("Alerta notificada a los usuarios.");
    }

    public void listAlert(List<Alert> alerts) {
        System.out.println("Lista de Alertas:");
        for (Alert alert : alerts) {
            System.out.println("Fecha: " + alert.getDate());
            System.out.println("Descripción: " + alert.getDescription());
            System.out.println("Servicio: " + alert.getService());
            System.out.println("------------------------");
        }
    }
}
