package it.edu.unito.client.model;

import it.edu.unito.client.Controllers.MainController;
import it.edu.unito.client.Controllers.ReplyController;
import it.edu.unito.eclientlib.AlertText;
import it.edu.unito.eclientlib.Mail;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static it.edu.unito.client.Controllers.MainController.selectedMail;

public class Model {
    private final ObservableList<Mail> inbox;

    private static Model instance;
    private Model() {
        inbox = FXCollections.observableList(Collections.synchronizedList(new ArrayList<>()));

    }

    public static Model getInstance(){
        if (instance==null){
            instance= new Model();
        }
        return instance;
    }

    public ObservableList<Mail> getInboxContent() {
        return inbox;
    }



}
