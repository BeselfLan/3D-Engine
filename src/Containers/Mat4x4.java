package Containers;

import Containers.*;

public class Mat4x4 {

    private float[][] m = new float[4][4];
    private final float PI = 3.14159f;
    private final float fFar = 1000.0f;
    private final float fFov = 90.0f;
    private final float fNear = 0.1f;
    private final float fAspectRatio = (float) 500 / (float) 500;
    private final float fFovRad = 1.0f / (float) Math.tan(fFov * 0.5f / 180.0f * PI);

    public Mat4x4() {

    }

    public float[][] getMatrix() {
        return m;
    }

    public void setMatrix(Mat4x4 m1) {
        m = m1.getMatrix();
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
}
