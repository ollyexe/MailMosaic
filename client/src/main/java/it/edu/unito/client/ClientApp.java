package it.edu.unito.client;

import it.edu.unito.client.Controllers.MainController;
import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static it.edu.unito.client.Controllers.MainController.selectedMail;

public class ClientApp extends Application {
    public static Model model = new Model();
    public static Client client;
    public static MainController mainController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("client.fxml"));

        Scene main = new Scene(fxmlLoader.load());
        stage.setTitle("Hello "+Client.prop.getProperty("client.usr"));
        stage.setResizable(false);
        mainController = fxmlLoader.getController();

        //fetch every 5 seconds from server
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    // Generate some random mail objects for testing
                    List<Mail> newMails =Client.getInstance().fetch().stream().filter(mail -> mail.getReceivers().contains(Client.getInstance().usr)).toList().stream().sorted(
                            (o1, o2) -> {
                                if (o2.getDate().isAfter(o1.getDate())){
                                    return 1;
                                }
                                return -1;
                            }).toList();

                    // Update the ListView items with the new mail objects
                    ListView<Mail> mailList = mainController.getMailListView();

                    Platform.runLater(()-> {
                        mailList.getItems().clear();
                        mailList.getItems().addAll(newMails);

                        //fa in modo che mantenga la selezione
                        mainController.getMailListView().getSelectionModel().select(selectedMail);
                    });


                });
            }
        }, 0, 5000); // Update the ListView every 5 seconds



        System.out.println("Client started at : "+ LocalDateTime.now().format(Util.formatter));


        stage.setScene(main);
        stage.show();
    }
    @Override
    public void stop() throws Exception {
        System.out.println("Client stoped at : "+ LocalDateTime.now().format(Util.formatter));
        System.exit(0);
    }

    public static void main(String[] args) {
         model = new Model();
         client = Client.getInstance();

        launch();

    }
}