package it.edu.unito.eserver.Log;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.LinkedList;


//LogManager con pattern singleton

public class LogManager {
    private final ListProperty<Log> log;
    private final ObservableList<Log> logContent;

    private static LogManager instance = new LogManager();
    private LogManager (){
        this.logContent = FXCollections.observableList(Collections.
                synchronizedList(new LinkedList<>()));
        this.log = new SimpleListProperty<>();
        this.log.set(logContent);

    }

    public ListProperty<Log> logProperty() {
        return log;
    }

    public static LogManager getInstance(){
        if (instance == null) {
            instance = new LogManager() ;
            System.out.println("new instance");
        }
        System.out.println("already existing instance");
        return instance;
    }

    public synchronized void printNewLog(Log l){
        logContent.add(l);
    }


}
