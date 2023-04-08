package it.edu.unito.eserver.model.Log;

import it.edu.unito.eserver.ServerApp;
import it.edu.unito.eclientlib.*;
import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;
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

        }

        return instance;
    }

    public synchronized void printNewLog(Log l){
        logContent.add(l);
    }


    public static void logResponse(ResponseName name, Request req) {
        if (name.equals(ResponseName.SUCCESS)){
            Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[INFO] :")
                            .append(req.toString())).toString(), LogType.INFO) ));

        } else if (name.equals(ResponseName.ILLEGAL_PARAMS)) {
            Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append(ResponseName.ILLEGAL_PARAMS).append("||").append(req.toString()).append("||").append(name)).toString(), LogType.WARNING) ));
        } else {
            Platform.runLater(()-> ServerApp.unifier.getLogManager().printNewLog(new Log(
                    (new StringBuilder().append(LocalDateTime.now().format(Util.formatter))
                            .append("[Warning] :")
                            .append("||").append(req.toString()).append("||").append(name)).toString(), LogType.WARNING) ));
        }


    }
}
