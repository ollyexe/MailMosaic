package it.edu.unito.eserver.model.Lock;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;


public class LockSystem {

    private final HashMap<String, ReentrantLock> locks;

    private static LockSystem instance = new LockSystem();
    private LockSystem(){
        locks = new HashMap<>();
    }

    public static LockSystem getInstance(){
        if (instance == null) {
            instance = new LockSystem() ;

        }

        return instance;
    }

    public void addLockEntry(String user){
        locks.putIfAbsent(user, new ReentrantLock(true));
    }
    public void removeLockEntry(String user){
        locks.remove(user);
    }
    public ReentrantLock getLock(String user){
        return locks.get(user);
    }

}


