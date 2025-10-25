package org.example;

import java.util.*;

public class Graph {

    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addNode(String node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(String from, String to, int weight) {
        addNode(from);
        addNode(to);

        adjacencyList.get(from).add(new Edge(from, to, weight));
        adjacencyList.get(to).add(new Edge(to, from, weight));
    }

    public Set<String> getNodes() {
        return adjacencyList.keySet();
    }

    public List<Edge> getEdgesFrom(String node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> list : adjacencyList.values()) {
            edges.addAll(list);
        }
        return edges;
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    public int getEdgeCount() {
        return getAllEdges().size() / 2;
    }
}
