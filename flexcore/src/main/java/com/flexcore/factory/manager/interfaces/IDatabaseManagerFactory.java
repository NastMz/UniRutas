package com.flexcore.factory.manager.interfaces;

import com.flexcore.database.manager.interfaces.IDatabaseManager;

public interface IDatabaseManagerFactory {
    IDatabaseManager createDatabaseManager();
}
