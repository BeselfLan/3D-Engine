package Containers;

import Containers.Vector3.*;
import main.LinesComponent;

import java.awt.*;

import static Containers.Vector3.*;

public class Triangle {

    private Vector3 point1, point2, point3;
    private Vector2[] textureInfo;
    private Color color;

    public Triangle(Vector3 point1, Vector3 point2, Vector3 point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        textureInfo = new Vector2[3];
    }
    public Triangle() {
        point1 = new Vector3(0, 0, 0);
        point2 = new Vector3(0, 0, 0);
        point3 = new Vector3(0, 0, 0);
        textureInfo = new Vector2[3];
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

    public static Vector3 findNormal(Triangle tri) {
        // find normal of triangle (line perpendicular to the triangle)
        Vector3 line1 = new Vector3(tri.getP2().getX() - tri.getP1().getX(),
                tri.getP2().getY() - tri.getP1().getY(),
                tri.getP2().getZ() - tri.getP1().getZ());
        Vector3 line2 = new Vector3(tri.getP3().getX() - tri.getP1().getX(),
                tri.getP3().getY() - tri.getP1().getY(),
                tri.getP3().getZ() - tri.getP1().getZ());
        Vector3 normal = new Vector3(line1.getY() * line2.getZ() - line1.getZ() * line2.getY(),
                line1.getZ() * line2.getX() - line1.getX() * line2.getZ(),
                line1.getX() * line2.getY() - line1.getY() * line2.getX());
        // Normalizing normal
        float l = (float) Math.sqrt(normal.getX() * normal.getX() + normal.getY() * normal.getY() + normal.getZ() * normal.getZ());
        normal.divX(l); normal.divY(l); normal.divZ(l);
        return normal;
    }

    public static boolean isTriangleVisible(Vector3 normal, Triangle tri, Vector3 camera) {
        return normal.getX() * (tri.getP1().getX() - camera.getX()) +
                normal.getY() * (tri.getP1().getY() - camera.getY()) +
                normal.getZ() * (tri.getP1().getZ() - camera.getZ()) < 0.0f;
    }

    public static void illuminateTriangle(Vector3 light_direction, Vector3 normal, Triangle tri) {
        float l = (float) Math.sqrt(light_direction.getX() * light_direction.getX() + light_direction.getY() * light_direction.getY() + light_direction.getZ() * light_direction.getZ());
        light_direction.divX(l);
        light_direction.divY(l);
        light_direction.divZ(l);

        float dp = Math.abs(normal.getX() * light_direction.getX() + normal.getY() * light_direction.getY() + normal.getZ() * light_direction.getZ());
//                System.out.printf("%f\n", dp);
        tri.setColor(new Color(dp, dp, dp));
    }

    public static void drawTriangles(Triangle triangle, LinesComponent lc) {
        lc.addTriangle((int)triangle.getP1().getX(), (int)triangle.getP1().getY(), (int)triangle.getP2().getX(), (int)triangle.getP2().getY(), (int)triangle.getP3().getX(), (int)triangle.getP3().getY(), triangle.getColor());
    }
    // returns number of new triangles after clipping
    public static int triangleClipAgainstPlane(Vector3 plane_p, Vector3 plane_n, Triangle inTri, Triangle outTri1, Triangle outTri2) {
        plane_n.setVector3(normalize(plane_n));

        Vector3[] insidePoints = new Vector3[3]; int nInsidePointCount = 0;
        Vector3[] outsidePoints = new Vector3[3]; int nOutsidePointCount = 0;

        for (int i = 0; i < 3; i++) {
            insidePoints[i] = new Vector3();
            outsidePoints[i] = new Vector3();
        }

        float d0 = findPointToPlaneDist(inTri.getP1(), plane_n, plane_p);
        float d1 = findPointToPlaneDist(inTri.getP2(), plane_n, plane_p);
        float d2 = findPointToPlaneDist(inTri.getP3(), plane_n, plane_p);

        // check sign of distance to see if inside or outside plane
        if (d0 >= 0) {
            insidePoints[nInsidePointCount] = inTri.getP1();
            nInsidePointCount++;
        }
        else {
            outsidePoints[nOutsidePointCount] = inTri.getP1();
            nOutsidePointCount++;
        }
        if (d1 >= 0) {
            insidePoints[nInsidePointCount] = inTri.getP2();
            nInsidePointCount++;
        }
        else {
            outsidePoints[nOutsidePointCount] = inTri.getP2();
            nOutsidePointCount++;
        }
        if (d2 >= 0) {
            insidePoints[nInsidePointCount] = inTri.getP3();
            nInsidePointCount++;
        }
        else {
            outsidePoints[nOutsidePointCount] = inTri.getP3();
            nOutsidePointCount++;
        }

        // classify triangle points and break input triangle into smaller output tirangles
        // their are 4 possible outcomes
//        System.out.printf("%d %d\n", nInsidePointCount, nOutsidePointCount);
        if (nInsidePointCount == 0) { // all points outside of plane
            return 0; // clip entire triangle
        }
        else if (nInsidePointCount == 3) { // all points inside of plane
            outTri1.copy(inTri);
            outTri1.setColor(inTri.getColor());
            return 1; // return orignal triangle
        }
        else if (nInsidePointCount == 1) {
            // two points outside of plane, triangle cliped into a smaller one
            outTri1.setColor(inTri.getColor()); // copy colour of orignal triangle

            outTri1.setP1(insidePoints[0]);

            // two new points are the locations where the orignal triangle intersects the plane
            outTri1.setP2(vectorIntersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]));
            outTri1.setP3(vectorIntersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[1]));
//            outTri1.printTriangle();
            return 1;
        }
        else {
//        if (nInsidePointCount == 2 && nOutsidePointCount == 1) {
            // one point outside of plane, clipped triangle becomes a quad and need two new triangles
            outTri1.setColor(inTri.getColor());
            outTri2.setColor(inTri.getColor());

            // first triangle contains two inside points and a new point from where
            // the triangle intersects the plane
            outTri1.setP1(insidePoints[0]);
            outTri1.setP2(insidePoints[1]);
            outTri1.setP3(vectorIntersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0]));

            // second triangle contains one side point, the new point created above,
            // and another point where the triangle intersects the plane
            outTri2.setP1(insidePoints[1]);
            outTri2.setP2(vectorIntersectPlane(plane_p, plane_n, insidePoints[0], outsidePoints[0])); // setting outTri2 point2 to outtri1 point 3 breaks?
            outTri2.setP3(vectorIntersectPlane(plane_p, plane_n, insidePoints[1], outsidePoints[0]));
            return 2; // two new triangles
        }
    }
    private static float findPointToPlaneDist(Vector3 p, Vector3 plane_n, Vector3 plane_p) {
        return (plane_n.getX() * p.getX() + plane_n.getY() * p.getY() + plane_n.getZ() * p.getZ() - dotProduct(plane_n, plane_p));
    }
}
