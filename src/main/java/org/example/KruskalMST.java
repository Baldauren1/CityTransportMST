package org.example;

import java.util.*;

public class KruskalMST {
    private int operationCount = 0;

    public List<Edge> findMST(Graph graph) {
        List<Edge> mst = new ArrayList<>();
        UnionFind uf = new UnionFind();

        // 1 — makeSet for each node
        for (String node : graph.getNodes()) {
            uf.makeSet(node);
        }

        // 2 — sort edges by weight
        List<Edge> edges = graph.getAllEdges();
        edges.sort(Comparator.comparingInt(Edge::getWeight));

        // 3 — Kruskal
        for (Edge edge : edges) {
            operationCount++;
            String rootA = uf.find(edge.getFrom());
            String rootB = uf.find(edge.getTo());

            if (!rootA.equals(rootB)) {
                mst.add(edge);
                uf.union(rootA, rootB);

                if (mst.size() == graph.getVertexCount() - 1) {
                    break;
                }
            }
        }
        return mst;
    }

    public int getOperationCount() {
        return operationCount;
    }
}
