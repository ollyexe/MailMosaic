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

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Send implements Operation{

    Request req;
    LogManager logManager;
    LockSystem lockSys;

    public Send(Request req) {
        this.req = req;
        logManager = Run.u.getLogManager();
        lockSys = Run.u.getLockSystem();
    }

    @Override
    public Response handle() {
        Mail email = req.getContent();
        ResponseName name;
        ReentrantReadWriteLock.WriteLock lock = lockSys.getLock(req.getSender()).writeLock();
//        ReentrantReadWriteLock.WriteLock writeLock =
//                syncManager.getLock(req.auth()).writeLock();

        //check for null object or if the recipients of the email actually exist.
        if (email == null){
            name = ResponseName.ILLEGAL_PARAMS;
        }
        else if (!email.getReceivers().stream().allMatch(receiver ->
                Run.u.getDao().checkUser(receiver))){
            name = ResponseName.INVALID_RECIPIENTS;
        }
        else {

            lock.lock();
            boolean result = Run.u.getDao()
                    .save(email, email.getSender());
            lock.unlock();


            if (!result){
                name = ResponseName.OP_ERROR;
                Platform.runLater(()->Run.u.getLogManager().printNewLog(new Log(ResponseName.OP_ERROR.toString(), LogType.WARNING) ));

            }
            else {
                boolean allSends = true;
                //scrive le mail nelle caselle dei destinatari
                for (String receiver : email.getReceivers()) {
                    if (!Objects.equals(receiver, req.getSender())){
                       lockSys.addLockEntry(receiver);
                        lock = lockSys.getLock(receiver).writeLock();

                        lock.lock();
                        if (!Run.u.getDao()
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
        }

        Platform.runLater(()->Run.u.getLogManager().printNewLog(new Log(req.toString(), LogType.INFO) ));


        return new Response(name, null);
    }
}
