package it.edu.unito.eserver;

import it.edu.unito.eserver.DAO.Dao;
import it.edu.unito.eserver.Log.LogManager;

public class Unifier {
    private final Dao dao;
    private final LogManager logManager;

    public Unifier(){
        dao= new Dao();
        logManager= LogManager.getInstance();
    }

    public Dao getDao() {
        return dao;
    }

    public LogManager getLogManager() {
        return logManager;
    }
}
