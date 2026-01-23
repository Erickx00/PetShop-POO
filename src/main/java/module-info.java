module org.example.petshoppoo {
    // Módulos do JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Módulos do Jackson para JSON
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.core;

    // Outros
    requires java.desktop;


    // Permite que o JavaFX acesse as classes principais (como MainApp)
    opens org.example.petshoppoo to javafx.fxml;

    //  JavaFX injetar componentes nos  Controllers
    opens org.example.petshoppoo.controllers to javafx.fxml;

    //  Jackson conseguir manipular  modelos (Reflexão)
    opens org.example.petshoppoo.model.Login to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo.model.Pet to com.fasterxml.jackson.databind;
    opens org.example.petshoppoo.model.Servico to com.fasterxml.jackson.databind;

    // Exportação dos pacotes para que outras partes do Java/JavaFX os vejam
    exports org.example.petshoppoo;
    exports org.example.petshoppoo.controllers;
    exports org.example.petshoppoo.model.Login;
    exports org.example.petshoppoo.model.Pet;
    exports org.example.petshoppoo.model.Servico;
    exports org.example.petshoppoo.services;
    exports org.example.petshoppoo.services.interfaces;
    exports org.example.petshoppoo.repository.interfaces;
    exports org.example.petshoppoo.repository.implementations;
    exports org.example.petshoppoo.exceptions;
    exports org.example.petshoppoo.utils;
}