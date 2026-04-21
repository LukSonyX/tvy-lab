package com.example.tvylab;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainMenuController {

    @FXML
    private Button learnBtn;
    @FXML
    private Button sandboxBtn;
    @FXML
    private Button settingsBtn;
    @FXML
    private Button exitBtn;

    @FXML
    public void initialize() {
        learnBtn.setText(LanguageChanger.get("learn"));
        sandboxBtn.setText(LanguageChanger.get("sandbox"));
        settingsBtn.setText(LanguageChanger.get("settings"));
        exitBtn.setText(LanguageChanger.get("exit"));
    }
    public void onLearnClick() throws IOException { Launcher.changeScene("learning-space.fxml"); }

    public void onSandboxClick() throws IOException { Launcher.changeScene("sandbox-space.fxml"); }

    public void onSettingsClick() throws IOException { Launcher.changeScene("settings-space.fxml"); }

    public void onExitClick() { Platform.exit(); }
}
