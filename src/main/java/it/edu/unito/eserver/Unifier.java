package it.edu.unito.eserver;

import it.edu.unito.eserver.model.DAO.Dao;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.LogManager;

import java.util.concurrent.locks.Lock;

public class Unifier {
    private final Dao dao;
    private final LogManager logManager;

    private final LockSystem lockSystem;

    public Unifier(){
        dao= Dao.getInstance();
        logManager= LogManager.getInstance();
        lockSystem = LockSystem.getInstance();
    }

    public Dao getDao() {
        return dao;
    }

    public LogManager getLogManager() {
        return logManager;
    }

    public LockSystem getLockSystem() {
        return lockSystem;
    }
}
