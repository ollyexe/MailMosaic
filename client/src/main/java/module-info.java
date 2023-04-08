module it.edu.unito.client {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
            requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
            requires com.almasb.fxgl.all;

    requires it.edu.unito.eclientlib;

    opens it.edu.unito.client to javafx.fxml;
    exports it.edu.unito.client;
    exports it.edu.unito.client.Controllers;
    opens it.edu.unito.client.Controllers to javafx.fxml;
    opens it.edu.unito.client.alerts to javafx.fxml;
    exports it.edu.unito.client.alerts;
}