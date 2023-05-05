package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Response;
import it.edu.unito.eclientlib.ResponseName;
import it.edu.unito.eserver.model.DAO.Dao;
import it.edu.unito.eserver.model.Lock.LockSystem;

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
        }
        else {
            lock.lock();
            name = (Dao.getInstance()
                    .read(mail, req.getSender())) ?
                    ResponseName.SUCCESS :
                    ResponseName.OP_ERROR;
            lock.unlock();
        }

        logResponse(name, req);


        return new Response(name, null);

    }
}
