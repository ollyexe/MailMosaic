package it.edu.unito.client;

import it.edu.unito.eclientlib.Mail;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Model {
    private final ObservableList<Mail> inbox;
    private final ListProperty<Mail> inboxProp;

    private final StringProperty emailAddress;

    public Model() {
        inbox = FXCollections.observableList(Collections.synchronizedList(new ArrayList<>()));
        inboxProp = new SimpleListProperty<>();
        inboxProp.set(inbox);

        this.emailAddress = new SimpleStringProperty(Client.getInstance().usr);

    }

    public ObservableList<Mail> getInboxContent() {
        return inbox;
    }

    public ObservableList<Mail> getInbox() {
        return inbox;
    }

    public ObservableList<Mail> getInboxProp() {
        return inboxProp.get();
    }

    public ListProperty<Mail> inboxPropProperty() {
        return inboxProp;
    }

    public String getEmailAddress() {
        return emailAddress.get();
    }

    public StringProperty emailAddressProperty() {
        return emailAddress;
    }

    public void addEmails(List<Mail> emails){
        inbox.addAll(emails);
        inbox.sort(Mail::compareTo);
    }
}
