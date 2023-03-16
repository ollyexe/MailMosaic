package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eserver.Run;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Log.LogType;
import it.edu.unito.oModels.Mail;
import it.edu.unito.oModels.Request;
import it.edu.unito.oModels.Response;
import it.edu.unito.oModels.ResponseName;
import javafx.application.Platform;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Fetch implements Operation{

    Request req;
    LogManager logManager;
    LockSystem lockSys;

    public Fetch(Request req) {
        this.req = req;
        logManager = Run.u.getLogManager();
        lockSys = Run.u.getLockSystem();
    }

    @Override
    public Response handle() {
        List<Mail> mails;
        ResponseName name;
        ReentrantReadWriteLock.WriteLock lock = lockSys.getLock(req.getSender()).writeLock();


        lock.lock();
        mails = Run.u.getDao().fetch(req.getSender()).stream().toList();

        lock.unlock();
        name = (mails.size()>=0) ?
                ResponseName.SUCCESS :
                ResponseName.OP_ERROR;

        Platform.runLater(()->Run.u.getLogManager().printNewLog(new Log(name.toString(), LogType.INFO) ));


        return new Response(name, mails);
    }
}
