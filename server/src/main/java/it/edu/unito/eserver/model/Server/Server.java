package it.edu.unito.eserver.model.Server;

import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.LogManager;
import it.edu.unito.eserver.model.Log.LogType;
import it.edu.unito.eserver.model.Operations.Ops;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{

    public final Properties proprieties;

    private final LogManager logManager;

    private ServerSocket serverSocket;
    private final ExecutorService serverThreads;
    public Server(){
        logManager= ServerApp.unifier.getLogManager();
        logManager.printNewLog(new Log("Loading initial config app.properties...", LogType.SYS));
        proprieties = loadProp();
        serverThreads  = Executors.newFixedThreadPool(Integer.parseInt(proprieties.getProperty("server.threads_count")));
    }

    @Override
    public void start(){
        Socket currentSocket = null;
        try {
            serverSocket = new ServerSocket(Integer.parseInt(proprieties.getProperty("server.server_port")));
            logManager.printNewLog(new Log("Start server at : "+ InetAddress.getLocalHost().getHostAddress()+":" + proprieties.getProperty("server.server_port"),LogType.SYS));

            while (!Thread.interrupted()){
                currentSocket = serverSocket.accept();
                currentSocket.setSoTimeout(Integer.parseInt(proprieties.getProperty("server.timeout")));
                serverThreads.execute(new Ops(currentSocket));
            }
        } catch (Exception ignored) {

        }

    }


    private Properties loadProp(){
        InputStream input = null;
        Properties proprieties = new Properties();
        try {
            input = new FileInputStream("server/src/main/resources/app.properties");

            // load a properties file
            proprieties.load(input);

            logManager.printNewLog(new Log("Configuration initialised",LogType.SYS));
            // get the property value and print it out


            logManager.printNewLog(new Log("Server port : "+proprieties.getProperty("server.server_port"),LogType.SYS));

            logManager.printNewLog(new Log("Thread count  : "+proprieties.getProperty("server.threads_count"),LogType.SYS));
            logManager.printNewLog(new Log("Server Timeout : "+proprieties.getProperty("server.timeout"),LogType.SYS));


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

}
