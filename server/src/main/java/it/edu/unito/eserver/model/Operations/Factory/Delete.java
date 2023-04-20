package it.edu.unito.eserver.model.Operations.Factory;

import it.edu.unito.eclientlib.*;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.DAO.Dao;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.Loger;
import it.edu.unito.eserver.model.Log.LogType;
import javafx.application.Platform;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

import static it.edu.unito.eserver.model.Log.Loger.logResponse;

public class Delete implements Operation{
    Request req;

    public Delete(Request req) {
        this.req = req;
    }


    @Override
    public Response handle()  {
        Mail email = req.getContent();
        ResponseName name;
        ReentrantLock lock = LockSystem.getInstance().getLock(req.getSender());
        if (email == null){
            name = ResponseName.ILLEGAL_PARAMS;
            Platform.runLater(()-> Loger.getInstance().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append(ResponseName.ILLEGAL_PARAMS)).toString(), LogType.WARNING) ));
        }else {
            lock.lock();
            boolean result;
            try {
                result = Dao.getInstance().delete(email,req.getSender());

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

    //controlla se l utente essite controllando se esiste una cartella con l username dell utente
    public static boolean checkUser(String receiver) {
        String memory;
        String[] dirs = new File(new File("").getAbsolutePath() +"/server/src/main/java/it/edu/unito/eserver/memory").list(
                (current, name) -> new File(current, name)
                        .isDirectory());
        return dirs != null && dirs.length != 0 &&
                Arrays.stream(dirs).toList().contains(receiver);
    }
}
