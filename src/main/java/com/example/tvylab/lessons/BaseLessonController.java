package com.example.tvylab.lessons;

import com.example.tvylab.Launcher;

import java.io.IOException;

public class BaseLessonController {
    public void onBackPressed() throws IOException {
        Launcher.changeScene("learning-space.fxml");
    }

}
