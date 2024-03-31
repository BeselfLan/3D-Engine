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
    public static Vector3 add(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }
    public static Vector3 sub(Vector3 v1, Vector3 v2) {
        return new Vector3(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }
    public static Vector3 mul(Vector3 v1, float k) { return new Vector3(v1.x * k, v1.y * k, v1.z * k); }
    public static float dotProduct(Vector3 v1, Vector3 v2) {
        return v1.x*v2.x + v1.y*v2.y + v1.z * v2.z;
    }
    public static float dotProductSelf(Vector3 v1) { return dotProduct(v1, v1); }
    public static float Vector_Length(Vector3 v) {
        return (float) Math.sqrt(dotProduct(v, v));
    }
    public static Vector3 normalize(Vector3 v) {
        float l = Vector_Length(v);
        return new Vector3(v.x / l, v.y / l, v.z / l);
    }
    public static Vector3 crossProduct(Vector3 v1, Vector3 v2) {
        Vector3 v = new Vector3();
        v.x = v1.y * v2.z - v1.z * v2.y;
        v.y = v1.z * v2.x - v1.x * v2.z;
        v.z = v1.x * v2.y - v1.y * v2.x;
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
