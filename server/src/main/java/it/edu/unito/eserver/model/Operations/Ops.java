package it.edu.unito.eserver.model.Operations;

import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Response;
import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Lock.LockSystem;
import it.edu.unito.eserver.model.Log.Loger;
import it.edu.unito.eserver.model.Operations.Factory.OperationFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Ops  implements Runnable{
    Socket socket;

    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    public Ops(Socket socket) {
        this.socket = socket;


    }

    @Override
    public void run() {
        try {

            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Request rq = (Request) inputStream.readObject();
            LockSystem.getInstance().addLockEntry(rq.getSender());
            Response response = new OperationFactory(rq).produce().handle();
            LockSystem.getInstance().removeLockEntry(rq.getSender());


            outputStream.writeObject(response);
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            try {
                //chiudo la connesione
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
