package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eclientlib.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static it.edu.unito.eserver.model.Log.LogManager.logResponse;

public class Fetch implements Operation{

    Request req;
    LogManager logManager;
    LockSystem lockSys;

    public Fetch(Request req) {
        this.req = req;
        logManager = ServerApp.unifier.getLogManager();
        lockSys = ServerApp.unifier.getLockSystem();
    }

    @Override
    public Response handle() {
        List<Mail> mails;
        ResponseName name;
        ReentrantReadWriteLock.WriteLock lock = lockSys.getLock(req.getSender()).writeLock();

        lock.lock();
        mails = ServerApp.unifier.getDao().fetch(req.getSender()).stream().toList();

        lock.unlock();
        name = (mails.size()>=0) ?
                ResponseName.SUCCESS :
                ResponseName.OP_ERROR;




          logResponse(name, req);
        return new Response(name, mails);

    }
}
