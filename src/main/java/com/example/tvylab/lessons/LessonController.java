package com.example.tvylab.lessons;

import com.example.tvylab.Launcher;
import com.example.tvylab.settings.Settings;
import com.example.tvylab.settings.SettingsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;

public class LessonController {
    public void onBackPressed() throws IOException { Launcher.changeScene("learning-space.fxml"); }

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
    }

    public void setLesson(Lesson lesson) {
        titleLabel.setText(lesson.title);
    }
}
