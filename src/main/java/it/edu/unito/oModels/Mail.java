package it.edu.unito.oModels;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Mail implements Serializable,Comparable<Mail>{
    private int id;
    private final String sender;
    private final List<String> receivers;
    private final String subject;
    private final String text;
    private LocalDateTime date;
    private boolean read;


    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Mail(String sender, List<String> receivers, String subject, String text) {
        this.id=this.hashCode();
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.text = text;
        this.date=LocalDateTime.now();
        this.read=false;
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


    @Override
    public String toString() {
        return "Mail{" +
                "id=" + id +
                ", sender='" + sender + '\'' +
                ", receivers=" + receivers +
                ", subject='" + subject + '\'' +
                ", text='" + text + '\'' +
                ", date=" + date.format(formatter) +
                ", read=" + read +
                '}';
    }


}
