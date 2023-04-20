package it.edu.unito.eserver.controller;

import it.edu.unito.eserver.model.Log.Log;
import it.edu.unito.eserver.model.Log.Loger;
import it.edu.unito.eserver.model.Log.LogType;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;


public class Controller {

    @FXML
    private ListView<Log> logList ;

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    public void initialize() {
        Loger lm = Loger.getInstance();
         logList.itemsProperty().bind(lm.logProperty());

         logList.setCellFactory(cell ->new ListCell<>(){

             @Override
             protected void updateItem(Log logItem, boolean empty){
                 super.updateItem(logItem, empty);

                 if (empty) {
                     setText(null);
                     getStyleClass().add("log");
                 }else {
                     setText((!empty && logItem != null) ? logItem.getContent() : null);


                     if (logItem!=null){
                         if (logItem.getType()==LogType.INFO){
                             getStyleClass().add("info");
                         }
                         else if (logItem.getType()==LogType.WARNING)
                             getStyleClass().add("warning");
                         else if (logItem.getType()==LogType.SYS) {
                             getStyleClass().add("sys");
                         }
                         else getStyleClass().add("warning");

                     }
                     getStyleClass().add("log");
                     logList.scrollTo(lm.logProperty().size());
                 }

             }

         });
    }
}
