package it.edu.unito.client.Controllers;

import it.edu.unito.client.ClientApp;
import it.edu.unito.client.model.Client;
import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainController {

    @FXML
    public TextArea emailContentTxt;
    @FXML
    public Label senderLabel;
    @FXML
    public Label receiversLabel;
    @FXML
    public Label subjectLabel;
    @FXML
    public Label dateLabel;





    @FXML
    public  TextFlow dangerAlert;

    public TextFlow getNewMailAlert() {
        return newMailAlert;
    }

    @FXML
    public  TextFlow newMailAlert;


    @FXML
    public ListView<Mail> mailListView;

    public static Mail selectedMail;
    Mail emptyMail;
    private  ExecutorService appFX;
    public static Scene scene;

    @FXML
    public void initialize(){
        appFX= Executors.newFixedThreadPool(Client.getInstance().getThreadNumber());

        emptyMail = Mail.generateEmptyEmail();
        selectedMail = emptyMail;
        ObservableList<Mail> inboxContent = FXCollections.observableList(Collections.
                synchronizedList(new ArrayList<>()));
        ListProperty<Mail> inboxProp = new SimpleListProperty<>();
        inboxProp.set(inboxContent);
        //binding tra lstEmails e inboxProperty
        mailListView.itemsProperty().bind(inboxProp);
        setListViewCellsListeners(mailListView);


    }

    public ListView<Mail> getMailListView() {
        return mailListView;
    }


    @FXML
    private void handleClickListView(MouseEvent event) {
        if (event.getClickCount() == 1) { // Double-click detection
            Mail selectedItem = mailListView.getSelectionModel().getSelectedItem();

            updateSelectedMailView(selectedItem);
            // Perform your desired action with the selected item
        }
    }



    private void setListViewCellsListeners(ListView<Mail> mailList){
        mailList.setCellFactory(cell -> new ListCell<>(){
            @Override
            protected void updateItem(Mail mail,boolean empty){
                super.updateItem(mail,empty);
                if (empty) {
                    setText(null);}
                else{
                    if (mail!=null){


                        if (!mail.isRead()){
                            setText("NEW!!!  "+ mail);
                            setStyle("-fx-font-weight: bold;");
                        }else {
                            setText(mail.toString());
                            setStyle("-fx-font-weight: normal");
                        }


                        setOnMouseClicked(mouseEvent -> {
                            setStyle(null);
                            setText(mail.toString());
                            selectedMail = mailList.getSelectionModel().getSelectedItem();
                            updateSelectedMailView(selectedMail);


                        });
                    }
                }


            }
        }

    );
    }


    private void updateSelectedMailView( Mail mail){
        if (!(mail==null)){
            dateLabel.setText(mail.getDate().format(Util.formatter));
            receiversLabel.setText(String.join(", ", mail.getReceivers()));
            subjectLabel.setText(mail.getSubject());
            senderLabel.setText(mail.getSender());
            emailContentTxt.setText(mail.getText());
            emailContentTxt.setWrapText(true);
            if (!(mail.isRead())){
                Client.getInstance().read(mail);

            }


        }

    }









    @FXML
    public void onComposeButtonClick()  {
        appFX.execute(() -> Platform.runLater(() -> {
            Parent root;
            try {
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("compose.fxml")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = new Stage();
            stage.setTitle("Write a new mail");
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }));

    }




    @FXML
    public void ondeleteButtonClick() {
        if (!selectedMail.equals(emptyMail)) {Client.getInstance().delete(selectedMail);
            selectedMail = emptyMail;
            updateSelectedMailView(selectedMail);
            ClientApp.extract();
        }

    }


    public void onReplyButtonClick() {
        if (!selectedMail.equals(emptyMail)) {
            appFX.execute(() -> Platform.runLater(() -> {
                Parent root ;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("reply.fxml"));
                    root = loader.load();
                    ReplyController reply = loader.getController();
                    Platform.runLater(() -> reply.setSelectedMail(selectedMail));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                Stage stage = new Stage();
                stage.setTitle("Reply to selected mail");
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }));
        }


    }



    public void onForwardButtonClick() {
        if (!selectedMail.equals(emptyMail)) {
            appFX.execute(() -> Platform.runLater(() -> {
                Parent root ;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("forward.fxml"));
                    root = loader.load();
                    ForwardController reply = loader.getController();
                    Platform.runLater(() -> reply.setSelectedMail(selectedMail));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                Stage stage = new Stage();
                stage.setTitle("Forward selected mail");
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }));
        }

    }

    public void onReplyAllButtonClick() {
        if (!selectedMail.equals(emptyMail)) {
            appFX.execute(() -> Platform.runLater(() -> {
                Parent root ;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("replyAll.fxml"));
                    root = loader.load();
                    ReplyAllController reply = loader.getController();
                    Platform.runLater(() -> reply.setSelectedMail(selectedMail));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                Stage stage = new Stage();
                stage.setTitle("Reply all to selected mail");
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }));
        }

    }
}
