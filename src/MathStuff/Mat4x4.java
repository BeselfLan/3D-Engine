package MathStuff;

import Containers.*;

public class Mat4x4 {

    private float[][] m = new float[4][4];

    public Mat4x4() {

    }

    public float[][] getMatrix() {
        return m;
    }

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
    public void setMatrix(Mat4x4 m1) {
        m = m1.getMatrix();
    }

    public static Mat4x4 multiplyMatrix(Mat4x4 m1, Mat4x4 m2) {
        Mat4x4 o = new Mat4x4();
        for (int c = 0; c < 4; c++) {
            for (int r = 0; r < 4; r++) {
                o.m[r][c] = m1.m[r][0] * m2.m[0][c] + m1.m[r][1] * m2.m[1][c] + m1.m[r][2] * m2.m[2][c] + m1.m[r][3] * m2.m[3][c];
            }
        }
        return o;
    }

    public void clear() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j <4; j++) {
                    m[i][j] = 0;
            }
        }
    }

    public void printMatrix() {
        System.out.println("----------------------");
        for (int i = 0; i < 4; i++) {
            System.out.printf("[");
            for (int j = 0; j <4; j++) {
                System.out.printf(" %.5f ", m[i][j]);
            }
            System.out.println("]");
        }
    }

    // this method slows everything down for some reason
//    public static Triangle multiplyMatrix(Triangle i, Mat4x4 mat) {
//        Vector3 empty = new Vector3(0, 0, 0);
//        Triangle o = new Triangle(empty, empty, empty);
//        o.point1 = multiplyMatrix(i.point1, mat);
//        o.point2 = multiplyMatrix(i.point2, mat);
//        o.point3 = multiplyMatrix(i.point3, mat);
//        return o;
//    }

    private final float PI = 3.14159f;
    private final float fFar = 1000.0f;
    private final float fFov = 90.0f;
    private final float fNear = 0.1f;
    private final float fAspectRatio = (float) 500 / (float) 500;
    private final float fFovRad = 1.0f / (float) Math.tan(fFov * 0.5f / 180.0f * PI);

    public void projection4x4() {
        clear();
        m[0][0] = fAspectRatio * fFovRad;
        m[1][1] = fFovRad;
        m[2][2] = fFar / (fFar - fNear);
        m[3][2] = (-fFar * fNear) / (fFar - fNear);
        m[2][3] = -1.0f; // changed from 1.0 to -1.0
        m[3][3] = 0.0f;
    }

    public void rotationZ4x4(float fTheta) {
        clear();
        m[0][0] = (float) Math.cos(fTheta);
        m[0][1] = (float) Math.sin(fTheta);
        m[1][0] = (float) -Math.sin(fTheta);
        m[1][1] = (float) Math.cos(fTheta);
        m[2][2] = 1.0f;
        m[3][3] = 1.0f;
    }
    public void rotationX4x4(float fTheta) {
        clear();
        m[0][0] = 1.0f;
        m[1][1] = (float) Math.cos(fTheta * 0.5f);
        m[1][2] = (float) Math.sin(fTheta * 0.5f);
        m[2][1] = (float) -Math.sin(fTheta * 0.5f);
        m[2][2] = (float) Math.cos(fTheta * 0.5f);
        m[3][3] = 1.0f;
    }
    public void rotationY4x4(float fTheta) {
        clear();
        m[0][0] = (float) Math.cos(fTheta);
        m[0][2] = (float) Math.sin(fTheta);
        m[2][0] = (float) -Math.sin(fTheta);
        m[1][1] = 1.0f;
        m[2][2] = (float) Math.cos(fTheta);
        m[3][3] = 1.0f;
    }
    public void identity4x4(float fTheta) {
        clear();
        m[0][0] = 1.0f;
        m[1][1] = 1.0f;
        m[2][2] = 1.0f;
        m[3][3] = 1.0f;
    }

    public static Mat4x4 matrix_pointAt(Vector3 pos, Vector3 target, Vector3 up) {
        // Calculate new forward direction
        Vector3 newForward = Vector3.sub(target, pos);
        newForward =Vector3.normalize(newForward);

        // Calculate new up direction
        Vector3 a = Vector3.mul(newForward, Vector3.dotProduct(up, newForward));
        Vector3 newUp = Vector3.sub(up, a);
        newUp = Vector3.normalize(newUp);

        // New Right direction, cross product
        Vector3 newRight = Vector3.crossProduct(newUp, newForward);

        // Construct Dimensioning and Translation Matrix
        Mat4x4 matrix = new Mat4x4();
        matrix.m[0][0] = newRight.getX();	matrix.m[0][1] = newRight.getY();	matrix.m[0][2] = newRight.getZ();	matrix.m[0][3] = 0.0f;
        matrix.m[1][0] = newUp.getX();		matrix.m[1][1] = newUp.getY();		matrix.m[1][2] = newUp.getZ();		matrix.m[1][3] = 0.0f;
        matrix.m[2][0] = newForward.getX();	matrix.m[2][1] = newForward.getY();	matrix.m[2][2] = newForward.getZ();	matrix.m[2][3] = 0.0f;
        matrix.m[3][0] = pos.getX();		matrix.m[3][1] = pos.getY();		matrix.m[3][2] = pos.getZ();		matrix.m[3][3] = 1.0f;
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
}
