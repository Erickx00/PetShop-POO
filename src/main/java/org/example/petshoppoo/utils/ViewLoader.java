package org.example.petshoppoo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ViewLoader {

    public static void loadView(Stage stage, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);

        stage.sizeToScene();
        stage.centerOnScreen();
        stage.show();
    }

    public static void changeScene(Node node, String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        loadView(stage, fxmlPath, title);
    }

}