package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.*;
import it.edu.unito.eserver.model.DAO.Dao;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogType;
import it.edu.unito.eserver.model.Log.Loger;
import javafx.application.Platform;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import static it.edu.unito.eserver.model.Log.Loger.logResponse;

public class Send implements Operation{

    Request req;


    public Send(Request req) {
        this.req = req;
    }

    @Override
    public Response handle() {
        Mail email = req.getContent();
        ResponseName name;
        ReentrantLock lock = LockSystem.getInstance().getLock(req.getSender());

        if (email == null){
            name = ResponseName.ILLEGAL_PARAMS;
            Platform.runLater(()-> Loger.getInstance().printNewLog(new Log(
                    LocalDateTime.now().format(Util.formatter) +
                            "[Warning] :" +
                            "||" + ResponseName.ILLEGAL_PARAMS, LogType.WARNING) ));
        }
        else if (!email.getReceivers().stream().allMatch(receiver ->
                Dao.getInstance().checkUser(receiver))){
            List<String> nonExistent =  email.getReceivers().stream().filter(s -> !(Dao.getInstance().checkUser(s))).toList();
            List<String> existent =  email.getReceivers().stream().filter(s -> (Dao.getInstance().checkUser(s))).toList();
            name = ResponseName.INVALID_RECIPIENTS;
            lock.lock();
             Dao.getInstance()
                    .save(new Mail("admin@gmail.com", Collections.singletonList(email.getSender()),"SERVER_WARNING","the server refused the folowing receivers : "+ nonExistent), email.getSender());
             lock.unlock();
             List<String> validRecipients= email.getReceivers().stream().filter(s -> !nonExistent.contains(s)).toList();
             email.setReceivers(validRecipients);
            for (String receiver : existent) {
                if (!Objects.equals(receiver, req.getSender())){
                    // inserisco delle entry perche gli riceventi potrebbero eseguire delle operazioni in contemporanea
                    LockSystem.getInstance().addLockEntry(receiver);
                    lock = LockSystem.getInstance().getLock(receiver);

                    lock.lock();
                    Dao.getInstance()
                            .save(email, receiver);


                    lock.unlock();

                    LockSystem.getInstance().removeLockEntry(receiver);
                }
            }
            logResponse( ResponseName.INVALID_RECIPIENTS, req);
        }
        else {


                boolean allSends = true;
                //scrive le mail nelle caselle dei destinatari
            if (email.getReceivers().size()== 1&&email.getReceivers().get(0).equals(email.getSender())){//TODO if solo per i test di invio a ses stesso
                LockSystem.getInstance().addLockEntry(email.getSender());
                lock = LockSystem.getInstance().getLock(email.getSender());

                lock.lock();
                if (!Dao.getInstance()
                        .save(email, email.getSender())) {
                    allSends = false;
                }
                lock.unlock();

                LockSystem.getInstance().removeLockEntry(email.getSender());
            }else {
                for (String receiver : email.getReceivers()) {
                    if (!Objects.equals(receiver, req.getSender())){
                        // inserisco delle entry perche gli riceventi potrebbero eseguire delle operazioni in contemporanea
                        LockSystem.getInstance().addLockEntry(receiver);
                        lock = LockSystem.getInstance().getLock(receiver);

                        lock.lock();
                        if (!Dao.getInstance()
                                .save(email, receiver)) {
                            allSends = false;
                        }
                        lock.unlock();

                        LockSystem.getInstance().removeLockEntry(receiver);
                    }
                }
            }


                name = (allSends) ?
                        ResponseName.SUCCESS :
                        ResponseName.OP_ERROR;
            }
            logResponse(name, req);



        return new Response(name, null);

    }



}
