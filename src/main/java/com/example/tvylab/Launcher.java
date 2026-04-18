package com.example.tvylab;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Launcher extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-menu.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 750, 500);

        root.setStyle("-fx-font-size: " + SettingsController.fontSize + "px;");

        String css = Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle("tvylab");
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource(fxml)));
        pane.setStyle("-fx-font-size: " + SettingsController.fontSize + "px;");
        /*Scene scene = primaryStage.getScene();

        pane.styleProperty().bind(Bindings.concat(
                "-fx-font-size: ",
                scene.widthProperty().divide(800).multiply(16).asString(), "pt;"
        ));*/
        primaryStage.getScene().setRoot(pane);
    }
}
