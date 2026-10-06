package org.example.petshoppoo.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class ViewLoader {
    private ViewLoader() {
        /* This utility class should not be instantiated */
    }


    public static void loadView(Stage stage, String fxmlPath, String title) throws IOException {
        boolean wasFullScreen = stage.isFullScreen();
        boolean wasMaximized = stage.isMaximized();
        boolean wasShowing = stage.isShowing();
        double previousCenterX = stage.getX() + stage.getWidth() / 2;
        double previousCenterY = stage.getY() + stage.getHeight() / 2;

        FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle(title);

        if (wasFullScreen) {
            stage.setFullScreen(true);
        } else if (wasMaximized) {
            stage.setMaximized(true);
        } else {
            stage.sizeToScene();
            if (wasShowing) {
                stage.setX(previousCenterX - stage.getWidth() / 2);
                stage.setY(previousCenterY - stage.getHeight() / 2);
            } else {
                stage.centerOnScreen();
            }
        }

        if (!wasShowing) {
            stage.show();
        }
    }

    public static void changeScene(Node node, String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) node.getScene().getWindow();
        loadView(stage, fxmlPath, title);
    }

}
