package com.example.tvylab.settings;

import com.example.tvylab.LanguageChanger;
import com.example.tvylab.Launcher;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;

public class SettingsController {
    public static File gatesDirectory;
    public static File lessonsDirectory;
    public static int fontSize;

    private Settings settings;

    @FXML
    private Label lessonsDir;

    @FXML
    private Label gatesDir;

    @FXML
    private Slider fontSlider;

    @FXML
    private Button backBtn;

    @FXML
    private Button browseOne;

    @FXML
    private Button browseTwo;

    @FXML
    private Label settingsLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private ChoiceBox<String> languageChooser;

    @FXML
    public void initialize() {

        settings = SettingsManager.load();

        gatesDirectory = new File(settings.gatesDir);
        lessonsDirectory = new File(settings.lessonsDir);
        fontSize = settings.fontSize;

        backBtn.setText(LanguageChanger.get("back"));
        browseOne.setText(LanguageChanger.get("browse"));
        browseTwo.setText(LanguageChanger.get("browse"));
        settingsLabel.setText(LanguageChanger.get("settings"));
        languageLabel.setText(LanguageChanger.get("language"));

        LanguageChanger.setLanguage(settings.language);

        languageChooser.getItems().addAll("English", "Čeština");
        languageChooser.getSelectionModel().select(
                settings.language.equals("en") ? "English" : "Čeština"
        );

        languageChooser.setOnAction(e -> {
            String selected = languageChooser.getValue();

            if ("English".equals(selected)) {
                settings.language = "en";
            } else {
                settings.language = "cz";
            }

            LanguageChanger.setLanguage(settings.language);
            SettingsManager.save(settings);
        });

        lessonsDir.setText(LanguageChanger.get("lessons_dir") + settings.lessonsDir);
        gatesDir.setText(LanguageChanger.get("gates_dir") + settings.gatesDir);

        fontSlider.setValue(settings.fontSize);
        updateFont(settings.fontSize);

        fontSlider.valueProperty().addListener((obs, oldVal, newVal) -> updateFont(newVal.intValue()));
    }

    @FXML
    public void onBackPressed() throws IOException {
        Launcher.changeScene("main-menu.fxml");
    }

    @FXML
    public void onGatesFolderPressed() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(LanguageChanger.get("gates_dir_chooser"));

        File selected = chooser.showDialog(Launcher.getPrimaryStage());
        if (selected != null) {
            settings.gatesDir = selected.getAbsolutePath();
            SettingsManager.save(settings);

            gatesDirectory = selected;
            gatesDir.setText(LanguageChanger.get("gates_dir") + selected.getAbsolutePath());
        }
    }

    @FXML
    public void onLessonsFolderPressed() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle(LanguageChanger.get("lessons_dir"));

        File selected = chooser.showDialog(Launcher.getPrimaryStage());
        if (selected != null) {
            settings.lessonsDir = selected.getAbsolutePath();
            SettingsManager.save(settings);

            lessonsDirectory = selected;
            lessonsDir.setText(LanguageChanger.get("lessons_dir") + selected.getAbsolutePath());
        }
    }

    private void updateFont(int size) {
        settings.fontSize = size;
        SettingsManager.save(settings);

        fontSize = size;
    }
}