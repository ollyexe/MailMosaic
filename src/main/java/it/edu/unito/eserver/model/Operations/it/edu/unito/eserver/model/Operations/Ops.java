package it.edu.unito.eserver.model.Operations;

import it.edu.unito.eserver.Run;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Operations.Factory.Fetch;
import it.edu.unito.eserver.model.Operations.Factory.OperationFactory;
import it.edu.unito.oModels.Request;
import it.edu.unito.oModels.Response;
import javafx.application.Platform;
import it.edu.unito.eserver.model.Log.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Ops  implements Runnable{
    Socket socket;
    LogManager logManager;
    LockSystem lockSys;

    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    public Ops(Socket socket) {
        this.socket = socket;
        logManager = Run.u.getLogManager();
        lockSys = Run.u.getLockSystem();

    }

    @Override
    public void run() {
        try {

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Request rq = (Request) inputStream.readObject();
            lockSys.addLockEntry(rq.getSender());
            Response response = new OperationFactory(rq).produce().handle();//factory da implementare
            lockSys.removeLockEntry(rq.getSender());
           // System.out.println("Received Value : "+ a);
            outputStream.flush();
            outputStream.writeObject(response);
            Platform.runLater(()->logManager.printNewLog(new Log("Content received  "+rq.getOpName(), LogType.WARNING)));
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
