package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.OperationName;
import it.edu.unito.eclientlib.Request;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

import java.util.Objects;

import static it.edu.unito.client.Front.model;

public class Controller {
    @FXML
    private ListView<Mail> mailListView;

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

    //    private void setListViewCellsListeners(ListView<Mail> mailList){
//        mailList.setCellFactory(cell -> new ListCell<>() {
//            @Override
//            protected void updateItem(Mail mail, boolean empty) {
//                super.updateItem(mail, empty);
//                boolean check = !empty && mail!= null;
//
//                String text = !check ? null :
//                        Objects.equals(mail.getSender(), "gionni@gmail.com") ?//TODO cambiare mail dinamicamente
//                                "YOU - " + mail.getSubject() :
//                                mail.toString();
//
//                setText(text);
//                setStyle(check && !mail.isRead() ? "-fx-font-weight: bold" : null);
//
//                setOnMouseClicked((click) -> {
//                    Mail selectedEmail = mailList.getSelectionModel().getSelectedItem();
//                    if (selectedEmail != null && !selectedEmail.isRead()) {
//                        //client-side markAsRead
//                        selectedEmail.setRead(false);
//                        setStyle(null);
//
//                        Client.processRequest(new Request("gionni@gmail.com",OperationName.GET));
//                    }
//                    MainController.this.selectedEmail = selectedEmail;
//                    updateDetailView(selectedEmail);
//                });
//            }
//        });
//    }

}
