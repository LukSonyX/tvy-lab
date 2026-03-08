package com.example.tvylab;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private VBox vbox;

    @FXML
    public void initialize() {
    }
    public void onLearnClick() throws IOException { Launcher.changeScene("learning-space.fxml"); }

    public void onSandboxClick() throws IOException { Launcher.changeScene("sandbox-space.fxml"); }

    public void onSettingsClick() throws IOException { Launcher.changeScene("settings-space.fxml"); }

    public void onExitClick() { Platform.exit(); }
}
