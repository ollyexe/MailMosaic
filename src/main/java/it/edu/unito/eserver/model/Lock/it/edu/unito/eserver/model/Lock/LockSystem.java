package it.edu.unito.eserver.model.Lock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 Classe che gestisce l acesso concorrente al db e alle varie operazioni,creando un ReentrantReadWriteLock per ogni utente.
 L utente connetendosi si va a creare un lock, quando si disconnette il lock viene eliminato.
 */
public class LockSystem {

    private final ConcurrentHashMap<String, ReentrantReadWriteLock> locks;

    private static LockSystem instance = new LockSystem();
    private LockSystem(){
        locks = new ConcurrentHashMap<>();
    }

    public static LockSystem getInstance(){
        if (instance == null) {
            instance = new LockSystem() ;
            System.out.println("new instance");
        }
        System.out.println("already existing instance");
        return instance;
    }

    public void addLockEntry(String user){
        locks.putIfAbsent(user, new ReentrantReadWriteLock());
    }
    public void removeLockEntry(String user){
        locks.remove(user);
    }
    public ReentrantReadWriteLock getLock(String user){
        return locks.get(user);
    }

}


