package it.edu.unito.eserver.model.Operations;

import it.edu.unito.eserver.Run;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogType;
import it.edu.unito.oModels.Mail;
import it.edu.unito.oModels.Request;
import it.edu.unito.oModels.Response;
import it.edu.unito.oModels.ResponseName;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Send {

    Request req;

    public Send(Request req) {
        this.req = req;
    }

    public Response handle() {
        Mail email = req.getContent();
        ResponseName name;
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

            //writeLock.lock();
            boolean result = Run.u.getDao()
                    .save(email, email.getSender());
            //writeLock.unlock();

            if (!result){
                name = ResponseName.OP_ERROR;
            }
            else {
                email.setRead(true);
                boolean allSends = true;

                for (String receiver : email.getReceivers()) {
                    if (!Objects.equals(receiver, req.getSender())){
                       // syncManager.addLockEntry(receiver);
//                        writeLock = syncManager.getLock(receiver).writeLock();
//
//                        writeLock.lock();
                        if (!Run.u.getDao()
                                .save(email, receiver)) {
                            allSends = false;
                        }
//                        writeLock.unlock();
//
//                        syncManager.removeLockEntry(receiver);
                    }
                }

                name = (allSends) ?
                        ResponseName.SUCCESS :
                        ResponseName.OP_ERROR;
            }
        }

        Run.u.getLogManager().printNewLog(new Log(name.toString()+"Tipo Log", LogType.INFO) );
        return new Response(name, null);
    }
}
