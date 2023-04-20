package it.edu.unito.client;

import it.edu.unito.client.Controllers.MainController;
import it.edu.unito.client.model.AlertManager;
import it.edu.unito.client.model.Client;
import it.edu.unito.eclientlib.AlertText;
import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
    public static Client client=Client.getInstance();
    public static MainController mainController;
    public static LocalDateTime lastFetch=LocalDateTime.now();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApp.class.getResource("client.fxml"));
        Scene main = new Scene(fxmlLoader.load());
        stage.setTitle("Hello "+Client.getInstance().getUsr());
        stage.setResizable(false);
        mainController = fxmlLoader.getController();

        //fetch every 5 seconds from server
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    List<Mail> newMails =Client.getInstance().fetch().stream().filter(mail -> mail.getReceivers().contains(Client.getInstance().getUsr())).toList().stream().sorted(
                            (o1, o2) -> {
                                if (o2.getDate().isAfter(o1.getDate())){
                                    return 1;
                                }else if (o2.getDate().isBefore(o1.getDate())){
                                    return -1;
                                }
                                else
                                    return 0;

                            }).toList();


                    ListView<Mail> mailList = mainController.getMailListView();

                    int count = (int) newMails.stream().filter(mail -> mail.getDate().isAfter(lastFetch)).count();
                    if (count>0){
                        AlertManager.showTemporizedAlert(mainController.getNewMailAlert(),AlertText.NEW_EMAILS, 2);
                    }
                    lastFetch=LocalDateTime.now();
                    Platform.runLater(()-> {
                        mailList.getItems().clear();
                        mailList.getItems().addAll(newMails);

                        //fa in modo che mantenga la selezione
                        mainController.getMailListView().getSelectionModel().select(selectedMail);
                    });


                });
            }
        }, 0, Long.parseLong(Client.prop.getProperty("client.fetch_interval"))); // Update the ListView every 5 seconds



        System.out.println("Client started at : "+ LocalDateTime.now().format(Util.formatter));


        stage.setScene(main);
        stage.show();
    }
    @Override
    public void stop()  {
        System.out.println("Client stoped at : "+ LocalDateTime.now().format(Util.formatter));
        System.exit(0);
    }

    public static void main(String[] args) {
         client = Client.getInstance();

         launch();

    }

    public static void extract() {

        Platform.runLater(() -> {
            List<Mail> newMails =Client.getInstance().fetch().stream().filter(mail -> mail.getReceivers().contains(Client.getInstance().getUsr())).toList().stream().sorted(
                    (o1, o2) -> {
                        if (o2.getDate().isAfter(o1.getDate())){
                            return 1;
                        }else if (o2.getDate().isBefore(o1.getDate())){
                            return -1;
                        }
                        else
                            return 0;

                    }).toList();

            Parent root ;
            MainController mainController;
            try {
                FXMLLoader loader = new FXMLLoader(ClientApp.class.getResource("client.fxml"));
                root = loader.load();
                mainController = loader.getController();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            ListView<Mail> mailList = mainController.getMailListView();

            int count = (int) newMails.stream().filter(mail -> mail.getDate().isAfter(lastFetch)).count();
            if (count>0){
                AlertManager.showTemporizedAlert(mainController.getNewMailAlert(), AlertText.NEW_EMAILS, 2);
            }
            lastFetch= LocalDateTime.now();

            Platform.runLater(()-> {
                mailList.getItems().clear();
                mailList.getItems().addAll(newMails);

                //fa in modo che mantenga la selezione
                mainController.getMailListView().getSelectionModel().select(selectedMail);
            });


        });

    }
}