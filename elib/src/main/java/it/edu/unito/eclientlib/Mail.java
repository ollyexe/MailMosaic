package it.edu.unito.eclientlib;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

public class Mail implements Serializable,Comparable<Mail>{
    private final int id;
    private final String sender;
    private final List<String> receivers;
    private final String subject;
    private final String text;
    private LocalDateTime date;
    private boolean read;


    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Mail(String sender, List<String> receivers, String subject, String text) {
        this.id=this.idGen(sender, receivers, subject, text, LocalDateTime.now());
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.text = text;
        this.date=LocalDateTime.now();
        this.read=false;
    }
    public Mail(String sender, List<String> receivers, String subject, String text,LocalDateTime date) {
        this.id=this.idGen(sender, receivers, subject, text, date);
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.text = text;
        this.date=date;
        this.read=false;
    }

    public static Mail generateEmptyEmail() {
        Mail m = new Mail("", List.of(""), "",
                "");
        m.date=null;
        return m;
    }


    public int getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public int compareTo(Mail email) {//per fare il display in ordine crescente
        return email.getDate().compareTo(this.date);
    }


    public int idGen(String sender,List<String> receivers,String subject,String text,LocalDateTime date) {
        return Objects.hash(sender, receivers, subject, text, date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mail email = (Mail) o;
        return getId() == email.getId();
    }

    @Override
    public String toString() {
        return getSubject()+" -> "+getSender();
    }





}
