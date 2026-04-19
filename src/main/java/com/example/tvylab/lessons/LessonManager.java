package com.example.tvylab.lessons;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class LessonManager {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Lesson load(File path) throws IOException {
        return mapper.readValue(path, Lesson.class);
    }

}
