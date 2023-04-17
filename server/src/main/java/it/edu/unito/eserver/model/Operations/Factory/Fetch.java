package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Response;
import it.edu.unito.eclientlib.ResponseName;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.LogManager;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


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
        ReentrantLock lock = lockSys.getLock(req.getSender());

        lock.lock();
        mails = ServerApp.unifier.getDao().fetch(req.getSender()).stream().toList();

        lock.unlock();
        name = (mails!=null) ?
                ResponseName.SUCCESS :
                ResponseName.OP_ERROR;


        return new Response(name, mails);

    }
}
