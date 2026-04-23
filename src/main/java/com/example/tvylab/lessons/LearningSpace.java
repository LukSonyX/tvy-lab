package com.example.tvylab.lessons;

import com.example.tvylab.LanguageChanger;
import com.example.tvylab.Launcher;
import com.example.tvylab.settings.Settings;
import com.example.tvylab.settings.SettingsManager;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.InputStream;


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

        // Bruteforce
        addInternalLessonButton(basicsBox, "Vstup a Výstup", "/com/example/tvylab/lessons/basics_input.json");
        addInternalLessonButton(basicsBox, "Signál", "/com/example/tvylab/lessons/signal_lesson.json");
        addInternalLessonButton(basicsBox, "Pravdivostní tabulka (AND)", "/com/example/tvylab/lessons/and_lesson.json");
        addInternalLessonButton(basicsBox, "Hradlo NOT", "/com/example/tvylab/lessons/not_lesson.json");
        addInternalLessonButton(basicsBox, "Hradlo Buffer", "/com/example/tvylab/lessons/buffer_lesson.json");
        addInternalLessonButton(basicsBox, "Hradlo NAND", "/com/example/tvylab/lessons/nand_lesson.json");
    }

    public void openInternalLesson(String lesson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream(lesson);
            if (is == null) {
                return;
            }
            LessonData lessonData = mapper.readValue(is, LessonData.class);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tvylab/lessons-builtin/base-lesson.fxml"));
            Parent root = loader.load();

            GenericLessonController controller = loader.getController();
            controller.initLesson(lessonData);

            Launcher.getPrimaryStage().getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openLesson(File jsonPath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LessonData lessonData = mapper.readValue(jsonPath, LessonData.class);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tvylab/lessons-builtin/base-lesson.fxml"));
            Parent root = loader.load();

            GenericLessonController controller = loader.getController();
            controller.initLesson(lessonData);

            Launcher.getPrimaryStage().getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLessonButton(VBox box, String name, String lesson) {
        Button btn = new Button(name);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> openLesson(new File(lesson)));
        box.getChildren().add(btn);
    }

    private void addInternalLessonButton(VBox box, String name, String lesson) {
        Button btn = new Button(name);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> openInternalLesson(lesson));
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
