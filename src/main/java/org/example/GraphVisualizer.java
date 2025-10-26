package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class GraphVisualizer extends JPanel {

    private final Graph graph;

    public GraphVisualizer(Graph graph) {
        this.graph = graph;
        setPreferredSize(new Dimension(600, 600));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<String> nodes = graph.getNodes().stream().toList();
        int n = nodes.size();

        int radius = 200;
        int centerX = 300;
        int centerY = 300;

        Point[] positions = new Point[n];
        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n;
            int x = centerX + (int) (radius * Math.cos(angle));
            int y = centerY + (int) (radius * Math.sin(angle));
            positions[i] = new Point(x, y);
        }

        // for drawing edges
        g2.setColor(Color.LIGHT_GRAY);
        for (Edge e : graph.getEdges()) {
            int i = nodes.indexOf(e.getFrom());
            int j = nodes.indexOf(e.getTo());
            if (i >= 0 && j >= 0) {
                Point p1 = positions[i];
                Point p2 = positions[j];
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);

                // подпишем вес ребра
                int midX = (p1.x + p2.x) / 2;
                int midY = (p1.y + p2.y) / 2;
                g2.setColor(Color.GRAY);
                g2.drawString(String.valueOf(e.getWeight()), midX, midY);
                g2.setColor(Color.LIGHT_GRAY);
            }
        }

        // for printing vertices
        for (int i = 0; i < n; i++) {
            Point p = positions[i];
            String label = nodes.get(i);
            g2.setColor(new Color(100, 149, 237)); // синий
            g2.fillOval(p.x - 20, p.y - 20, 40, 40);
            g2.setColor(Color.WHITE);
            g2.drawString(label, p.x - 5, p.y + 5);
        }
    }

    public static void showGraph(Graph graph) {
        JFrame frame = new JFrame("Graph Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new GraphVisualizer(graph));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
