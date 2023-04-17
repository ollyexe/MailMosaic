package it.edu.unito.client.Controllers;

import it.edu.unito.client.model.Client;
import it.edu.unito.client.model.AlertManager;
import it.edu.unito.eclientlib.AlertText;
import it.edu.unito.eclientlib.Mail;
import it.edu.unito.eclientlib.Util;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

    public TextFlow getDangerAlert() { return dangerAlert; }


    public ForwardController() {
    }

    @FXML
    public void initialize(){


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
    private void onSendButtonClick()  {
        String[] recipientsArray = recipientsTextField.getText().split(",");
        if (Arrays.stream(recipientsArray).allMatch(Util::validateEmail)){
            Mail email = new Mail(senderTextField.getText(),
                    new ArrayList<>(List.of(recipientsArray)),
                    objectTextField.getText(), messageEditor.getText());


            if (Client.getInstance().send(email)){
                //clearing all fields
                recipientsTextField.clear();
                objectTextField.clear();
                messageEditor.setText("");
                Stage stage=(Stage) MainController.scene.getWindow();
                stage.close();

            }else {
                AlertManager.showTemporizedAlert(dangerAlert, AlertText.OP_ERROR, 2);

            }


        } else {
            AlertManager.showTemporizedAlert(dangerAlert, AlertText.INVALID_RECIPIENTS, 2);
        }

    }



}
