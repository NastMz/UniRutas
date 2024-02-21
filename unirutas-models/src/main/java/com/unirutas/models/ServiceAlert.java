package com.unirutas.models;

import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.List;

@Table(name="ServiceAlert")
public class ServiceAlert {
    @PrimaryKey(name = "service_id")
    private String serviceId;
    @PrimaryKey(name = "alert_id")
    private String alertId;

    public ServiceAlert(String serviceId, String alertId) {
        this.serviceId = serviceId;
        this.alertId = alertId;
    }

    public static List<Alert> getAlertForService(String serviceId) {
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertAlert(String serviceId, String alertId) {
        // TODO: Implementa esta lógica
    }

    public static void removeAlert(String serviceId, String alertId) {
        // TODO: Implementa esta lógica
    }
}
