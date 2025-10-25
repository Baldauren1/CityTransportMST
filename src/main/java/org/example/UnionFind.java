package org.example;

import java.util.HashMap;
import java.util.Map;

public class UnionFind {

    private final Map<String, String> parent = new HashMap<>();
    private final Map<String, Integer> rank = new HashMap<>();

    public void makeSet(String node) {
        parent.put(node, node);
        rank.put(node, 0);
    }

    public String find(String node) {
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node)));
        }
        return parent.get(node);
    }

    public void union(String a, String b) {
        String rootA = find(a);
        String rootB = find(b);

        if (rootA.equals(rootB)) {
            return;
        }

        if (rank.get(rootA) < rank.get(rootB)) {
            parent.put(rootA, rootB);
        } else if (rank.get(rootA) > rank.get(rootB)) {
            parent.put(rootB, rootA);
        } else {
            parent.put(rootB, rootA);
            rank.put(rootA, rank.get(rootA) + 1);
        }
    }
}

