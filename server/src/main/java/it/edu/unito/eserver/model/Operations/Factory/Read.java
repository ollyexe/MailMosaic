package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.*;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.DAO.Dao;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.Loger;
import it.edu.unito.eserver.model.Log.LogType;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

import static it.edu.unito.eserver.model.Log.Loger.logResponse;

public class Read implements Operation{

    Request req;


    public Read(Request req) {
        this.req = req;
    }

    @Override
    public Response handle() {
        Mail mail=req.getContent();
        ResponseName name;
        ReentrantLock lock = LockSystem.getInstance().getLock(req.getSender());

        if (mail == null){
            name = ResponseName.ILLEGAL_PARAMS;
            Platform.runLater(()-> Loger.getInstance().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append(ResponseName.ILLEGAL_PARAMS)).toString(), LogType.WARNING) ));
        }
        else {
            lock.lock();
            name = (Dao.getInstance()
                    .read(mail, req.getSender())) ?
                    ResponseName.SUCCESS :
                    ResponseName.OP_ERROR;
            lock.unlock();
            logResponse(name, req);
        }



        return new Response(name, null);

    }
}
