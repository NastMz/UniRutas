package com.unirutas.models;

import java.time.LocalTime;

public class Schedule {
    private final LocalTime hour;

    public Schedule(LocalTime hour) {
        this.hour = hour;
    }

    public LocalTime getHour() {
        return hour;
    }
}
