module org.example.petshoppoo {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.core;
    opens org.example.petshoppoo to javafx.fxml;
    opens org.example.petshoppoo.controllers to javafx.fxml;
    opens org.example.petshoppoo.model.Login to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo.model.Pet to com.fasterxml.jackson.databind;
    exports org.example.petshoppoo;
    exports org.example.petshoppoo.controllers;
    exports org.example.petshoppoo.services;
    exports org.example.petshoppoo.exceptions;
}