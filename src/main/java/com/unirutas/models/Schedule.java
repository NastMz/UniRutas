package com.unirutas.models;

import java.time.LocalTime;
import java.util.UUID;

@Table(name="Schedule")
public class Schedule {
    @PrimaryKey(name = "id")
    private String id;
    @Column(name = "hour")
    private final LocalTime hour;

    public Schedule(LocalTime hour) {
        this.id = String.valueOf(UUID.randomUUID());
        this.hour = hour;
    }

    public LocalTime getHour() {
        return hour;
    }
}
