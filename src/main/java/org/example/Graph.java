package org.example;

import java.util.*;

public class Graph {
    private final Map<String, List<Edge>> adjacencyList = new HashMap<>();

    public void addNode(String node) {
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(String from, String to, int weight) {
        adjacencyList.putIfAbsent(from, new ArrayList<>());
        adjacencyList.putIfAbsent(to, new ArrayList<>());
        adjacencyList.get(from).add(new Edge(from, to, weight));
        adjacencyList.get(to).add(new Edge(to, from, weight)); // undirected
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

    // for output in concol
    public void printGraph() {
        System.out.println("Graph structure:");
        for (String node : adjacencyList.keySet()) {
            System.out.print(" " + node + " -> ");
            List<String> neighbors = adjacencyList.get(node).stream()
                    .map(e -> e.getTo() + "(" + e.getWeight() + ")")
                    .toList();
            System.out.println(String.join(", ", neighbors));
        }
    }
//for output visual graph
    public List<Edge> getEdges() {
        List<Edge> edges = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (String node : adjacencyList.keySet()) {
            for (Edge e : adjacencyList.get(node)) {
                String key1 = e.getFrom() + "-" + e.getTo();
                String key2 = e.getTo() + "-" + e.getFrom();
                if (!seen.contains(key1) && !seen.contains(key2)) {
                    seen.add(key1);
                    edges.add(e);
                }
            }
        }
        return edges;
    }

}
