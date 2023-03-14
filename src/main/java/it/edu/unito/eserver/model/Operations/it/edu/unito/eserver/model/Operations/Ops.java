package it.edu.unito.eserver.model.Operations;

import it.edu.unito.eserver.Run;
import it.edu.unito.eserver.model.Log.LogManager;
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

    ObjectInputStream inputStream;
    ObjectOutputStream outputStream;

    public Ops(Socket socket) {
        this.socket = socket;
        logManager = Run.u.getLogManager();
    }

    @Override
    public void run() {
        try {
            //System.out.println("Ops");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Request rq = (Request) inputStream.readObject();
            Response response = new Send(rq).handle();
           // System.out.println("Received Value : "+ a);
            outputStream.flush();
            outputStream.writeObject(response);
            Platform.runLater(()->logManager.printNewLog(new Log("Content received  "+rq.getOpName(), LogType.WARNING)));
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
