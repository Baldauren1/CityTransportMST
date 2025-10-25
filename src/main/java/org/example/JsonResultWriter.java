package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonResultWriter {

    private final List<Map<String, Object>> results = new ArrayList<>();

    public void addResult(Map<String, Object> result) {
        results.add(result);
    }

    public void writeOutput(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(Map.of("results", results), writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
