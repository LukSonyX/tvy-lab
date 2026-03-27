package com.example.tvylab;

import javafx.fxml.FXML;

import java.io.IOException;

public class SettingsController {
    @FXML
    public void onBackPressed() throws IOException { Launcher.changeScene("main-menu.fxml"); }

    // Slozka na naciteni Gates
    // Slozka na nacitani Lekci
}
