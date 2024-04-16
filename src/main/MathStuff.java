package main;

import java.awt.Color;

import Containers.*;

public class MathStuff {
	
	
	public MathStuff() {
		
	}
	// MATRIX MATH
    public static Vector3 multiplyMatrix(Vector3 i, Mat4x4 mat) {
        Vector3 o = new Vector3();
        float[][] matrix = mat.getMatrix();
//        System.out.println("MULTIPLY MATRIX");
//        System.out.printf("i[0] = [%f %f %f]\n", i.x, i.y, i.z);
//        System.out.printf("mat[0] = [%f %f %f % f]\n", mat.m[0][0], mat.m[1][0], mat.m[2][0], mat.m[3][0]);
//        System.out.printf("mat[1] = [%f %f %f % f]\n", mat.m[0][1], mat.m[1][1], mat.m[2][1], mat.m[3][1]);
//        System.out.printf("mat[2] = [%f %f %f % f]\n", mat.m[0][2], mat.m[1][2], mat.m[2][2], mat.m[3][2]);
//        System.out.printf("mat[3] = [%f %f %f % f]\n", mat.m[0][3], mat.m[1][3], mat.m[2][3], mat.m[3][3]);
        o.setX(i.getX() * matrix[0][0] + i.getY() * matrix[1][0] + i.getZ() * matrix[2][0] + matrix[3][0]);
        o.setY(i.getX() * matrix[0][1] + i.getY() * matrix[1][1] + i.getZ() * matrix[2][1] + matrix[3][1]);
        o.setZ(i.getX() * matrix[0][2] + i.getY() * matrix[1][2] + i.getZ() * matrix[2][2] + matrix[3][2]);
        o.setW(i.getX() * matrix[0][3] + i.getY() * matrix[1][3] + i.getZ() * matrix[2][3] + matrix[3][3]);

        if (o.getW() != 0.0f) {
            o.divX(o.getW());
            o.divY(o.getW());
            o.divZ(o.getW());
        }
//        System.out.printf("RESULT = [%f %f %f]\n", o.x, o.y, o.z);
        return new Vector3(o.getX(), o.getY(), o.getZ());
    }
    
    public static Mat4x4 multiplyMatrix(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 o = new Mat4x4();
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                o.getMatrix()[r][c] = m1.getMatrix()[r][0] * m2.getMatrix()[0][c] + m1.getMatrix()[r][1] * m2.getMatrix()[1][c]
                		+ m1.getMatrix()[r][2] * m2.getMatrix()[2][c] + m1.getMatrix()[r][3] * m2.getMatrix()[3][c];
            }
        }
        return o;
    }
    
    public static Mat4x4 matrix_pointAt(Vector3 pos, Vector3 target, Vector3 up) {
        // Calculate new forward direction
        Vector3 newForward = sub(target, pos);
        newForward = normalize(newForward);

        // Calculate new up direction
        Vector3 a = mul(newForward, dotProduct(up, newForward));
        Vector3 newUp = sub(up, a);
        newUp = normalize(newUp);

        // New Right direction, cross product
        Vector3 newRight = crossProduct(newUp, newForward);

        // Construct Dimensioning and Translation Matrix
        Mat4x4 matrix = new Mat4x4();
        matrix.getMatrix()[0][0] = newRight.getX();		matrix.getMatrix()[0][1] = newRight.getY();		matrix.getMatrix()[0][2] = newRight.getZ();		matrix.getMatrix()[0][3] = 0.0f;
        matrix.getMatrix()[1][0] = newUp.getX();		matrix.getMatrix()[1][1] = newUp.getY();		matrix.getMatrix()[1][2] = newUp.getZ();		matrix.getMatrix()[1][3] = 0.0f;
        matrix.getMatrix()[2][0] = newForward.getX();	matrix.getMatrix()[2][1] = newForward.getY();	matrix.getMatrix()[2][2] = newForward.getZ();	matrix.getMatrix()[2][3] = 0.0f;
        matrix.getMatrix()[3][0] = pos.getX();			matrix.getMatrix()[3][1] = pos.getY();			matrix.getMatrix()[3][2] = pos.getZ();			matrix.getMatrix()[3][3] = 1.0f;
        return matrix;
    }

    public static Mat4x4 quickInverse(Mat4x4 m1) { // only works for rotation/translation matrices
        Mat4x4 matrix = new Mat4x4();
        matrix.getMatrix()[0][0] = m1.getMatrix()[0][0]; matrix.getMatrix()[0][1] = m1.getMatrix()[1][0]; matrix.getMatrix()[0][2] = m1.getMatrix()[2][0]; matrix.getMatrix()[0][3] = 0.0f;
        matrix.getMatrix()[1][0] = m1.getMatrix()[0][1]; matrix.getMatrix()[1][1] = m1.getMatrix()[1][1]; matrix.getMatrix()[1][2] = m1.getMatrix()[2][1]; matrix.getMatrix()[1][3] = 0.0f;
        matrix.getMatrix()[2][0] = m1.getMatrix()[0][2]; matrix.getMatrix()[2][1] = m1.getMatrix()[1][2]; matrix.getMatrix()[2][2] = m1.getMatrix()[2][2]; matrix.getMatrix()[2][3] = 0.0f;
        matrix.getMatrix()[3][0] = -(m1.getMatrix()[3][0] * matrix.getMatrix()[0][0] + m1.getMatrix()[3][1] * matrix.getMatrix()[1][0] + m1.getMatrix()[3][2] * matrix.getMatrix()[2][0]);
        matrix.getMatrix()[3][1] = -(m1.getMatrix()[3][0] * matrix.getMatrix()[0][1] + m1.getMatrix()[3][1] * matrix.getMatrix()[1][1] + m1.getMatrix()[3][2] * matrix.getMatrix()[2][1]);
        matrix.getMatrix()[3][2] = -(m1.getMatrix()[3][0] * matrix.getMatrix()[0][2] + m1.getMatrix()[3][1] * matrix.getMatrix()[1][2] + m1.getMatrix()[3][2] * matrix.getMatrix()[2][2]);
        matrix.getMatrix()[3][3] = 1.0f;
        return matrix;
    }
    
    // Triangle math
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
    
    // Vector3 math
    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
    }
    public static Vector3 sub(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
    }
    public static Vector3 mul(Vector3 v1, float k) { return new Vector3(v1.getX() * k, v1.getY() * k, v1.getZ() * k); }
    public static float dotProduct(Vector3 v1, Vector3 v2) {
        return v1.getX()*v2.getX() + v1.getY()*v2.getY() + v1.getZ() * v2.getZ();
    }
    public static float dotProductSelf(Vector3 v1) { return dotProduct(v1, v1); }
    public static float Vector_Length(Vector3 v) {
        return (float) Math.sqrt(dotProduct(v, v));
    }
    public static Vector3 normalize(Vector3 v) {
        float l = Vector_Length(v);
        return new Vector3(v.getX() / l, v.getY() / l, v.getZ() / l);
    }
    public static Vector3 crossProduct(Vector3 v1, Vector3 v2) {
        Vector3 v = new Vector3();
        v.setX(v1.getY() * v2.getZ() - v1.getZ() * v2.getY());
        v.setY(v1.getZ() * v2.getX() - v1.getX() * v2.getZ());
        v.setZ(v1.getX() * v2.getY() - v1.getY() * v2.getX());
        return v;
    }
    // return where line intersects plane (use point on plane and normal of plane to define plane equation)
    public static Vector3 vectorIntersectPlane(Vector3 plane_p, Vector3 plane_n, Vector3 lineStart, Vector3 lineEnd) {
        plane_n.setVector3(normalize(plane_n));
        float plane_d = -dotProduct(plane_n, plane_p);
        float ad = dotProduct(lineStart, plane_n);
        float bd = dotProduct(lineEnd, plane_n);
        float t = (-(plane_d) - ad) / (bd - ad);
        Vector3 lineStartToEnd = sub(lineEnd, lineStart);
        Vector3 lineToIntersect = mul(lineStartToEnd, t);
        return add(lineStart, lineToIntersect);
    }
}
