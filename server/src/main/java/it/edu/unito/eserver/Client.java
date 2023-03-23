package it.edu.unito.eserver;


import it.edu.unito.eclientlib.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {

    public static void main(String[] args) throws IOException {

        System.out.println(call(new Request("gionni@gmail.com",OperationName.POST,new Mail("gionni@gmail.com", List.of("olly@gmail.com","gionni@gmail.com"),"lavoro notturno","hello", LocalDateTime.parse("25/05/2023 12:12:12", Util.formatter)))).getContent());

        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        //System.out.println(Objects.requireNonNull(call(new Request("olly@gmail.com", OperationName.GET))).getContent().stream().filter(mail -> mail.getReceivers().contains("olly@gmail.com")).collect(Collectors.toList()));

        //System.out.println(call(new Request("olly@gmail.com",OperationName.DELETE)));


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
