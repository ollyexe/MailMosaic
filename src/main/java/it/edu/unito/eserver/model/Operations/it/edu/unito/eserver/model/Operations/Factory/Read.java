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
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Read implements Operation{

    Request req;
    LogManager logManager;
    LockSystem lockSys;

    public Read(Request req) {
        this.req = req;
        logManager = Run.u.getLogManager();
        lockSys = Run.u.getLockSystem();
    }

    @Override
    public Response handle() {
        Mail mail=req.getContent();
        ResponseName name;
        ReentrantReadWriteLock.WriteLock lock = lockSys.getLock(req.getSender()).writeLock();


        ReentrantReadWriteLock.WriteLock writeLock =
                lockSys.getLock(req.getSender()).writeLock();

        if (mail == null){
            name = ResponseName.ILLEGAL_PARAMS;
        }
        else {
            writeLock.lock();



            name = (Run.u.getDao()
                    .read(mail, req.getSender())) ?
                    ResponseName.SUCCESS :
                    ResponseName.OP_ERROR;

            writeLock.unlock();
        }
        Platform.runLater(()->Run.u.getLogManager().printNewLog(new Log(name.toString(), LogType.INFO) ));

        Platform.runLater(()->Run.u.getLogManager().printNewLog(new Log(req.getOpName().name(), LogType.WARNING) ));
        return new Response(name, null);
    }
}
