package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Log.LogType;
import it.edu.unito.eclientlib.*;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static it.edu.unito.eserver.model.Log.LogManager.logResponse;

public class Send implements Operation{

    Request req;
    LogManager logManager;
    LockSystem lockSys;

    public Send(Request req) {
        this.req = req;
        logManager = ServerApp.unifier.getLogManager();
        lockSys = ServerApp.unifier.getLockSystem();
    }

    @Override
    public Response handle() {
        Mail email = req.getContent();
        ResponseName name;
        ReentrantReadWriteLock.WriteLock lock = lockSys.getLock(req.getSender()).writeLock();


        if (email == null){
            name = ResponseName.ILLEGAL_PARAMS;
            Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append("||").append(ResponseName.ILLEGAL_PARAMS)).toString(), LogType.WARNING) ));
        }

        else if (!email.getReceivers().stream().allMatch(receiver ->
                ServerApp.unifier.getDao().checkUser(receiver))){
            name = ResponseName.INVALID_RECIPIENTS;
            logResponse(name, req);
        }
        else {

            lock.lock();
            boolean result = ServerApp.unifier.getDao()
                    .save(email, email.getSender());
            lock.unlock();


            if (!result){
                name = ResponseName.OP_ERROR;
                Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                        (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                                .append("[Warning] :")
                                .append(ResponseName.OP_ERROR)).toString(), LogType.WARNING) ));
            }
            else {
                boolean allSends = true;
                //scrive le mail nelle caselle dei destinatari
                for (String receiver : email.getReceivers()) {
                    if (!Objects.equals(receiver, req.getSender())){
                       lockSys.addLockEntry(receiver);
                        lock = lockSys.getLock(receiver).writeLock();

                        lock.lock();
                        if (!ServerApp.unifier.getDao()
                                .save(email, receiver)) {
                            allSends = false;
                        }
                        lock.unlock();

                        lockSys.removeLockEntry(receiver);
                    }
                }

                name = (allSends) ?
                        ResponseName.SUCCESS :
                        ResponseName.OP_ERROR;
            }
            logResponse(name, req);
        }


        return new Response(name, null);

    }



}
