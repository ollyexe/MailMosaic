package it.edu.unito.oModels;

import java.io.Serializable;

public class Request implements Serializable {
    String sender;
    OperationName opName;
    Mail content;

    public Request(String sender, OperationName opName, Mail content) {
        this.sender = sender;
        this.opName = opName;
        this.content = content;
    }
    public Request(String sender, OperationName opName) {
        this.sender = sender;
        this.opName = opName;
    }

    public OperationName getOpName() {
        return opName;
    }

    public Mail getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return "SEND " +
                "from='" + sender + '\'' +
                ", to=" + content.getReceivers() ;
    }
}
