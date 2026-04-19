package com.example.tvylab.settings;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class SettingsManager {

    private static final String FILE_PATH = "settings-config.json";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static Settings load() {
        try {
            return mapper.readValue(new File(FILE_PATH), Settings.class);
        } catch (Exception e) {
            Settings def = createDefault();
            save(def);
            return def;
        }
    }

    public static void save(Settings settings) {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(FILE_PATH), settings);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Settings createDefault() {
        Settings s = new Settings();
        s.gatesDir = "Gates/";
        s.lessonsDir = "Lessons/";
        s.fontSize = 16;
        s.language = "cz";
        return s;
    }
}