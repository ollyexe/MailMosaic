package it.edu.unito.eserver;


import it.edu.unito.oModels.Mail;
import it.edu.unito.oModels.OperationName;
import it.edu.unito.oModels.Request;
import it.edu.unito.oModels.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {

    public static void main(String[] args) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        System.out.println(call(new Request("olly@gmail.com",OperationName.PUT,new Mail("gionni@gmail.com", List.of("olly@gmail.com"),"albergo","gg", LocalDateTime.parse("25/05/2023 12:12:12",formatter)))).getResponseName());

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println(Objects.requireNonNull(call(new Request("olly@gmail.com", OperationName.GET))).getContent().stream().filter(mail -> mail.getReceivers().contains("olly@gmail.com")).collect(Collectors.toList()));


    }


    public static Response call(Request request) {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        Socket socket=null;
        // create a new Socket object and connect to the server on port 1234

           try {
               socket = new Socket("127.0.0.1", 40000);
               outputStream = new ObjectOutputStream(socket.getOutputStream());
               inputStream  = new ObjectInputStream(socket.getInputStream());
               outputStream.writeObject(request);
               outputStream.flush();

               return (Response) inputStream.readObject();
           } catch (ClassNotFoundException | IOException e) {
               throw new RuntimeException(e);
           }finally {
               try{
                   assert inputStream != null;
                   inputStream.close();
                   outputStream.close();
                   socket.close();

               } catch (IOException ignored) {
               }

           }

    }
}
