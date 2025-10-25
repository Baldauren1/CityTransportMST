package org.example;

import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addEdge(String from, String to, int weight) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.putIfAbsent(to, new ArrayList<>());

        adjacencyList.get(from).add(new Edge(from, to, weight));
        adjacencyList.get(to).add(new Edge(to, from, weight)); // because undirected graph
    }

    public Set<String> getNodes() {
        return adjacencyList.keySet();
    }

    public List<Edge> getEdgesFrom(String node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (String node : adjacencyList.keySet()) {
            edges.addAll(adjacencyList.get(node));
        }
        return edges;
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        return getAllEdges().size() / 2; // divided by 2 because graph is undirected
    }
}
