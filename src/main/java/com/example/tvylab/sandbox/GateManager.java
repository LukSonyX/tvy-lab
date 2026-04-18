package com.example.tvylab.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class GateManager {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static GateDefinition load(String filename) throws IOException {
        return mapper.readValue(new File(filename), GateDefinition.class);
    }

    public static void save(GateDefinition def, String filename) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filename), def);
    }
}
