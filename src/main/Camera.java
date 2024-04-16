package main;

import Containers.Mat4x4;
import Containers.Triangle;
import Containers.Vector3;

public class Camera {

    private Vector3 camera, vTarget, vLookDir, vUp, vSideDir;
    private Mat4x4 matView, matCamera, matCameraRot;
    private float fYaw;
    private final float PI = 3.14159f;

    public Camera() {
        camera = new Vector3();
        fYaw = 0.0f;
        vLookDir = new Vector3(0, 0, 1);
        vSideDir = new Vector3(1, 0, 0);
        vUp = new Vector3(0, 1, 0);
        vTarget = new Vector3(0, 0, 1);
        matCameraRot = new Mat4x4();
        matView = new Mat4x4();
        matCamera = new Mat4x4();
    }

    public Vector3 getCamera() {
        return camera;
    }
    public Vector3 getTarget() {
        return vTarget;
    }
    public Vector3 getLookDir() {
        return vLookDir;
    }
    public Vector3 getSideDir() { return vSideDir; }
    public Vector3 getUp() {
        return vUp;
    }
    public Mat4x4 getMatView() {
        return matView;
    }
    public Mat4x4 getMatCamera() {
        return matCamera;
    }
    public Mat4x4 getMatCameraRot() {
        return matCameraRot;
    }
    public float getYaw() {
        return fYaw;
    }
    public void moveCameraY(float change) {
        camera.addY(change);
    }
    public void updateYaw(float change) {
        fYaw += change;
    }
    public void updateCameraPointAt() {
        vUp.setVector3(new Vector3(0, 1, 0));
        vTarget.setVector3(new Vector3(0, 0, 1));
        matCameraRot.rotationY4x4(fYaw); // rotation matrix to rotate camera around y axis
        vLookDir.setVector3(MathStuff.multiplyMatrix(vTarget, matCameraRot)); // unit vector rotated around orgin using rotation matrix

        matCameraRot.rotationY4x4(fYaw - PI/2);
        vSideDir.setVector3(MathStuff.multiplyMatrix(vTarget, matCameraRot));

//        System.out.printf("%f %f %f\n", vLookDir.getX(), vLookDir.getY(), vLookDir.getZ());
        vTarget.setVector3(MathStuff.add(camera, vLookDir)); // offset camera rotation to current camera location
        matCamera.setMatrix(MathStuff.matrix_pointAt(camera, vTarget, vUp));
        // make view matrix from camera
        matView.setMatrix(MathStuff.quickInverse(matCamera));
//        matView.printMatrix();
    }

    public static Vector3 projectToPlane(Vector3 v, Triangle tri) {
        Vector3 n = MathStuff.findNormal(tri);
        Vector3 vProjN = MathStuff.mul(n, (float) (MathStuff.dotProduct(v, n) / Math.pow(MathStuff.dotProductSelf(n), 2.0)));
        return MathStuff.sub(v, vProjN);
    }
}
