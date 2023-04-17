package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.*;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Log.LogType;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

import static it.edu.unito.eserver.model.Log.LogManager.logResponse;

public class Read implements Operation{

    Request req;
    LogManager logManager;
    LockSystem lockSys;

    public Read(Request req) {
        this.req = req;
        logManager = ServerApp.unifier.getLogManager();
        lockSys = ServerApp.unifier.getLockSystem();
    }

    @Override
    public Response handle() {
        Mail mail=req.getContent();
        ResponseName name;
        ReentrantLock lock = lockSys.getLock(req.getSender());

        if (mail == null){
            name = ResponseName.ILLEGAL_PARAMS;
            Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append(ResponseName.ILLEGAL_PARAMS)).toString(), LogType.WARNING) ));
        }
        else {
            lock.lock();
            name = (ServerApp.unifier.getDao()
                    .read(mail, req.getSender())) ?
                    ResponseName.SUCCESS :
                    ResponseName.OP_ERROR;
            lock.unlock();
            logResponse(name, req);
        }



        return new Response(name, null);

    }
}
