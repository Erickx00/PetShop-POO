package org.example.petshoppoo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.petshoppoo.controllers.CadastroController;

import java.io.IOException;

public class MainApp extends Application {

    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/org/example/petshoppoo/resources/views/cadastro.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 450);
        stage.setTitle("Hello!");
        stage.setScene(scene);

        CadastroController controller = fxmlLoader.getController();
        stage.show();
    }

    public static void main(String[] args) {
        //System.out.println(MainApp.class.getResource("/org/example/petshoppoo/resources/views/cadastro.fxml"));

        launch();
    }
}
