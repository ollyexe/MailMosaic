package it.edu.unito.client.Controllers;

import it.edu.unito.client.Client;
import it.edu.unito.client.alerts.AlertManager;
import it.edu.unito.client.alerts.AlertText;
import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.LineTo;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ForwardController {

    @FXML
    public Button cancelBtn;
    @FXML
    public Button sendBtn;
    @FXML
    private TextFlow dangerAlert;
    @FXML
    private TextFlow successAlert;
    @FXML
    private TextField senderTextField;
    @FXML
    private TextField recipientsTextField;
    @FXML
    private TextField objectTextField;
    @FXML
    private TextArea messageEditor;



    public void setSelectedMail(Mail selectedMail) {
        this.selectedMail = selectedMail;
        senderTextField.setText(Client.prop.getProperty("client.usr"));
        senderTextField.setEditable(false);
        objectTextField.setText("Forward : "+selectedMail.getSubject());
        objectTextField.setEditable(false);
        messageEditor.setText("Forward :" + selectedMail.toStringForForward());
        messageEditor.setEditable(false);
        messageEditor.setWrapText(true);

    }



    Mail selectedMail ;

    public TextFlow getSuccessAlert() { return successAlert; }

    public TextFlow getDangerAlert() { return dangerAlert; }


    public ForwardController() {
    }

    @FXML
    public void initialize(){


    }


    public TextField getSenderTextField() {
        return senderTextField;
    }

    public TextField getRecipientsTextField() {
        return recipientsTextField;
    }

    public TextField getObjectTextField() {
        return objectTextField;
    }

    public TextArea getMessageEditor() {
        return messageEditor;
    }


    @FXML
    private void onCancelButtonClick() {
        //clearing all fields
        recipientsTextField.clear();
        objectTextField.clear();
        messageEditor.setText("");

        Stage stage=(Stage) MainController.scene.getWindow();
        stage.close();
    }


    @FXML
    private void onSendButtonClick() throws InterruptedException {
        String[] recipientsArray = recipientsTextField.getText().split("\\s*,\\s*");
        if (Arrays.stream(recipientsArray).allMatch(Util::validateEmail)){
            Mail email = new Mail(senderTextField.getText(),
                    new ArrayList<>(List.of(recipientsArray)),
                    objectTextField.getText(), messageEditor.getText());


            if (Client.getInstance().send(email)){
                //clearing all fields
                recipientsTextField.clear();
                objectTextField.clear();
                messageEditor.setText("");
                AlertManager.showTemporizedAlert(successAlert, AlertText.MESSAGE_SENT, 2);
                Stage stage=(Stage) MainController.scene.getWindow();
                stage.close();

            }else {
                AlertManager.showTemporizedAlert(dangerAlert, AlertText.OP_ERROR, 2);

            }


        } else {
            AlertManager.showTemporizedAlert(dangerAlert, AlertText.INVALID_RECIPIENTS, 2);
        }

    }


    private void send(Object email){
//        model.addEmails(Collections.singletonList((Email) email));
//        //clearing all fields
//        recipientsTextField.clear();
//        objectTextField.clear();
//        messageEditor.setText("");
//        AlertManager.showSuccessSendMessage(AlertText.MESSAGE_SENT, 2);
    }
}
