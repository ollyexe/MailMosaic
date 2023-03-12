package it.edu.unito.eserver;



import java.io.*;
import java.net.Socket;

public class Client {
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    public static void main(String[] args) throws IOException {
        // create a new Socket object and connect to the server on port 1234
        Socket socket = new Socket("127.0.0.1", 40000);
        System.out.println("Connected to server: " + socket.getInetAddress());

        // create the input and output streams
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream  = new ObjectInputStream(socket.getInputStream());

        try {
            Integer i = 1;
            outputStream.writeObject(i);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}