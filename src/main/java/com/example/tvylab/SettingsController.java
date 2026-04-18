package com.example.tvylab;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

public class SettingsController {
    public static File gatesDirectory = new File("Gates/");
    public static File lessonsDirectory = new File("Lessons/");
    public static String fontSize = "16";

    @FXML
    Label lessonsDir;
    @FXML
    Label gatesDir;
    @FXML private Slider fontSlider;

    @FXML
    public void initialize() throws IOException {
        lessonsDir.setText(LanguageChanger.get("lessons_dir") + lessonsDirectory.toString() + "/");
        gatesDir.setText(LanguageChanger.get("gates_dir") + gatesDirectory.toString() + "/");

        fontSlider.setValue(Double.parseDouble(fontSize));
        updateFont((int) fontSlider.getValue());

        fontSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            try {
                updateFont((int) newVal.intValue());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }

    @FXML
    public void onGatesFolderPressed() {
        DirectoryChooser gateDirChooser = new DirectoryChooser();
        gateDirChooser.setTitle("Choose Gates Directory!");
        File selected = gateDirChooser.showDialog(Launcher.getPrimaryStage());
        if (selected != null) {
            gatesDirectory = selected;
            gatesDir.setText("Gates Directory: " + selected + "/");
        }
    }

    private void updateFont(int size) throws IOException {
        fontSize = String.valueOf(size);
        //Launcher.changeScene("settings-space.fxml");
        //root.setStyle("-fx-font-size: " + size + "px;");
    }
}
