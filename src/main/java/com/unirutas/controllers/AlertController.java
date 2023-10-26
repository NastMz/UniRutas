package com.unirutas.controllers;

import com.unirutas.core.dependency.annotations.Inject;
import com.unirutas.models.Alert;
import com.unirutas.services.implementation.AlertServices;

import java.util.List;

public class AlertController {
    @Inject
    private AlertServices alertServices;

    public void createAlert(Alert alert) {
        alertServices.create(alert);
    }

    public void updateAlert(Alert alert) {
        alertServices.update(alert);
    }

    public void deleteAlert(String id) {
        alertServices.delete(id);
    }

    public Alert findAlertById(String id) {
        return alertServices.findById(id);
    }

    public boolean existsAlertById(String id) {
        return alertServices.existsById(id);
    }

    public List<Alert> findAllAlerts() {
        return alertServices.findAll();
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
