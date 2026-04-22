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
        addLessonButton(basicsBox, "Vstup a Výstup", new File("Lessons/Dynamic/basics_input.json"));
        addLessonButton(basicsBox, "Signál", new File("Lessons/Dynamic/signal_lesson.json"));
        addLessonButton(basicsBox, "Pravdivostní tabulka (AND)", new File("Lessons/Dynamic/and_lesson.json"));
        addLessonButton(basicsBox, "Hradlo NOT", new File("Lessons/Dynamic/not_lesson.json"));
        addLessonButton(basicsBox, "Hradlo Buffer", new File("Lessons/Dynamic/buffer_lesson.json"));
        addLessonButton(basicsBox, "Hradlo NAND", new File("Lessons/Dynamic/nand_lesson.json"));
    }

    public void openLesson(File jsonFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LessonData lessonData = mapper.readValue(jsonFile, LessonData.class);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/tvylab/lessons-builtin/base-lesson.fxml"));
            Parent root = loader.load();

            GenericLessonController controller = loader.getController();
            controller.initLesson(lessonData);

            Launcher.getPrimaryStage().getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addLessonButton(VBox box, String name, File lesson) {
        Button btn = new Button(name);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setOnAction(e -> {
            openLesson(lesson);
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
                    addLessonButton(vBox, name, new File(f.getPath()));
                }
            }
        }
    }
}
