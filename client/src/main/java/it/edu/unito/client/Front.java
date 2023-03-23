package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.OperationName;
import it.edu.unito.eclientlib.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Front extends Application {
    public static Model model = new Model();
    public static Client client;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Front.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        Controller c = fxmlLoader.getController();


        //fetch every 5 seconds from server
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Generate some random mail objects for testing
                    List<Mail> newMails =Client.getInstance().fetch().stream().filter(mail -> mail.getReceivers().contains("gionni@gmail.com")).toList();

                    // Update the ListView items with the new mail objects
                    ObservableList <Mail> items = c.getMailListView().getItems();
                    items.clear();
                    items.addAll(newMails);
                });
            }
        }, 0, 5000); // Update the ListView every 5 seconds



        System.out.println("Client started at : "+ LocalDateTime.now().format(Util.formatter));


        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() throws Exception {
        System.out.println("Client stoped at : "+ LocalDateTime.now().format(Util.formatter));
        System.exit(0);
    }

    public static void main(String[] args) {
        ScheduledExecutorService fetcher = Executors.newSingleThreadScheduledExecutor();
         model = new Model();
         client = Client.getInstance();

        launch();

    }
}