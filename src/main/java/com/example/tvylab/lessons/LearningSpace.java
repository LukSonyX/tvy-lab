package com.example.tvylab.lessons;

import com.example.tvylab.Launcher;

import java.io.IOException;


public class LearningSpace {
    // Nahravani lekci (do nastaveni pridat nejakou slozku na nacitani)
    // Vyber lekci

    // List<CustomButton> lections = ...;

    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }
    private void loadLessons() {}


}
