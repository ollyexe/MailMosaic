package it.edu.unito.client.model;

import it.edu.unito.eclientlib.Mail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collections;

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
