module org.example.petshoppoo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.petshoppoo to javafx.fxml;
    exports org.example.petshoppoo;
}