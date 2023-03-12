package it.edu.unito.eserver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test extends Application {

    private static ExecutorService appFX;
        public static Unifier u;
        public static Server server;

    public static void main(String[] args)  {

        try {
            u = new Unifier();
            server = new Server();

            appFX = Executors.newSingleThreadExecutor();
            appFX.execute(Application::launch);
            ((Thread) server).start();
            System.out.println("here");
        } catch (Exception e ){
            e.printStackTrace();
        }


    }


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Test.class.getResource("server_scene.fxml"));
        Scene scene = new Scene(loader.load(), 1080, 720);
        stage.setTitle("EServer");
        stage.setScene(scene);
        stage.show();
    }

}
