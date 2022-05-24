module com.example.laboras {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires mysql.connector.java;

    requires spring.context;
    requires spring.web;
    //requires gson;

    requires spring.core;
    requires com.google.gson;

    opens com.example.laboras to javafx.fxml;
    exports com.example.laboras;
}