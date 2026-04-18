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
            Map.entry("learn", "Naučit se"),
            Map.entry("sandbox", "Sandbox"),
            Map.entry("settings", "Nastavení"),
            Map.entry("exit", "Odejít"),
            Map.entry("", "")
    );

    public static Map<String, String> english_table = Map.ofEntries(
            Map.entry("save", "Save")
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
