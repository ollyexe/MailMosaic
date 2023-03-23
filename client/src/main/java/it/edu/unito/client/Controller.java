package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.OperationName;
import it.edu.unito.eclientlib.Request;
import it.edu.unito.eclientlib.Util;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

import java.util.Objects;

import static it.edu.unito.client.Front.model;


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
    @FXML
    private ListView<Mail> mailListView;

    Mail selectedMail;

    private ObservableList<Mail> inbox;

    @FXML
    public void initialize(){
        inbox= FXCollections.observableArrayList(
                Mail.generateEmptyEmail()
        );
        mailListView.setItems(inbox);
    }

    public ListView<Mail> getMailListView() {
        return mailListView;
    }

    public ObservableList<Mail> getInbox() {
        return inbox;
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


                String tumadre="ggg";
                setOnMouseClicked(mouseEvent -> {
                    Mail m = mailList.getSelectionModel().getSelectedItem();

                    Controller.this.selectedMail=m;
                    updateSelectedMailView(selectedMail);

                });
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

}
