module com.bognen.budget {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    //+++ fails with java: module not found: eu.hansolo.tilesfx
    // requires eu.hansolo.tilesfx;
    // SQLite Module
    requires java.sql;

    opens com.bognen.budget to javafx.fxml;
    opens com.bognen.budget.model to javafx.base;
    exports com.bognen.budget;
    exports com.bognen.budget.controller;
    opens com.bognen.budget.controller to javafx.fxml;
}