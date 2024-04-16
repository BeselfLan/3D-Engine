package Containers;

import main.Obj;
// https://www.youtube.com/watch?v=chScv-vaXPo

public class Vector3 {
    private float x, y, z, w; // w value only for conventional 4x4 matrix multiplication

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        w = 1.0f;
    }
    public Vector3() {
        x = .0f;
        y = .0f;
        z = .0f;
        w = 1.0f;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getZ() {
        return z;
    }
    public float getW() { return w; }

    public void setX(float newVal) {
        x = newVal;
    }
    public void setY(float newVal) {
        y = newVal;
    }
    public void setZ(float newVal) {
        z = newVal;
    }
    public void setW(float newVal) {
        w = newVal;
    }

    public void addX(float val) {
        x += val;
    }
    public void addY(float val) {
        y += val;
    }
    public void addZ(float val) {
        z += val;
    }

    public void mulX(float val) {
        x *= val;
    }
    public void mulY(float val) {
        y *= val;
    }
    public void mulZ(float val) {
        z *= val;
    }

    public void divX(float val) {
        x /= val;
    }
    public void divY(float val) {
        y /= val;
    }
    public void divZ(float val) {
        z /= val;
    }

    public void setVector3(Vector3 v1) {
        setX(v1.getX());
        setY(v1.getY());
        setZ(v1.getZ());
    }
}
