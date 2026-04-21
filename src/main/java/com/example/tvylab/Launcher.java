package com.example.tvylab;

import com.example.tvylab.settings.Settings;
import com.example.tvylab.settings.SettingsManager;
import javafx.application.Application;
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

        Settings settings = SettingsManager.load();

        root.setStyle("-fx-font-size: " + settings.fontSize + "px;");

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
        Settings settings = SettingsManager.load();
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource(fxml)));
        pane.setStyle("-fx-font-size: " + settings.fontSize + "px;");
        primaryStage.getScene().setRoot(pane);
    }
}
