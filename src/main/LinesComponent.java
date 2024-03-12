package main;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
// https://stackoverflow.com/questions/5801734/how-to-draw-lines-in-java

public class LinesComponent extends JComponent {
    private final LinkedList<Shape> lines = new LinkedList<Shape>();
    private final LinkedList<Shape> triangles = new LinkedList<Shape>();

    private static class Shape {
        final int x1, y1, x2, y2, x3, y3;
        final Color color;

        public Shape(int x1, int y1, int x2, int y2, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            x3 = 0;
            y3 = 0;
            this.color = color;
        }

        public Shape(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.x3 = x3;
            this.y3 = y3;
            this.color = color;
        }
    }

    public void addLine(int x1, int y1, int x2, int y2) {
        addLine(x1, y1, x2, y2, Color.black);
    }
    public void addLine(int x1, int y1, int x2, int y2, Color color) {
        lines.add(new Shape(x1, y1, x2, y2, color));
        repaint();
    }

    public void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        addTriangle(x1, y1, x2, y2, x3, y3, Color.black);
    }
    public void addTriangle(int x1, int y1, int x2, int y2, int x3, int y3, Color color) {
        triangles.add(new Shape(x1, y1, x2, y2, x3, y3, color));
        repaint();
    }

    public void clearLines() {
        lines.clear();
        triangles.clear();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape triangle : triangles) {
            int x[] = {triangle.x1, triangle.x2, triangle.x3}; // change 0,0 from top left to bottom right
            int y[] = {triangle.y1, triangle.y2,  triangle.y3};
            g.setColor(triangle.color);
            g.fillPolygon(x, y, 3);
        }
        for (Shape line : lines) {
            g.setColor(line.color);
            g.drawLine(line.x1, line.y1, line.x2, line.y2);
        }
    }
}
