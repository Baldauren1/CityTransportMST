# Assignment 3 — City Transportation Network Optimization (MST)

## 1. Objective
The objective of this project was to apply Prim’s and Kruskal’s algorithms to optimize a city transportation network by finding the Minimum Spanning Tree (MST) —
the minimal set of roads that connects all city districts with the lowest possible total cost.

Both algorithms were implemented from scratch in Java 21,
and their performance was compared using automatically generated datasets of different sizes.
The project also includes JSON/CSV processing, automated tests, and a simple visualization for small graphs.
---

## 2. Project Structure
````
CityTransportMST/
 ├── src/
 │   ├── main/java/org/example/
 │   │    ├── Edge.java
 │   │    ├── Graph.java
 │   │    ├── PrimMST.java
 │   │    ├── KruskalMST.java
 │   │    ├── UnionFind.java
 │   │    ├── JsonGraphReader.java
 │   │    ├── JsonResultWriter.java
 │   │    ├── GraphVisualizer.java
 │   │    └── Main.java
 │   ├── main/resources/
 │   │    ├── ass_3_input.json
 │   │    ├── ass_3_output.json
 │   │    └── results.csv
 │   └── test/java/org/example/
 │        └── MSTAlgorithmsTest.java
 ├── pom.xml
 └── README.md

````

---

## 3. Input Datasets
- **28 graphs total:**
    - 5 small (5–30 vertices)
    - 10 medium (30–300 vertices)
    - 10 large (300–1000 vertices)
    - 3 extra-large (1000–2000 vertices)
- Each graph is stored in `ass_3_input.json` with:
  ```json
  {
    "id": 1,
    "nodes": ["v0", "v1", "v2", "v3", "v4"],
    "edges": [
      {"from": "v0", "to": "v1", "weight": 67}, ...
    ]
  }

All graphs were automatically read and processed using the custom class
*JsonGraphReader.java*, and results were written by *JsonResultWriter.java*.

---

## 4. Output and Evaluation
**Program generates two files:**
 - ass_3_output.json - detailed MST results
 - results.csv - summarized metrics for analysis
Each contains:
- Total MST cost
- Execution time (ms)
- Number of vertices
- Operation count
- Graph ID
- Algorithm name

---

## 5. Example CSV Output
![img.png](img.png)

*Full data is available in the file: src/main/resources/results.csv
and was used to perform statistical and graphical analysis.*

---

## 6. Analysis and Discussion
### **Correctness:**
- Both algorithms produce identical total MST costs for every graph.
- MST edge count = V - 1, connected and acyclic.
- Disconnected graphs handled correctly.
  JUnit tests (MSTAlgorithmsTest.java) verified these properties automatically.

---

### **Performance Comparison**

| Size          | Avg. Vertices | Faster Algorithm   | Comments                                          |
| ------------- | ------------- | ------------------ | ------------------------------------------------- |
| Small (≤30)   | ~10           | **Prim**           | Heap-based selection faster on dense small graphs |
| Medium (≤300) | ~100          | **Prim ≈ Kruskal** | Similar performance                               |
| Large (≤1000) | ~500          | **Kruskal**        | Sorting overhead small, Union-Find efficient      |
| Extra-Large   | ≥1000         | **Kruskal**        | Better scalability due to simpler structure       |

Prim’s algorithm is more efficient for dense graphs (many edges),
while Kruskal’s performs better for sparse large graphs.

---

### Theoretical Comparison
| Property                  | Prim’s Algorithm | Kruskal’s Algorithm       |
| ------------------------- | ---------------- | ------------------------- |
| Type                      | Greedy           | Greedy                    |
| Main data structure       | PriorityQueue    | Union-Find (Disjoint Set) |
| Time complexity           | O(E log V)       | O(E log E) ≈ O(E log V)   |
| Suitable for              | Dense graphs     | Sparse graphs             |
| Implementation difficulty | Moderate         | Easier                    |
| Space complexity          | O(V + E)         | O(V + E)                  |
These theoretical expectations matched the experimental results:
Prim consistently outperformed Kruskal for dense graphs, while Kruskal remained competitive on sparse ones.

---

### Comparison with External Implementations
To validate my implementation, I compared results with well-known sources:

| Source                        | Observations                                                                    | Comparison to My Results                                                    |
| ------------------------------| ------------------------------------------------------------------------------- | --------------------------------------------------------------------------- |
| **GeeksforGeeks (2023)**      | Kruskal is better for sparse graphs, Prim for dense.                            | Matches exactly — my tests confirm same behavior.                           |
| **Baeldung (2022)**           | Prim with adjacency lists and `PriorityQueue` shows linear-logarithmic scaling. | Same trend observed: runtime grows slowly even for 2000 nodes.              |
| **MIT OpenCourseWare(6.006)** | Describes O(E log V) for both, with Kruskal affected more by sorting overhead.  | My Kruskal runtime is consistently higher by factor ~3–6× for large graphs. |

---

### Operation Counts
- Prim performs more key updates and comparisons (priority queue operations).
- Kruskal performs fewer operations overall due to edge sorting once per run.

---

### Implementation Challenges
During development, several issues appeared:
- Result Consistency Between Algorithms Prim and Kruskal initially returned edges in different orders, which looked incorrect when comparing outputs.
Solved by normalizing MST edge lists before writing to JSON.
- JSON Parsing Errors:
Initially, I received `NullPointerException` because weights were read as doubles.
Solved by casting:
 ````java
int weight = ((Double) e.get("weight")).intValue();
````

- Git Branch Workflow:
  Accidentally worked on the wrong branch several times;
  later adopted a strict feature-branch workflow (`feature/mst-prim, feature/mst-kruskal`, etc.), which improved organization.
- Timing Precision:
  Early tests showed 0 ms execution time.
  Fixed by switching from `System.currentTimeMillis() → System.nanoTime()`.
- Union-Find Logic:
  My initial `UnionFind` caused incorrect merges, later rewritten to track parent and rank arrays.
- Disconnected Graphs:
  Both algorithms were updated to handle cases where no MST exists.

Solving these problems helped me understand how algorithms behave in practice, not just in theory.

---

## 7. Conclusions
From both theoretical and experimental analysis, the following conclusions were reached:
- Correctness: Both algorithms produce identical MST total cost and valid edge structures. 
- Performance: Prim’s algorithm performs better on dense graphs, while Kruskal’s algorithm can be competitive on sparse networks.
- Scalability: For graphs with more than 1000 vertices, Prim becomes significantly faster.
- Readability & Modularity: Using separate classes (Graph, Edge, UnionFind) improved code clarity.
- Practical insight: The real-world transportation networks are usually dense, meaning Prim’s algorithm would generally be more suitable for this scenario.

---

## 8. Bonus: Graph Design in Java
Implemented custom classes:
- Graph.java — adjacency structure for vertices and edges
- Edge.java — weighted undirected edge

These were fully integrated with both MST algorithms.

As part of the bonus task, I implemented a custom graph visualization tool in Java using Swing and AWT.

`GraphVisualizer.java`
- Displays graph nodes as blue circles.
- Draws weighted edges with labels.
- Automatically arranges nodes in a circular layout.
- Used only for small graphs (5–10 vertices) due to complexity.
![img_1.png](img_1.png)

## 9. References
1. [GeeksforGeeks – Prim’s and Kruskal’s Algorithm in Java](https://www.geeksforgeeks.org/java/kruskals-algorithm-in-java/)
Used to test the O(E log V) complexity formula and the Kruskal/Prim code structure. Also helped me understand how to properly initialize UnionFind for Kruskal.
2. [Baeldung – Working with Graphs in Java](https://www.baeldung.com/java-graphs)
Modern tutorial on the implementation of graphs in Java. I used the idea of graph representation via Map<String, List<Edge>> - This helped me to make the structure of Graph.java clean and convenient for MST algorithms.
3. [MIT OpenCourseWare – 6.006 Introduction to Algorithms (MST)](https://ocw.mit.edu/courses/6-006-introduction-to-algorithms-spring-2020/6afd7e9a85d7d36e204533569f9fccf6_MIT6_006S20_r09.pdf)
Lectures on the Minimum Spanning Tree. Hence the formal explanation of the differences between Prim and Kruskal, and also why Kruskal is less scalable with very large graphs.
4. [CLRS – “Introduction to Algorithms”, 3rd Edition](https://www.cs.mcgill.ca/~akroit/math/compsci/Cormen%20Introduction%20to%20Algorithms.pdf)
I used Prim and Kruskal formulas, diagrams, and proofs of correctness, as well as a description of the data structures (heap, disjoint set).
5. [Oracle Java Documentation)](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/package-summary.html)
Used when working with Map, List, PriorityQueue, and debugging compilation errors.

While working on this assignment, I spent most of the time debugging the JSON parsing  and testing large datasets. Overall, I enjoyed the process — it helped me see how  theoretical algorithms can be used for real problems like transport network optimization.

