package com.unirutas.models;

import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.List;

@Table(name="ServiceSchedule")
public class ServiceSchedule {
    @PrimaryKey(name = "service_id")
    private String serviceId;
    @PrimaryKey(name = "schedule_id")
    private String scheduleId;

    public ServiceSchedule(String serviceId, String scheduleId) {
        this.serviceId = serviceId;
        this.scheduleId = scheduleId;
    }

    public static List<Schedule> getScheduleForService(String serviceId) {
        // TODO: Implementa esta lógica
        return null;
    }

    public static void insertSchedule(String serviceId, String scheduleId) {
        // TODO: Implementa esta lógica
    }

    public static void removeSchedule(String serviceId, String scheduleId) {
        // TODO: Implementa esta lógica
    }
}
