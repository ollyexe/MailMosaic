package it.edu.unito.eclientlib;

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
        String op= null;
        switch (opName){
            case PUT -> {
                op="Read";
                return  op +"||"+
                        " from='" + sender + '\'' +
                        ", to=" + content.getReceivers()+"|| initiator "+sender ;
            }
            case POST -> {
                op="Send";
                return  op +"||"+
                        " from='" + sender + '\'' +
                        ", to=" + content.getReceivers()+"|| initiator "+sender ;
            }
            case GET -> {
                op="Fetch";
                return op+" "+"|| initiator "+sender ;
            }
            case DELETE -> {
                op="Delete";
                return  op +"||"+
                        " from='" + content.getSender()  +
                        ", to=" + content.getReceivers()+"|| initiator "+sender ;
            }
        }
        return null;
    }


}
