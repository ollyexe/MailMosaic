package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Compose extends Controller{

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


    public TextFlow getSuccessAlert() { return successAlert; }

    public TextFlow getDangerAlert() { return dangerAlert; }


    @FXML
    public void initialize(){
        senderTextField.setEditable(false);
        senderTextField.setText(Client.prop.getProperty("client.usr"));
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

       Stage stage=(Stage) Controller.scene.getWindow();
       stage.close();
    }


    @FXML
    private void onSendButtonClick() {
        String[] recipientsArray = recipientsTextField.getText().split("\\s*,\\s*");
//        if (Arrays.stream(recipientsArray).allMatch(CommonUtil::validateEmail)){
//            Mail email = new Mail(senderTextField.getText(),
//                    new ArrayList<>(List.of(recipientsArray)),
//                    objectTextField.getText(), messageEditor.getText());
//
//            model.getClient().sendCmd(CommandName.SEND_EMAIL, email,
//                    ClientApp.sceneController.getController(SceneName.COMPOSE),
//                    (obj) -> Platform.runLater( () -> send(obj)), email);
//        } else {
//            AlertManager.showTemporizedAlert(dangerAlert, AlertText.INVALID_RECIPIENTS, 2);
//        }
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
