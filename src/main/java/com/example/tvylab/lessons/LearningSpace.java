package com.example.tvylab.lessons;

import com.example.tvylab.Launcher;
import com.example.tvylab.settings.Settings;
import com.example.tvylab.settings.SettingsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;


public class LearningSpace {


    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }

    @FXML
    private VBox gatesBox;
    @FXML
    private VBox basicsBox;
    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        Settings settings = SettingsManager.load();
        createTabsFromFolders(new File(settings.lessonsDir));
        for (int i = 0; i < 15; i++) {
            addLessonButton(gatesBox, "Some Name", "");
        }
    }

    public void openLesson(String jsonPath) throws IOException {
        Launcher.changeScene("lesson-view.fxml");
        try {
            Lesson lesson = LessonManager.load(jsonPath);
            LessonController.loadLesson(lesson);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLessonButton(VBox box, String name, String jsonPath) {
        Button btn = new Button(name);
        btn.setMaxWidth(Double.MAX_VALUE);

        btn.setOnAction(e -> {
            try {
                openLesson(jsonPath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        box.getChildren().add(btn);
    }

    private void createTabsFromFolders(File root) {
        File[] folders = root.listFiles();
        if (folders == null) return;

        for (File folder : folders) {
            tabPane.getTabs().add(new Tab(folder.getName()));
        }
    }
}
