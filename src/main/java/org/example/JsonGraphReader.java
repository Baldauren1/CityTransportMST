package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonGraphReader {

    public static class JsonGraph {
        int id;
        List<String> nodes;
        List<Map<String, Object>> edges;
    }

    public static List<JsonGraph> readInput(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<JsonGraph>>>() {}.getType();
            Map<String, List<JsonGraph>> data = gson.fromJson(reader, type);
            return data.get("graphs");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
