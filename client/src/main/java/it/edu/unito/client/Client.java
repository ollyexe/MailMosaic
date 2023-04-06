package it.edu.unito.client;

import it.edu.unito.eclientlib.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static it.edu.unito.client.ClientApp.model;

public class Client {
    private final ExecutorService pool;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    static Properties prop;
    private Client(){
         prop = loadProp();
        String usr;
        if (Util.checkUser(prop.getProperty("client.usr"))){
            usr = prop.getProperty("client.usr");
        } else {
            throw new IllegalArgumentException();
        }

        pool = Executors.newFixedThreadPool(Integer.parseInt(prop.getProperty("client.threads_count")));



    }
    public static Client getInstance(){
        return new Client();
    }

    private Properties loadProp(){
        InputStream input = null;
        Properties proprieties = new Properties();
        try {
            input = new FileInputStream("client/src/main/resources/app.properties");

            // load a properties file
            proprieties.load(input);






        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return proprieties;
    }

//    public void sendRequest(OperationName reqType, Mail content,
//                            Controller controller,
//                            Object successArg){
//
//
//            processResponse(processRequest(new Request("gionni@gmail.com", reqType, content)), controller, successArg);
//    }

    public Response processRequest(Request request) {
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        Socket socket=null;
        // create a new Socket object and connect to the server on port 1234

        try {
            socket = new Socket("127.0.0.1", Integer.parseInt(prop.getProperty("client.server_port")));
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream  = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(request);
            outputStream.flush();

            return (Response) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

    }

//    public static void processResponse(Response resp,
//                                      Controller controller,
//                                      Object successArg){
//
//        if (resp == null) {
//            //no connection available
////            AlertManager.showAlert(
////                    controller.getDangerAlert(),
////                    AlertText.NO_CONNECTION);
////TODO Pop up rosso
//        }
//        else {
//            //connection is now available
////            AlertManager.hideAlert(ClientApp.sceneController
////                    .getController(SceneName.MAIN)
////                    .getDangerAlert(), 1);
////            AlertManager.hideAlert(ClientApp.sceneController
////                    .getController(SceneName.COMPOSE)
////                    .getDangerAlert(), 1);
//
//            //handle of all response cases
//            switch (resp.getResponseName()){
//                case SUCCESS -> successHandler.handle(
//                        successArg != null ?
//                                successArg :
//                                resp);
//                case ILLEGAL_PARAMS -> AlertManager.showTemporizedAlert(
//                        controller.getDangerAlert(),
//                        AlertText.ILLEGAL_PARAMS,
//                        2);
//                case INVALID_RECIPIENTS -> AlertManager.showTemporizedAlert(
//                        controller.getDangerAlert(),
//                        AlertText.INVALID_RECIPIENTS,
//                        2);
//                case OP_ERROR -> AlertManager.showTemporizedAlert(
//                        controller.getDangerAlert(),
//                        AlertText.OP_ERROR,
//                        2);
//            }
//        }
//    }

    public List<Mail> fetch(){

        Response resp = processRequest(new Request("gionni@gmail.com",OperationName.GET));//TODO mail

        List<Mail> l = resp.getContent()
                .stream()
                .filter(email -> !model.getInboxContent().contains(email))
                .toList();

        return l;
    }

    public void read(Mail mail){

                processRequest(new Request("gionni@gmail.com",OperationName.PUT,mail));

        //TODO mail

    }


}
