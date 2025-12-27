module org.example.petshoppoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.graphics;

    opens org.example.petshoppoo.controllers to javafx.fxml;
    opens org.example.petshoppoo.model.Login to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo to javafx.fxml;

    exports org.example.petshoppoo;
}