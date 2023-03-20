package it.edu.unito.eclientlib;

import java.util.ArrayList;

public class User {
    private String email;

    ArrayList<Mail> messaggi;

    public String getEmail() {
        return email;
    }

    public ArrayList<Mail> getMessaggi() {
        return messaggi;
    }

    public User(String email) {
        this.email = email;
        this.messaggi=new ArrayList<>();
    }


}
