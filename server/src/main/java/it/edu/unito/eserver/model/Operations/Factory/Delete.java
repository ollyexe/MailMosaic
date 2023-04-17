package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.*;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Log.LogType;
import javafx.application.Platform;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

import static it.edu.unito.eserver.model.Log.LogManager.logResponse;

public class Delete implements Operation{
    Request req;
    LogManager logManager;
    LockSystem lockSys;
    public Delete(Request req) {
        this.req = req;
        logManager = ServerApp.unifier.getLogManager();
        lockSys = ServerApp.unifier.getLockSystem();
    }


    @Override
    public Response handle()  {
        Mail email = req.getContent();
        ResponseName name;
        ReentrantLock lock = lockSys.getLock(req.getSender());
        if (email == null){
            name = ResponseName.ILLEGAL_PARAMS;
            Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append(ResponseName.ILLEGAL_PARAMS)).toString(), LogType.WARNING) ));
        }else {
            lock.lock();
            boolean result;
            try {
                result = ServerApp.unifier.getDao().delete(email,req.getSender());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            lock.unlock();

            name = (result) ?
                    ResponseName.SUCCESS :
                    ResponseName.OP_ERROR;

            logResponse(name, req);
        }


        return new Response(name, null);
    }
}
