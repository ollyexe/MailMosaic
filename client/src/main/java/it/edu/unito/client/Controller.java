package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


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

    @FXML
    public void initialize(){
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

}
