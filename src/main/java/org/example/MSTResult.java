package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MSTResult {
    public List<Edge> edges = new ArrayList<>();
    public int totalCost = 0;
    public int operations = 0;

    public MSTResult() {}

    public static MSTResult fromEdges(List<Edge> edgeList, int operationsCount) {
        MSTResult r = new MSTResult();
        r.edges = new ArrayList<>(edgeList);
        r.operations = operationsCount;
        r.totalCost = edgeList.stream().mapToInt(Edge::getWeight).sum();
        return r;
    }

    public void print() {
        System.out.println("MST edges:");
        for (Edge e : edges) {
            System.out.println(e.getFrom() + " - " + e.getTo() + " (" + e.getWeight() + ")");
        }
        System.out.println("Total cost: " + totalCost);
        System.out.println("Operations: " + operations);
        System.out.println("Vertices (approx): " + countVertices());
        System.out.println("Edges: " + edges.size());
    }

    private int countVertices() {
        return (int) edges.stream()
                .flatMap(e -> java.util.stream.Stream.of(e.getFrom(), e.getTo()))
                .distinct()
                .count();
    }
}
