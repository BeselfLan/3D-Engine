package Containers;

import Containers.Vector3.*;
import main.LinesComponent;

import java.awt.*;

import static Containers.Vector3.*;

public class Triangle {

    private Vector3 point1, point2, point3;
    private Color color;

    public Triangle(Vector3 point1, Vector3 point2, Vector3 point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }
    public Triangle() {
        point1 = new Vector3(0, 0, 0);
        point2 = new Vector3(0, 0, 0);
        point3 = new Vector3(0, 0, 0);
    }

    public Vector3 getP1() {
        return point1;
    }
    public Vector3 getP2() {
        return point2;
    }
    public Vector3 getP3() {
        return point3;
    }
    public Color getColor() {
        return color;
    }

    public void setP1(Vector3 newPoint) {
        point1 = newPoint;
    }
    public void setP2(Vector3 newPoint) {
        point2 = newPoint;
    }
    public void setP3(Vector3 newPoint) {
        point3 = newPoint;
    }
    public void setColor(Color newColor) {
        color = newColor;
    }

    public void clear() {
        Vector3 empty = new Vector3(0, 0, 0);
        point1 = empty;
        point2 = empty;
        point3 = empty;
    }

    public void translateX(float val) {
        point1.addX(val);
        point2.addX(val);
        point3.addX(val);
    }

    public void translateY(float val) {
        point1.addY(val);
        point2.addY(val);
        point3.addY(val);
    }

    public void translateZ(float val) {
        point1.addZ(val);
        point2.addZ(val);
        point3.addZ(val);
    }

    public void scaleBy2D(float val) {
        point1.mulX(val);
        point1.mulY(val);
        point2.mulX(val);
        point2.mulY(val);
        point3.mulX(val);
        point3.mulY(val);
    }

    // get rid of pass by reference
    public void copy(Triangle other) {
        point1 = new Vector3(other.point1.getX(), other.point1.getY(), other.point1.getZ());
        point2 = new Vector3(other.point2.getX(), other.point2.getY(), other.point2.getZ());
        point3 = new Vector3(other.point3.getX(), other.point3.getY(), other.point3.getZ());
    }

    public void printTriangle() {
        System.out.println("----------------");
        System.out.printf("(%f %f %f) ", point1.getX(), point1.getY(), point1.getZ());
        System.out.printf("(%f %f %f) ", point2.getX(), point2.getY(), point2.getZ());
        System.out.printf("(%f %f %f)", point3.getX(), point3.getY(), point3.getZ());
        System.out.printf("\n");
    }
}
