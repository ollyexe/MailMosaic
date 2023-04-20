package it.edu.unito.client.model;

import it.edu.unito.eclientlib.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Properties;

import static it.edu.unito.client.ClientApp.mainController;

public class Client {

    private static Client instance=null;
    public static Properties prop;

    String usr;
    public String getUsr() {
        return usr;
    }
     public int threadNumber;
     public int fetchIntervall;

    public int getThreadNumber() {
        return threadNumber;
    }

    public int getFetchIntervall() {
        return fetchIntervall;
    }

    public int getPort() {
        return port;
    }

    public int port;



    private Client(){
            prop = loadProp();
            port = Integer.parseInt(prop.getProperty("client.server_port"));
            fetchIntervall= Integer.parseInt(prop.getProperty("client.fetch_interval"));
            threadNumber = Integer.parseInt(prop.getProperty("client.threads_count"));
            usr = prop.getProperty("client.usr");





    }
    public static Client getInstance(){
        if (instance==null)
            instance=new Client();

        return instance;
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



    public Response processRequest(Request request) {
        ObjectOutputStream outputStream ;
        ObjectInputStream inputStream ;
        // create a new Socket object and connect to the server on port x

        try (Socket socket = new Socket("127.0.0.1", port)){
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream  = new ObjectInputStream(socket.getInputStream());
            outputStream.writeObject(request);
            outputStream.flush();

            return (Response) inputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {

            AlertManager.showTemporizedAlert(mainController.dangerAlert, AlertText.NO_CONNECTION, 2);
            throw new RuntimeException("Connessione persa");

        }

    }


    public List<Mail> fetch(){

        Response resp = processRequest(new Request(usr,OperationName.GET));

        return resp.getContent()
                .stream()
                .filter(email -> !Model.getInstance().getInboxContent().contains(email))
                .toList();
    }

    public boolean send(Mail mail){

        Response resp = processRequest(new Request(usr,OperationName.POST,mail));

        return resp.getResponseName().equals(ResponseName.SUCCESS);
    }

    public void read(Mail mail){

                processRequest(new Request(usr,OperationName.PUT,mail));



    }


    public void delete(Mail selectedMail) {
        processRequest(new Request(usr, OperationName.DELETE, selectedMail));
    }


}
