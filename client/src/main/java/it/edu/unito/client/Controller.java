package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {
    @FXML
    public WebView emailContentTxt;
    @FXML
    public Label senderLabel;
    @FXML
    public Label receiversLabel;
    @FXML
    public Label subjectLabel;
    @FXML
    public Label dateLabel;
    public Label owner;


    @FXML
    private ListView<Mail> mailListView;

    private ObservableList<Mail> inboxContent;

    private ListProperty<Mail> inboxProp;
    static Mail selectedMail;
    Mail emptyMail;
    private  ExecutorService appFX;
    public static Scene scene;

    @FXML
    public void initialize(){
        appFX= Executors.newFixedThreadPool(Integer.parseInt(Client.prop.getProperty("client.threads_count")));

        emptyMail = Mail.generateEmptyEmail();
        selectedMail = emptyMail;
        inboxContent= FXCollections.observableList(Collections.
                synchronizedList(new ArrayList<>()));
        inboxProp=new SimpleListProperty<>();
        inboxProp.set(inboxContent);
        owner.setText(Client.prop.getProperty("client.usr"));
        //binding tra lstEmails e inboxProperty
        mailListView.itemsProperty().bind(inboxProp);
        setListViewCellsListeners(mailListView);


    }

    public ListView<Mail> getMailListView() {
        return mailListView;
    }

    public ObservableList<Mail> getInbox() {
        return inboxContent;
    }



    @FXML
    private void handleClickListView(MouseEvent event) {
        if (event.getClickCount() == 1) { // Double-click detection
            Object selectedItem = mailListView.getSelectionModel().getSelectedItem();

            updateSelectedMailView((Mail) selectedItem);
            // Perform your desired action with the selected item
        }
    }



    private void setListViewCellsListeners(ListView<Mail> mailList){
        mailList.setCellFactory(cell -> new ListCell<>(){
            @Override
            protected void updateItem(Mail mail,boolean empty){
                super.updateItem(mail,empty);


                if (mail!=null){

                    setText(mail.toString());
                    if (!mail.isRead()){
                        setStyle("-fx-font-weight: bold;");
                    }


                    setOnMouseClicked(mouseEvent -> {
                        setStyle(null);
                         selectedMail = mailList.getSelectionModel().getSelectedItem();
                        updateSelectedMailView(selectedMail);


                    });
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
            emailContentTxt.getEngine().loadContent(mail.getText());
            if (!(mail.isRead())){
                Client.getInstance().read(mail);
            }


        }

    }









    @FXML
    public void onComposeButtonClick(MouseEvent mouseEvent) throws IOException {
        appFX.execute(() -> {
            Platform.runLater(() -> {
                Parent root = null;
                try {
                    root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("compose.fxml")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                stage.setTitle("Write a new e-mail");
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            });
        });

    }




    @FXML
    public void ondeleteButtonClick(MouseEvent mouseEvent) {
        Client.getInstance().delete(selectedMail);
        selectedMail = emptyMail;
        updateSelectedMailView(selectedMail);
    }
}
