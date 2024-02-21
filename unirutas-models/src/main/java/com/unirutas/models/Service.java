package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

import java.util.List;
import java.util.UUID;

@Table(name="Service")
public class Service {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "route_id")
    private final String routeId;

    public Service(String routeId) {
        this.id = String.valueOf(UUID.randomUUID());
        this.routeId = routeId;
    }

    public String getId() {
        return id;
    }

    public String getRouteId() {
        return routeId;
    }

    public List<Bus> getBuses() {
        return ServiceBus.getBusForService(this.id);
    }

    public void addBus(Bus bus) {
        ServiceBus.insertBus(this.id, bus.getId());
    }

    public void removeBus(Bus bus) {
        ServiceBus.removeBus(this.id, bus.getId());
    }

    public List<Schedule> getSchedules() {
        return ServiceSchedule.getScheduleForService(this.id);
    }

    public void addSchedule(Schedule schedule) {
        ServiceSchedule.insertSchedule(this.id, schedule.getId());
    }

    public void removeSchedule(Schedule schedule) {
        ServiceSchedule.removeSchedule(this.id, schedule.getId());
    }

    public List<Alert> getAlerts() {
        return ServiceAlert.getAlertForService(this.id);
    }

    public void addAlert(Alert alert) {
        ServiceAlert.insertAlert(this.id, alert.getId());
    }

    public void removeAlert(Alert alert) {
        ServiceAlert.removeAlert(this.id, alert.getId());
    }
}
