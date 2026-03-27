package com.example.tvylab.sandbox;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

public class GateSaver {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void save(GateDefinition def, String filename) throws IOException {
        mapper.writerWithDefaultPrettyPrinter()
                .writeValue(new File(filename), def);
    }
}