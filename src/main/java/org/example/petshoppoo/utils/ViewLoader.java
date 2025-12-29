package org.example.petshoppoo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewLoader {

    public static void loadView(Stage stage, String fxmlPath, String title, int width, int height) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
    }

    public static void changeScene(javafx.scene.Node node, String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        loadView(stage, fxmlPath, title, 400, 500);
    }
}
