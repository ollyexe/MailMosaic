module com.example.eserver {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    exports it.edu.unito.eserver;
    opens it.edu.unito.eserver to javafx.fxml;
    exports it.edu.unito.eserver.controller;
    opens it.edu.unito.eserver.controller to javafx.fxml;
    exports it.edu.unito.eserver.model.Log;
    opens it.edu.unito.eserver.model.Log to javafx.fxml;
    exports it.edu.unito.eserver.model.DAO;
    opens it.edu.unito.eserver.model.DAO to javafx.fxml;

}