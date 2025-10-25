package org.example;

import java.util.*;

public class PrimMST {
    private int operationCount = 0;

    public List<Edge> findMST(Graph graph) {
        Set<String> visited = new HashSet<>();
        List<Edge> mstEdges = new ArrayList<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        String start = graph.getNodes().iterator().next();
        visited.add(start);
        pq.addAll(graph.getEdgesFrom(start));

        while (!pq.isEmpty() && mstEdges.size() < graph.getVertexCount() - 1) {
            Edge edge = pq.poll();
            operationCount++;

            if (!visited.contains(edge.getTo())) {
                visited.add(edge.getTo());
                mstEdges.add(edge);

                for (Edge next : graph.getEdgesFrom(edge.getTo())) {
                    if (!visited.contains(next.getTo())) {
                        pq.add(next);
                    }
                }
            }
        }
        return mstEdges;
    }

    public int getOperationCount() {
        return operationCount;
    }
}
