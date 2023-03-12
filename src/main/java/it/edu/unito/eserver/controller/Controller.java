package it.edu.unito.eserver.controller;

import it.edu.unito.eserver.Log.Log;
import it.edu.unito.eserver.Log.LogManager;
import it.edu.unito.eserver.Log.LogType;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


public class Controller {

    @FXML
    private ListView<Log> logList ;


    public void initialize() {
        LogManager lm = LogManager.getInstance();
         logList.itemsProperty().bind(lm.logProperty());

         logList.setCellFactory(cell ->new ListCell<>(){

             @Override
             protected void updateItem(Log logItem, boolean empty){
                 super.updateItem(logItem, empty);
                 setText((!empty && logItem != null) ? logItem.getContent() : null);


                 if (logItem!=null){
                     if (logItem.getType()==LogType.INFO&&!empty){
                         getStyleClass().add("info");
                     }
                     else if (logItem.getType()==LogType.WARNING&&!empty)
                         getStyleClass().add("warning");
                     else if (logItem.getType()==LogType.SYS&&!empty) {
                         getStyleClass().add("sys");
                     }
                     else getStyleClass().add("warning");

                 }
                 getStyleClass().add("log");
                 logList.scrollTo(lm.logProperty().size());
             }

         });
    }
}
