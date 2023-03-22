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
}