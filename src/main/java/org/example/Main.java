package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        String inputPath = "src/main/resources/ass_3_input.json";
        String outputPath = "src/main/resources/ass_3_output.json";
        String csvPath = "src/main/resources/results.csv";

        List<JsonGraphReader.JsonGraph> jsonGraphs = JsonGraphReader.readInput(inputPath);
        JsonResultWriter writer = new JsonResultWriter();

        List<String> csvLines = new ArrayList<>();
        csvLines.add("id,algorithm,vertices,total_cost,execution_time_ms,operation_count");

        for (JsonGraphReader.JsonGraph jsonGraph : jsonGraphs) {
            Graph graph = new Graph();
            for (String n : jsonGraph.nodes) {
                graph.addNode(n);
            }
            for (var e : jsonGraph.edges) {
                String from = (String) e.get("from");
                String to = (String) e.get("to");
                Number wn = (Number) e.get("weight");
                int weight = wn.intValue();
                graph.addEdge(from, to, weight);
            }

            // Print structure only for small graphs, which are less than 10 vertices
            if (jsonGraph.nodes.size() <= 10) {//output will be in concole
                System.out.println("Graph ID: " + jsonGraph.id);
                graph.printGraph();
                System.out.println("----------------------------");
            }

//            if (jsonGraph.nodes.size() <= 10) {
//                System.out.println("Opening visualization for Graph ID: " + jsonGraph.id);
//                GraphVisualizer.showGraph(graph);
//                break; //to not open 28 windows for all type of graphs
//            }

            PrimMST prim = new PrimMST();
            long startPrim = System.nanoTime();
            MSTResult primResult = prim.run(graph);
            long endPrim = System.nanoTime();
            double primTimeMs = (endPrim - startPrim) / 1_000_000.0;

            KruskalMST kruskal = new KruskalMST();
            long startKruskal = System.nanoTime();
            MSTResult kruskalResult = kruskal.run(graph);
            long endKruskal = System.nanoTime();
            double kruskalTimeMs = (endKruskal - startKruskal) / 1_000_000.0;

            Map<String, Object> result = new HashMap<>();
            result.put("graph_id", jsonGraph.id);
            result.put("input_stats", Map.of(
                    "vertices", jsonGraph.nodes.size(),
                    "edges", jsonGraph.edges.size()
            ));
            result.put("prim", Map.of(
                    "mst_edges", primResult.edges.stream().map(edge -> Map.of(
                            "from", edge.getFrom(),
                            "to", edge.getTo(),
                            "weight", edge.getWeight()
                    )).toList(),
                    "total_cost", primResult.totalCost,
                    "operations_count", primResult.operations,
                    "execution_time_ms", primTimeMs
            ));

            result.put("kruskal", Map.of(
                    "mst_edges", kruskalResult.edges.stream().map(edge -> Map.of(
                            "from", edge.getFrom(),
                            "to", edge.getTo(),
                            "weight", edge.getWeight()
                    )).toList(),
                    "total_cost", kruskalResult.totalCost,
                    "operations_count", kruskalResult.operations,
                    "execution_time_ms", kruskalTimeMs
            ));
            writer.addResult(result);

            csvLines.add(String.format(Locale.US, "%d,Kruskal,%d,%d,%.6f,%d",
                    jsonGraph.id,
                    jsonGraph.nodes.size(),
                    kruskalResult.totalCost,
                    kruskalTimeMs,
                    kruskalResult.operations));

            csvLines.add(String.format(Locale.US, "%d,Prim,%d,%d,%.6f,%d",
                    jsonGraph.id,
                    jsonGraph.nodes.size(),
                    primResult.totalCost,
                    primTimeMs,
                    primResult.operations));
        }

        writer.writeOutput(outputPath);
        System.out.println("Done. Results written to " + outputPath);

        try (FileWriter fw = new FileWriter(csvPath)) {
            for (String line : csvLines) {
                fw.write(line + System.lineSeparator());
            }
            System.out.println("CSV written to " + csvPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
