package it.edu.unito.oModels;

import it.edu.unito.eserver.Log.Log;
import it.edu.unito.eserver.Log.LogManager;
import it.edu.unito.eserver.Log.LogType;
import it.edu.unito.eserver.Test;

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
        logManager = Test.u.getLogManager();
    }

    @Override
    public void run() {
        try {
            System.out.println("Ops");
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            Integer a = (Integer) inputStream.readObject();
            System.out.println("Received Value : "+ a);
            logManager.printNewLog(new Log("Content received"+a, LogType.WARNING));
            outputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
