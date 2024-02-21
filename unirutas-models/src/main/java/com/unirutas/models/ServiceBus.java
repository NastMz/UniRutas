package com.unirutas.models;

import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.List;

@Table(name="ServiceBus")
public class ServiceBus {
    @PrimaryKey(name = "service_id")
    private String serviceId;
    @PrimaryKey(name = "bus_id")
    private String busId;

    public ServiceBus(String serviceId, String busId) {
        this.serviceId = serviceId;
        this.busId = busId;
    }

    public static List<Bus> getBusForService(String serviceId) {
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertBus(String serviceId, String busId) {
        // TODO: Implementa esta lógica
    }

    public static void removeBus(String serviceId, String busId) {
        // TODO: Implementa esta lógica
    }
}
