package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Graph graph = new Graph();
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 7);
        graph.addEdge("C", "E", 8);
        graph.addEdge("D", "E", 6);

        System.out.println("=== City Transport MST: Prim ===");
        PrimMST prim = new PrimMST();
        List<Edge> primEdges = prim.findMST(graph);
        MSTResult primResult = MSTResult.fromEdges(primEdges, prim.getOperationCount());
        primResult.print();

        System.out.println("\n=== City Transport MST: Kruskal ===");
        KruskalMST kruskal = new KruskalMST();
        List<Edge> kruskalEdges = kruskal.findMST(graph);
        MSTResult kruskalResult = MSTResult.fromEdges(kruskalEdges, kruskal.getOperationCount());
        kruskalResult.print();

        System.out.println("\n=== Comparison ===");
        if (primResult.totalCost == kruskalResult.totalCost) {
            System.out.println(" Same total cost: " + primResult.totalCost);
        } else {
            System.out.println(" Different total cost: Prim = " + primResult.totalCost +
                    ", Kruskal = " + kruskalResult.totalCost);
        }

        if (primResult.edges.size() == kruskalResult.edges.size()) {
            System.out.println(" Same number of edges: " + primResult.edges.size());
        } else {
            System.out.println(" Different number of edges");
        }

        System.out.println("\nDone.");
    }
}
