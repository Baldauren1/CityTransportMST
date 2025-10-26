package org.example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        String inputPath = "src/main/resources/ass_3_input.json";
        String outputPath = "src/main/resources/ass_3_output.json";

        List<JsonGraphReader.JsonGraph> jsonGraphs = JsonGraphReader.readInput(inputPath);
        JsonResultWriter writer = new JsonResultWriter();

        for (JsonGraphReader.JsonGraph jsonGraph : jsonGraphs) {

            // Build graph
            Graph graph = new Graph();
            for (String n : jsonGraph.nodes) {
                graph.addNode(n);
            }
            for (var e : jsonGraph.edges) {
                String from = (String) e.get("from");
                String to = (String) e.get("to");
                int weight = ((Double) e.get("weight")).intValue();
                graph.addEdge(from, to, weight);
            }

            // Run Prim
            PrimMST prim = new PrimMST();
            long startPrim = System.currentTimeMillis();
            MSTResult primResult = prim.run(graph);
            long endPrim = System.currentTimeMillis();
            long primTime = (endPrim - startPrim) * 1000;

            // Run Kruskal
            KruskalMST kruskal = new KruskalMST();
            long startKruskal = System.currentTimeMillis();
            MSTResult kruskalResult = kruskal.run(graph);
            long endKruskal = System.currentTimeMillis();
            long kruskalTime = (endKruskal - startKruskal) * 1000;

            // Write to result JSON
            Map<String, Object> result = new HashMap<>();
            result.put("graph_id", jsonGraph.id);
            result.put("input_stats", Map.of(
                    "vertices", jsonGraph.nodes.size(),
                    "edges", jsonGraph.edges.size()
            ));
            result.put("prim", Map.of(
                    "mst_edges", primResult.edges.stream().map(e -> Map.of(
                            "from", e.getFrom(),
                            "to", e.getTo(),
                            "weight", e.getWeight()
                    )).toList(),
                    "total_cost", primResult.totalCost,
                    "operations_count", primResult.operations,
                    "execution_time_ms", primTime
            ));
            result.put("kruskal", Map.of(
                    "mst_edges", kruskalResult.edges.stream().map(e -> Map.of(
                            "from", e.getFrom(),
                            "to", e.getTo(),
                            "weight", e.getWeight()
                    )).toList(),
                    "total_cost", kruskalResult.totalCost,
                    "operations_count", kruskalResult.operations,
                    "execution_time_ms", kruskalTime
            ));
            writer.addResult(result);
        }

        writer.writeOutput(outputPath);
        System.out.println(" Done. Results written to ass_3_output.json");
    }
}
