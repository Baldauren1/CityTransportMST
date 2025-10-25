package org.example;

import java.util.*;

public class PrimMST {

    public static class Result {
        public final List<Edge> mstEdges = new ArrayList<>();
        public long totalCost = 0;
        public long operations = 0;        // rough count of key ops (push/poll/comparisons)
        public double executionTimeMs = 0;
        public int vertices = 0;
        public int edges = 0;
        public boolean connected = false;  // true if MST has V-1 edges
    }

    public Result run(Graph graph) {
        Result res = new Result();
        long start = System.nanoTime();

        if (graph == null) {
            res.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
            return res;
        }

        int V = graph.getVertexCount();
        res.vertices = V;
        res.edges = graph.getEdgeCount();

        if (V == 0) {
            res.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
            return res;
        }

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));
        Set<String> visited = new HashSet<>();

        Iterator<String> it = graph.getNodes().iterator();
        if (!it.hasNext()) {
            res.executionTimeMs = (System.nanoTime() - start) / 1_000_000.0;
            return res;
        }
        String startNode = it.next();
        visited.add(startNode);

        for (Edge e : graph.getEdgesFrom(startNode)) {
            pq.add(e);
            res.operations++; // push
        }

        while (!pq.isEmpty() && res.mstEdges.size() < V - 1) {
            Edge e = pq.poll();
            res.operations++;

            String u = e.getFrom();
            String v = e.getTo();

            String next;
            if (visited.contains(u) && !visited.contains(v)) next = v;
            else if (visited.contains(v) && !visited.contains(u)) next = u;
            else {
                res.operations++;
                continue;
            }

            visited.add(next);
            res.mstEdges.add(e);
            res.totalCost += e.getWeight();

            for (Edge adj : graph.getEdgesFrom(next)) {
                String other = adj.getTo();
                if (!visited.contains(other)) {
                    pq.add(adj);
                    res.operations++; // push
                }
            }
        }

        res.connected = (res.mstEdges.size() == Math.max(0, V - 1));
        long end = System.nanoTime();
        res.executionTimeMs = (end - start) / 1_000_000.0;
        return res;
    }
}
