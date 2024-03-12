package Containers;

import Containers.Triangle;
import Containers.Vector3;
import main.LinesComponent;
import main.Obj;

public class Cube extends Obj {
    private Vector3[] points = new Vector3[8];
    private static Triangle[] triangles = new Triangle[12];

    // first 4 points make front square, last 4 points make back square, topRightClose is the front square and points go clockwise
    public Cube(Vector3 topLeftClose, float size, LinesComponent lc) {
        super(lc);
        points[0] = topLeftClose;
        points[1] = new Vector3(topLeftClose.getX(), topLeftClose.getY() + size, topLeftClose.getZ());
        points[2] = new Vector3(topLeftClose.getX() + size, topLeftClose.getY() + size, topLeftClose.getZ());
        points[3] = new Vector3(topLeftClose.getX() + size, topLeftClose.getY(), topLeftClose.getZ());
        points[4] = new Vector3(topLeftClose.getX(), topLeftClose.getY(), topLeftClose.getZ() + size);
        points[5] = new Vector3(topLeftClose.getX(), topLeftClose.getY() + size, topLeftClose.getZ() + size);
        points[6] = new Vector3(topLeftClose.getX() + size, topLeftClose.getY() + size, topLeftClose.getZ() + size);
        points[7] = new Vector3(topLeftClose.getX() + size, topLeftClose.getY(), topLeftClose.getZ() + size);
        for (int i = 0; i < 8; i++) {
            super.getPoints()[i] = points[i];
//            System.out.printf("%f, %f, %f\n", points[i].x, points[i].y, points[i].z);
        }
        createTriangles();
    }

    // left to right, then top, then bottom
    private void createTriangles() {
        triangles[0] = new Triangle(points[0], points[1], points[2]);
        triangles[1] = new Triangle(points[0], points[2], points[3]);
        triangles[2] = new Triangle(points[3], points[2], points[6]);
        triangles[3] = new Triangle(points[3], points[6], points[7]);
        triangles[4] = new Triangle(points[7], points[6], points[5]);
        triangles[5] = new Triangle(points[7], points[5], points[4]);
        triangles[6] = new Triangle(points[4], points[5], points[1]);
        triangles[7] = new Triangle(points[4], points[1], points[0]);
        triangles[8] = new Triangle(points[1], points[5], points[6]);
        triangles[9] = new Triangle(points[1], points[6], points[2]);
        triangles[10] = new Triangle(points[7], points[4], points[0]);
        triangles[11] = new Triangle(points[7], points[0], points[3]);
        for (int i = 0; i < 12; i++) {
            super.getTriangles()[i] = triangles[i];
        }//        for (int i = 0; i < 12; i++) {
//            System.out.printf("point1 (%f %f %f) ", triangles[i].point1.x, triangles[i].point1.y, triangles[i].point1.z);
//            System.out.printf("point2 (%f %f %f) ", triangles[i].point2.x, triangles[i].point2.y, triangles[i].point2.z);
//            System.out.printf("point3 (%f %f %f)", triangles[i].point3.x, triangles[i].point3.y, triangles[i].point3.z);
//            System.out.printf("\n");
//        }
    }

}
