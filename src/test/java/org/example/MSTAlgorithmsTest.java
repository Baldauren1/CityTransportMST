package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class MSTAlgorithmsTest {

    //  2a. CORRECTNESS TESTS
    @Test
    public void testPrimAndKruskalSameTotalCost() {
        System.out.println("-> Test 1: Prim vs Kruskal – correctness");

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
                "Prim and Kruskal must produce the same MST total cost");
        System.out.println(" Total cost matches: " + primRes.totalCost);

        assertEquals(g.getNodes().size() - 1, primRes.edges.size(),
                "Prim MST must have V-1 edges");
        assertEquals(g.getNodes().size() - 1, kruskalRes.edges.size(),
                "Kruskal MST must have V-1 edges");
        System.out.println(" Correct edge count (V−1)");

        assertTrue(isConnected(g, primRes.edges), "Prim MST must connect all vertices");
        assertTrue(isConnected(g, kruskalRes.edges), "Kruskal MST must connect all vertices");
        System.out.println(" MST connects all vertices");

        assertTrue(isAcyclic(primRes.edges, g), "Prim MST must be acyclic");
        assertTrue(isAcyclic(kruskalRes.edges, g), "Kruskal MST must be acyclic");
        System.out.println(" MST is acyclic (no cycles)");
    }

    @Test
    public void testDisconnectedGraphHandledGracefully() {
        System.out.println("\n-> Test 2: Disconnected graph handling");

        Graph g = new Graph();
        g.addEdge("A", "B", 1);
        g.addNode("C"); // isolated vertex (disconnected)

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        MSTResult primRes = prim.run(g);
        MSTResult kruskalRes = kruskal.run(g);

        assertTrue(primRes.edges.size() < g.getNodes().size() - 1,
                "Prim should detect disconnected graph");
        assertTrue(kruskalRes.edges.size() < g.getNodes().size() - 1,
                "Kruskal should detect disconnected graph");
        System.out.println(" Disconnected graph handled correctly");
    }

    //  2b. PERFORMANCE AND CONSISTENCY TESTS
    @Test
    public void testPerformanceAndConsistency() {
        System.out.println("\n-> Test 3: Performance & Consistency");

        Graph g = new Graph();
        int n = 25;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                g.addEdge("v" + i, "v" + j, (int) (Math.random() * 100));
            }
        }

        PrimMST prim = new PrimMST();
        KruskalMST kruskal = new KruskalMST();

        // Measure time for Prim
        long startPrim = System.nanoTime();
        MSTResult prim1 = prim.run(g);
        long endPrim = System.nanoTime();
        double primTime = (endPrim - startPrim) / 1_000_000.0;

        // Measure time for Kruskal
        long startKruskal = System.nanoTime();
        MSTResult kruskal1 = kruskal.run(g);
        long endKruskal = System.nanoTime();
        double kruskalTime = (endKruskal - startKruskal) / 1_000_000.0;

        System.out.printf("    Prim time: %.4f ms%n", primTime);
        System.out.printf("    Kruskal time: %.4f ms%n", kruskalTime);

        assertTrue(primTime >= 0, "Prim execution time must be >= 0");
        assertTrue(kruskalTime >= 0, "Kruskal execution time must be >= 0");

        assertTrue(prim1.operations >= 0, "Prim operation count must be >= 0");
        assertTrue(kruskal1.operations >= 0, "Kruskal operation count must be >= 0");

        MSTResult prim2 = prim.run(g);
        MSTResult kruskal2 = kruskal.run(g);

        assertEquals(prim1.totalCost, prim2.totalCost, "Prim MST must be reproducible");
        assertEquals(kruskal1.totalCost, kruskal2.totalCost, "Kruskal MST must be reproducible");

        System.out.println(" Performance and consistency verified.");
    }


    //  Helper Methods
    // BFS to check connectivity
    private boolean isConnected(Graph graph, List<Edge> edges) {
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

    // Check acyclicity via DFS
    private boolean isAcyclic(List<Edge> edges, Graph graph) {
        Map<String, List<String>> adj = new HashMap<>();
        for (Edge e : edges) {
            adj.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(e.getTo());
            adj.computeIfAbsent(e.getTo(), k -> new ArrayList<>()).add(e.getFrom());
        }

        Set<String> visited = new HashSet<>();
        return !hasCycleDFS(adj, visited, null, edges.get(0).getFrom());
    }

    private boolean hasCycleDFS(Map<String, List<String>> adj, Set<String> visited, String parent, String node) {
        visited.add(node);
        for (String neigh : adj.getOrDefault(node, List.of())) {
            if (!visited.contains(neigh)) {
                if (hasCycleDFS(adj, visited, node, neigh)) return true;
            } else if (!neigh.equals(parent)) {
                return true;
            }
        }
        return false;
    }
}
