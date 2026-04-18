package com.example.tvylab;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LanguageChanger {
    private static String currentLanguage = "cz";
    private static final Map<String, String> czech_table = Map.ofEntries(
            Map.entry("save", "Uložit"),
            Map.entry("delete", "Smazat"),
            Map.entry("back", "Zpět"),
            Map.entry("input", "Vstup"),
            Map.entry("output", "Výstup"),
            Map.entry("gates", "Hradla"),
            Map.entry("folder", "Složku"),
            Map.entry("folder_delete_title", "Odstranit složku s hradly?"),
            Map.entry("folder_delete_content", "Všechno v složce bude odstraněno!"),
            Map.entry("empty_sandbox", "Chyba: Prázdná plocha"),
            Map.entry("empty_sandbox_header", "Není nic k uložení!"),
            Map.entry("gate", "Hradlo"),
            Map.entry("name", "Název"),
            Map.entry("category", "Kategorie"),
            Map.entry("lessons_dir", "Složka s lekcemi:\n"),
            Map.entry("gates_dir", "Složka s hradly:\n"),
            Map.entry("learn", "Lekce"),
            Map.entry("sandbox", "Sandbox"),
            Map.entry("settings", "Nastavení"),
            Map.entry("exit", "Odejít"),
            Map.entry("gates_dir_chooser", "Vyberte složku s hradly"),
            Map.entry("browse", "Hledat"),
            Map.entry("language", "Jazyk:"),
            Map.entry("current_language", "Čeština"),
            Map.entry("", "")
    );

    public static Map<String, String> english_table = Map.ofEntries(
            Map.entry("save", "Save"),
            Map.entry("delete", "Delete"),
            Map.entry("back", "Back"),
            Map.entry("input", "Input"),
            Map.entry("output", "Output"),
            Map.entry("gates", "Gates"),
            Map.entry("folder", "Folder"),
            Map.entry("folder_delete_title", "Delete folder with gates?"),
            Map.entry("folder_delete_content", "Everything in the folder will be deleted!"),
            Map.entry("empty_sandbox", "Error: Empty workspace"),
            Map.entry("empty_sandbox_header", "Nothing to save!"),
            Map.entry("gate", "Gate"),
            Map.entry("name", "Name"),
            Map.entry("category", "Category"),
            Map.entry("lessons_dir", "Lessons directory:\n"),
            Map.entry("gates_dir", "Gates directory:\n"),
            Map.entry("learn", "Lessons"),
            Map.entry("sandbox", "Sandbox"),
            Map.entry("settings", "Settings"),
            Map.entry("exit", "Exit"),
            Map.entry("gates_dir_chooser", "Select gates folder"),
            Map.entry("browse", "Browse"),
            Map.entry("language", "Language:"),
            Map.entry("current_language", "English"),
            Map.entry("", "")
    );

    public static String get(String key) {
        if (currentLanguage.equals("cz")) {
            return czech_table.get(key);
        }
        if (currentLanguage.equals("en")) {
            return english_table.get(key);
        }
        return "";
    }

    public static void setLanguage(String lang) {
        currentLanguage = lang;
    }
}
