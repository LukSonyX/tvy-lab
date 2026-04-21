package com.example.tvylab.lessons;

import com.example.tvylab.LanguageChanger;
import com.example.tvylab.Launcher;
import com.example.tvylab.settings.Settings;
import com.example.tvylab.settings.SettingsManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;


public class LearningSpace {

    @FXML
    public Button backBtn;

    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }

    @FXML
    private VBox basicsBox;
    @FXML
    private TabPane tabPane;

    @FXML
    public void initialize() {
        Settings settings = SettingsManager.load();
        tabPane.getTabs().get(0).setText(LanguageChanger.get("basics"));
        backBtn.setText(LanguageChanger.get("back"));
        createTabsFromFolders(new File(settings.lessonsDir));

        addLessonButton(basicsBox, "First", "lesson-input-cz.fxml");
    }

    public void openLesson(String lessonSrc) throws IOException {
        Launcher.changeScene("/com/example/tvylab/lessons-builtin/" + lessonSrc);

    }

    private void addLessonButton(VBox box, String name, String lesson) {
        Button btn = new Button(name);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            try {
                openLesson(lesson);
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
            Tab newTab = new Tab(folder.getName());
            VBox vBox = new VBox();
            ScrollPane scrollPane = new ScrollPane(vBox);
            scrollPane.setFitToWidth(true);

            newTab.setContent(scrollPane);

            tabPane.getTabs().add(newTab);

            File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));
            if (files != null) {
                for (File f : files) {
                    String name = f.getName().replace(".json", "");
                    addLessonButton(vBox, name, f.getPath());
                }
            }
        }
    }
}
