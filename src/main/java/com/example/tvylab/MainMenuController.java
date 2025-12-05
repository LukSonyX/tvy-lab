package com.example.tvylab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainMenuController {
    @FXML
    private Button learnButton;

    @FXML
    private Button sandboxButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Button exitButton;

    @FXML
    private VBox vbox;

    @FXML
    public void initialize() {
        vbox.heightProperty().addListener((obs, oldVal, newVal) -> {
            double fontSize = newVal.doubleValue() / 10;
            learnButton.setStyle("-fx-font-size: " + fontSize + "px;");
            sandboxButton.setStyle("-fx-font-size: " + fontSize + "px;");
            settingsButton.setStyle("-fx-font-size: " + fontSize + "px;");
            exitButton.setStyle("-fx-font-size: " + fontSize + "px;");
        });
    }
    public void onLearnClick() throws IOException { Launcher.changeScene("learning-space.fxml"); }

    public void onSandboxClick() throws IOException { Launcher.changeScene("sandbox-space.fxml"); }

    public void onSettingsClick() throws IOException { Launcher.changeScene("settings-space.fxml"); }

    public void onExitClick() { Platform.exit(); }
}
