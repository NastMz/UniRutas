package com.unirutas.core.database.manager.interfaces;

public interface IDatabaseManager {
    void connect();
    void disconnect();

    void getDate();
    void getHour();
}

