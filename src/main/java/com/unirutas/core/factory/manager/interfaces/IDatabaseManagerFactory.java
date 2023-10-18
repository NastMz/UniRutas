package com.unirutas.core.factory.manager.interfaces;

import com.unirutas.core.database.manager.interfaces.IDatabaseManager;

public interface IDatabaseManagerFactory {
    IDatabaseManager createDatabaseManager();
}
