package it.edu.unito.eserver;

import it.edu.unito.eclientlib.Util;
import it.edu.unito.eserver.model.Server.Server;
import it.edu.unito.eserver.model.Server.Unifier;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerApp extends Application {

    private static ExecutorService appFX;
        public static Unifier unifier;
        public static Server server;

    public static void main(String[] args)  {

        try {
            unifier = new Unifier();
            server = new Server();

            appFX = Executors.newFixedThreadPool(Integer.parseInt(server.proprieties.getProperty("server.threads_count")));
            appFX.execute(Application::launch);
            ((Thread) server).start();

        } catch (Exception e ){
            e.printStackTrace();
        }


    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(ServerApp.class.getResource("server_scene.fxml"));
        Scene scene = new Scene(loader.load(), 1080, 720);
        stage.setTitle("EServer");
        stage.setScene(scene);
        stage.show();
        System.out.println("Server started at : "+ LocalDateTime.now().format(Util.formatter));
    }


    @Override
    public void stop() throws Exception {
        System.out.println("Server stoped at : "+LocalDateTime.now().format(Util.formatter));
       System.exit(0);
    }

}
