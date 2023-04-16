package it.edu.unito.eserver.model.Operations;

import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Response;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Operations.Factory.OperationFactory;

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
        logManager = ServerApp.unifier.getLogManager();
        lockSys = ServerApp.unifier.getLockSystem();

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
//            Platform.runLater(()->logManager.printNewLog(new Log(response.getResponseName().toString(),response.getResponseName().equals(ResponseName.SUCCESS)?LogType.INFO:LogType.WARNING)));
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
                System.out.println("Close");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
