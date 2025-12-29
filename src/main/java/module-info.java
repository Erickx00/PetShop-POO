module org.example.petshoppoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.graphics;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens org.example.petshoppoo to javafx.fxml;
    opens org.example.petshoppoo.controllers to javafx.fxml;
    opens org.example.petshoppoo.model.Login to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo.model.Dono to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo.model.Pet to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo.model.Usuario to com.fasterxml.jackson.databind;




    exports org.example.petshoppoo;
}