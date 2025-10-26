package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class MSTAlgorithmsTest {

    @Test
    public void testPrimAndKruskalProduceSameTotalCost() {
        System.out.println("🔹 Running test: Prim vs Kruskal (correctness check)");

        Graph g = new Graph();
        g.addEdge("A", "B", 4);
        g.addEdge("A", "C", 3);
        g.addEdge("B", "C", 2);
        g.addEdge("B", "D", 5);
        g.addEdge("C", "D", 7);
        g.addEdge("D", "E", 6);
        g.addEdge("C", "E", 8);

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        MSTResult primRes = prim.run(g);
        MSTResult kruskalRes = kruskal.run(g);

        assertEquals(primRes.totalCost, kruskalRes.totalCost,
                "Prim and Kruskal must produce the same total cost");

        assertEquals(g.getNodes().size() - 1, primRes.edges.size(), "Prim MST should contain V-1 edges");
        assertEquals(g.getNodes().size() - 1, kruskalRes.edges.size(), "Kruskal MST should contain V-1 edges");

        assertTrue(isConnected(g, primRes.edges), "Prim MST must connect all vertices");
        assertTrue(isConnected(g, kruskalRes.edges), "Kruskal MST must connect all vertices");

        System.out.println("   Same total cost, correct edge count, and connected MST.");
    }

    private boolean isConnected(Graph graph, List<Edge> edges) {
        // Простой BFS для проверки связности
        if (edges.isEmpty()) return false;

        Map<String, List<String>> adj = new HashMap<>();
        for (Edge e : edges) {
            adj.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(e.getTo());
            adj.computeIfAbsent(e.getTo(), k -> new ArrayList<>()).add(e.getFrom());
        }

        Set<String> visited = new HashSet<>();
        String start = edges.get(0).getFrom();
        Queue<String> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String cur = queue.poll();
            for (String neigh : adj.getOrDefault(cur, List.of())) {
                if (visited.add(neigh)) {
                    queue.add(neigh);
                }
            }
        }

        return visited.containsAll(graph.getNodes());
    }
}
