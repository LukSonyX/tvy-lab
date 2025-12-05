package com.example.tvylab;

import atlantafx.base.theme.PrimerLight;
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
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle("tvylab");
        stage.setScene(scene);
        stage.show();
    }

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource(fxml)));
        primaryStage.getScene().setRoot(pane);
    }
}
