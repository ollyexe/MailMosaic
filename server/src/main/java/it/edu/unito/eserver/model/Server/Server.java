package it.edu.unito.eserver.model.Server;

import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.Loger;
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

    private static Server instance=null;
    public final Properties prop;

    private final Loger loger;

    private final ExecutorService pool;
    public int threadNumber;
    public int timeout;

    public int getThreadNumber() {
        return threadNumber;
    }

    public int getFetchIntervall() {
        return timeout;
    }

    public int getPort() {
        return port;
    }

    public int port;
    private Server(){
        loger = Loger.getInstance();
        loger.printNewLog(new Log("Loading initial config app.properties...", LogType.SYS));
        prop = loadProp();
        port = Integer.parseInt(prop.getProperty("server.server_port"));
        timeout = Integer.parseInt(prop.getProperty("server.timeout"));
        threadNumber = Integer.parseInt(prop.getProperty("server.threads_count"));
        pool  = Executors.newFixedThreadPool(threadNumber);
    }
    public static Server getIntance(){
        if (instance==null){
            return new Server();
        }
        return instance;
    }

    @Override
    public void start(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            loger.printNewLog(new Log("Start server at : "+ InetAddress.getLocalHost().getHostAddress()+":" + prop.getProperty("server.server_port"),LogType.SYS));

            while (!Thread.interrupted()){
                //apro la connseeione
                Socket currentSocket = serverSocket.accept();
                currentSocket.setSoTimeout(timeout);
                //currentSocket passato per valore
                pool.execute(new Ops(currentSocket));
            }
            //chiude tutte le connessioni
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private Properties loadProp(){
        InputStream input = null;
        Properties proprieties = new Properties();
        try {
            input = new FileInputStream("server/src/main/resources/app.properties");

            // load a properties file
            proprieties.load(input);

            loger.printNewLog(new Log("Configuration initialised",LogType.SYS));
            // get the property value and print it out


            loger.printNewLog(new Log("Server port : "+proprieties.getProperty("server.server_port"),LogType.SYS));

            loger.printNewLog(new Log("Thread count  : "+proprieties.getProperty("server.threads_count"),LogType.SYS));
            loger.printNewLog(new Log("Server Timeout : "+proprieties.getProperty("server.timeout"),LogType.SYS));


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
