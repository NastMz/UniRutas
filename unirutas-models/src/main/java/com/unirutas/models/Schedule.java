package com.unirutas.models;

import com.flexcore.annotations.Column;
import com.flexcore.annotations.PrimaryKey;
import com.flexcore.annotations.Table;

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

    public String getId() {
        return id;
    }

    public LocalTime getHour() {
        return hour;
    }
}
