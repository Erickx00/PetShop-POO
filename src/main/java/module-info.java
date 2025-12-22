module org.example.petshoppoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.jdi;
    requires com.fasterxml.jackson.databind;


    opens org.example.petshoppoo to javafx.fxml;
    exports org.example.petshoppoo;
    exports org.example.petshoppoo.controllers;
    opens org.example.petshoppoo.controllers to javafx.fxml;
}