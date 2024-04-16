package main;

import Containers.Mat4x4;
import Containers.Triangle;
import Containers.Vector3;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Obj {

    private final int SWIDTH = 1000;
    private final int SHEIGHT = 1000;

    private Vector3[] points = new Vector3[10000];
    private Triangle[] triangles = new Triangle[10000];
    private LinesComponent lc;
    private LinkedList<Triangle> toRender;
    private Mat4x4 pm, rzm, rxm, rym; // projection matrix, z axis rotation matrix, and x axis rotation
    private boolean isTriCollide;
    private Triangle collidingTri;

    public Obj(LinesComponent lc) {
        toRender = new LinkedList<Triangle> ();
        this.lc = lc;

        pm = new Mat4x4();
        rzm = new Mat4x4();
        rxm = new Mat4x4();
        rym = new Mat4x4();
        pm.projection4x4();
        rzm.rotationZ4x4(0.0f);
        rxm.rotationX4x4(0.0f);
        rym.rotationY4x4(0.0f);

        collidingTri = new Triangle();
        isTriCollide = false;
    }

    public boolean isTriCollide() { return isTriCollide; }
    public Triangle getCollidingTri() { return collidingTri; }
    public Vector3[] getPoints() { return points; }
    public Triangle[] getTriangles() {
        return triangles;
    }
    public void getObj(String path) {
        char type;
        int point1, point2, point3, vLen = 1, fLen = 0;
        try {
            Scanner sc = new Scanner(new File(path));

            while(sc.hasNext()) {
                String[] line = sc.nextLine().split("\\s+");
                if (line.length == 0) {
                    continue;
                }
                type = line[0].charAt(0);
                if (type == 'v') {
                    points[vLen] = new Vector3(Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]));
//                    System.out.printf("Adding (%f %f %f) to points (%d)\n", Float.parseFloat(line[1]), Float.parseFloat(line[2]), Float.parseFloat(line[3]), vLen);
                    vLen ++;
                }
                else if (type == 'f') {
                    point1 = Integer.parseInt(line[1]);
                    point2 = Integer.parseInt(line[2]);
                    point3 = Integer.parseInt(line[3]);
                    triangles[fLen] = new Triangle(points[point1], points[point2], points[point3]);
//                    System.out.printf("Adding points (%d %d %d) to triangles (%d)\n", point1, point2, point3, fLen);
                    fLen ++;
                }
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Missing Resource!\n");
        }
    }

    public void projectObject(Camera vCamera, float fTheta) {
        boolean collisionCycle = false;
        toRender.clear();
        lc.clearLines();

        vCamera.updateCameraPointAt();

        for (Triangle tri : getTriangles()) {
            if (tri == null) {
                break;
            }
            Triangle triRotatedX = new Triangle(), triRotatedZ = new Triangle(), triProjected = new Triangle(), triTranslated = new Triangle(), triViewed = new Triangle();

            // ROTATE cube z axis
            triRotatedZ.copy(tri);
            rotateTriangle(triRotatedZ, fTheta, 'z', rzm);
            // ROTATE cube x axis
            triRotatedX.copy(triRotatedZ);
            rotateTriangle(triRotatedX, fTheta, 'x', rxm);
            //TRANSLATE obj into viewing area
            triTranslated.copy(triRotatedX);
            triTranslated.translateZ(8.0f);

            // Calculate normal of each triangle to determine if we should render them
            Vector3 normal = MathStuff.findNormal(triTranslated);
            // if (normal.z < 0) {
            // find if normal is visible relative to camera (dot product)
            if (MathStuff.isTriangleVisible(normal, triTranslated, vCamera.getCamera())) {

                // Illumination
                Vector3 light_direction = vCamera.getLookDir();
//                Vector3 light_direction = new Vector3(0.0f, 0.0f, -1.0f);
                MathStuff.illuminateTriangle(light_direction, normal, triViewed);

                if (collisionCheck(vCamera.getCamera(), triTranslated, 0.1f)) {
                    collisionCycle = true;
//                    triViewed.setColor(Color.RED);
                }

                // Convert world space into view space
                triViewed.setP1(MathStuff.multiplyMatrix(triTranslated.getP1(), vCamera.getMatView()));
                triViewed.setP2(MathStuff.multiplyMatrix(triTranslated.getP2(), vCamera.getMatView()));
                triViewed.setP3(MathStuff.multiplyMatrix(triTranslated.getP3(), vCamera.getMatView()));

                // clip viewed triangle against near plane, this can form an additional 2 triangles
                Triangle[] clipped = new Triangle[2];
                clipped[0] = new Triangle(); clipped[1] = new Triangle();
                int nClippedTriangles = MathStuff.triangleClipAgainstPlane(new Vector3(0.0f, 0.0f, 0.1f), new Vector3(0.0f, 0.0f, 1.0f), triViewed, clipped[0], clipped[1]);
//                System.out.printf("%d\n", nClippedTriangles);
                for (int i = 0; i < nClippedTriangles; i++) {
//                    clipped[n].printTriangle();
                    // Project triangle from 3D to 2D (screen)
                    projectTriangle(clipped[i]);

                    // Scale into view
                    clipped[i].translateX(1.0f);
                    clipped[i].translateY(1.0f);

                    clipped[i].scaleBy2D(0.5f * 1000.0f);

//                    if (clipped[i].getColor().toString().equals("java.awt.Color[r=255,g=0,b=0]") || clipped[i].getColor().toString().equals("java.awt.Color[r=0,g=255,b=0]") || clipped[i].getColor().toString().equals("java.awt.Color[r=0,g=0,b=255]")) {
//                        System.out.println(clipped[i].getColor().toString());
//                    }

                    toRender.add(clipped[i]);
                    // FOR DEBUGGING, SHOWS TRIANGLE OUTLINES
//                    lc.addLine((int)triProjected.getP1().getX(), (int)triProjected.getP1().getY(), (int)triProjected.getP2().getX(), (int)triProjected.getP2().getY());
//                    lc.addLine((int)triProjected.getP2().getX(), (int)triProjected.getP2().getY(), (int)triProjected.getP3().getX(), (int)triProjected.getP3().getY());
//                    lc.addLine((int)triProjected.getP3().getX(), (int)triProjected.getP3().getY(), (int)triProjected.getP1().getX(), (int)triProjected.getP1().getY());
                }
            }
        }
        isTriCollide = collisionCycle;

        toRender.sort(new Comparator<Triangle>() {
            @Override
            public int compare(Triangle t1, Triangle t2) {
                float z1 = (t1.getP1().getZ() + t1.getP2().getZ() + t1.getP3().getZ()) / 3.0f;
                float z2 = (t2.getP1().getZ() + t2.getP2().getZ() + t2.getP3().getZ()) / 3.0f;
                return Float.compare(z1, z2);
            }
        });
        for (Triangle tri : toRender) {

            Triangle[] clipped = new Triangle[2];
            LinkedList<Triangle> listTriangles = new LinkedList<>();
            listTriangles.add(tri);
            int nNewTriangles = 1;

            for (int p = 0; p < 4; p++) { // check 4 planes to see if triangle clips camera boundaries
                int nTrisToAdd = 0;
                while (nNewTriangles > 0) {
                    Triangle test = listTriangles.pop();
                    nNewTriangles--;
                    clipped[0] = new Triangle();
                    clipped[1] = new Triangle();

                    switch(p) {
                        case 0: nTrisToAdd = MathStuff.triangleClipAgainstPlane(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(0.0f, 1.0f, 0.0f), test, clipped[0], clipped[1]); break;
                        case 1: nTrisToAdd = MathStuff.triangleClipAgainstPlane(new Vector3(0.0f, (float)SHEIGHT-1, 0.0f), new Vector3(0.0f, -1.0f, 0.0f), test, clipped[0], clipped[1]); break;
                        case 2: nTrisToAdd = MathStuff.triangleClipAgainstPlane(new Vector3(0.0f, 0.0f, 0.0f), new Vector3(1.0f, 0.0f, 0.0f), test, clipped[0], clipped[1]); break;
                        case 3: nTrisToAdd = MathStuff.triangleClipAgainstPlane(new Vector3((float)SWIDTH-1, 0.0f, 0.0f), new Vector3(-1.0f, 0.0f, 0.0f), test, clipped[0], clipped[1]); break;

                    }
                    for (int w = 0; w < nTrisToAdd; w++) {
                        listTriangles.push(clipped[w]);
                    }
                }
                nNewTriangles = listTriangles.size();
            }
            for (Triangle t : listTriangles) {
//                lc.addLine((int)t.getP1().getX(), (int)t.getP1().getY(), (int)t.getP2().getX(), (int)t.getP2().getY());
//                lc.addLine((int)t.getP2().getX(), (int)t.getP2().getY(), (int)t.getP3().getX(), (int)t.getP3().getY());
//                lc.addLine((int)t.getP3().getX(), (int)t.getP3().getY(), (int)t.getP1().getX(), (int)t.getP1().getY());
            	MathStuff.drawTriangles(t, lc);
            }
        }
    }
    // function is pass by reference
    public void rotateTriangle(Triangle tri, float fTheta, char axis, Mat4x4 rotationMatrix) {
        switch(axis) {
            case 'x': rotationMatrix.rotationX4x4(fTheta); break;
            case 'y': rotationMatrix.rotationY4x4(fTheta); break;
            case 'z': rotationMatrix.rotationZ4x4(fTheta); break;
            default : System.out.printf("Given rotation axis '%c' doesn't exist!", axis); break;
        }
        tri.setP1(MathStuff.multiplyMatrix(tri.getP1(), rotationMatrix));
        tri.setP2(MathStuff.multiplyMatrix(tri.getP2(), rotationMatrix));
        tri.setP3(MathStuff.multiplyMatrix(tri.getP3(), rotationMatrix));
    }

    public void projectTriangle(Triangle tri) {
        tri.setP1(MathStuff.multiplyMatrix(tri.getP1(), pm));
        tri.setP2(MathStuff.multiplyMatrix(tri.getP2(), pm));
        tri.setP3(MathStuff.multiplyMatrix(tri.getP3(), pm));
    }

    public boolean collisionCheck(Vector3 point, Triangle tri, float thresh) {
//        System.out.printf("%f %f\n", distToTriangle(point, tri.getP1(), tri.getP2(), tri.getP3()), thresh);
        if (distToTriangle(point, tri.getP1(), tri.getP2(), tri.getP3()) <= thresh) {
            collidingTri.copy(tri);
            return true;
        }
        return false;
    }
    private static float distToTriangle(Vector3 point, Vector3 a, Vector3 b, Vector3 c) {
        Vector3 ba = MathStuff.sub(b, a);
        Vector3 pa = MathStuff.sub(point, a);
        Vector3 cb = MathStuff.sub(c, b);
        Vector3 pb = MathStuff.sub(point, b);
        Vector3 ac = MathStuff.sub(a, c);
        Vector3 pc = MathStuff.sub(point, c);
        Vector3 nor = MathStuff.crossProduct(ba, ac);

        if (Math.signum(MathStuff.dotProduct(MathStuff.crossProduct(ba, nor), pa))
                + Math.signum(MathStuff.dotProduct(MathStuff.crossProduct(cb, nor), pb))
                + Math.signum(MathStuff.dotProduct(MathStuff.crossProduct(ac, nor), pc)) < 2.0f) {
            return Math.min(Math.min(MathStuff.dotProductSelf(MathStuff.sub(MathStuff.mul(ba, constrain(MathStuff.dotProduct(ba, pa) / MathStuff.dotProductSelf(ba), 0.0f, 1.0f)), pa)),
            		MathStuff.dotProductSelf(MathStuff.sub(MathStuff.mul(cb, constrain(MathStuff.dotProduct(cb, pb) / MathStuff.dotProductSelf(cb), 0.0f, 1.0f)), pb))),
            		MathStuff.dotProductSelf(MathStuff.sub(MathStuff.mul(ac, constrain(MathStuff.dotProduct(ac, pc) / MathStuff.dotProductSelf(ac), 0.0f, 1.0f)), pc)));
        }
        return MathStuff.dotProduct(nor, pa) * MathStuff.dotProduct(nor, pa)/MathStuff.dotProductSelf(nor);
    }
    private static float constrain(float n, float upper, float lower) {
        return Math.max(lower, Math.min(upper, n));
    }
}
