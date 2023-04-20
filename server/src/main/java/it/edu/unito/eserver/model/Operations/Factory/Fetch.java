package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Response;
import it.edu.unito.eclientlib.ResponseName;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.DAO.Dao;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Loger;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Fetch implements Operation{

    Request req;


    public Fetch(Request req) {
        this.req = req;
    }

    @Override
    public Response handle() {
        List<Mail> mails;
        ResponseName name;
        ReentrantLock lock = LockSystem.getInstance().getLock(req.getSender());

        lock.lock();
        mails = Dao.getInstance().fetch(req.getSender()).stream().toList();

        lock.unlock();
        name = (mails!=null) ?
                ResponseName.SUCCESS :
                ResponseName.OP_ERROR;


        return new Response(name, mails);

    }


}
