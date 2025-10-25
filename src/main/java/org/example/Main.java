package org.example;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== City Transport MST: Prim Test ===");

        Graph graph = new Graph();
        graph.addEdge("A", "B", 4);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 7);
        graph.addEdge("C", "E", 8);
        graph.addEdge("D", "E", 6);

        PrimMST prim = new PrimMST();
        List<Edge> mst = prim.findMST(graph);

        System.out.println("\nMST edges:");
        int totalCost = 0;
        for (Edge e : mst) {
            System.out.println(e);
            totalCost += e.getWeight();
        }

        System.out.println("\nTotal cost: " + totalCost);
        System.out.println("Operations: " + prim.getOperationCount());
        System.out.println("Vertices: " + graph.getVertexCount());
        System.out.println("Edges: " + graph.getEdgeCount());
    }
}
