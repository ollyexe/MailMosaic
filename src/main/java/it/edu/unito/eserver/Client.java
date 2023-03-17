package it.edu.unito.eserver;



import it.edu.unito.oModels.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Client {
    private static ObjectOutputStream outputStream;
    private static ObjectInputStream inputStream;
    public static void main(String[] args) throws IOException {
        // create a new Socket object and connect to the server on port 1234
        Socket socket = new Socket("127.0.0.1", 40000);
        System.out.println("Connected to server: " + socket.getInetAddress()+":"+40000);

        // create the input and output streams
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream  = new ObjectInputStream(socket.getInputStream());
        User u = new User("olly@gmail.com");
        try {

            //per put e delete serve invocare il costruttore mail con data , perche dal client si ha l entita con il timestamp



            //PUT
//            outputStream.writeObject(new Request("gionni@gmail.com",OperationName.PUT,new Mail(u.getEmail(), List.of("gionni@gmail.com"),"lavoro","gg" )));
//            outputStream.flush();
//            Response r = (Response) inputStream.readObject();
//            System.out.println(r.getResponseName());


            //POST
//            outputStream.writeObject(new Request(u.getEmail(),OperationName.POST,new Mail(u.getEmail(), List.of("gionni@gmail.com"),"lavoro","gg" )));
//            outputStream.flush();
//            Response r = (Response) inputStream.readObject();
//            System.out.println(r.getResponseName());


            //GET
            outputStream.writeObject(new Request("gionni@gmail.com",OperationName.GET));
            outputStream.flush();
            Response r = (Response) inputStream.readObject();
            System.out.println(r.getContent());

            //Put
//            outputStream.writeObject(new Request("gionni@gmail.com",OperationName.PUT,new Mail(u.getEmail(), List.of("gionni@gmail.com"),"lavoro","gg" )));
//            outputStream.flush();
//            Response r = (Response) inputStream.readObject();
//            System.out.println(r.getResponseName());

//            outputStream.writeObject(new Request("gionni@gmail.com",OperationName.PUT,r.getContent().get(0)));
//            outputStream.flush();
//            r = (Response) inputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}