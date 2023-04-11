package it.edu.unito.client.Controllers;

import it.edu.unito.client.Client;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class MainController {

    @FXML
    private SplitPane splitPane;
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


    public void setMailListView(ListView<Mail> mailListView) {
        this.mailListView = mailListView;
    }

    @FXML
    public ListView<Mail> mailListView;

    private ObservableList<Mail> inboxContent;

    private ListProperty<Mail> inboxProp;
    public static Mail selectedMail;
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
                if (empty) {
                    setText(null);}
                else{
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
    public void onComposeButtonClick(MouseEvent mouseEvent) throws IOException {
        appFX.execute(() -> {
            Platform.runLater(() -> {
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
            });
        });

    }




    @FXML
    public void ondeleteButtonClick(MouseEvent mouseEvent) {
        Client.getInstance().delete(selectedMail);
        selectedMail = emptyMail;
        updateSelectedMailView(selectedMail);
    }


    public void onReplyButtonClick(MouseEvent mouseEvent) {
        appFX.execute(() -> {
            Platform.runLater(() -> {
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
            });
        });

    }



    public void onForwardButtonClick(MouseEvent mouseEvent) {
        appFX.execute(() -> {
            Platform.runLater(() -> {
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
            });
        });
    }
}
