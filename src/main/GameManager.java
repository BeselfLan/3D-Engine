package main;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.Timer;

import Containers.*;

public class GameManager { // collision/clipping system a bit wonky, you can clip through concave corners
    private final int WIDTH = 1000, HEIGHT = 1000;
    private KeyJFrame window;
    private LinkedList<Obj> objects;
    private Camera vCamera;
    private LinesComponent lc;
    private float time;

    public GameManager() {
        createMainField();
        objects = new LinkedList<>();
        objects.add(new Cube(new Vector3(0, 1, 0), 1, lc));
        objects.add(new Cube(new Vector3(0, 2, 0), 1, lc));
        objects.add(new Cube(new Vector3(1, 2, 0), 1, lc));

//        spaceShip.getObj("res/VideoShip.obj");
//        spaceShip.projectObject(vCamera, fYaw,0.0f);
        gameTimer.start();
        window.setVisible(true);
    }
    
    
    public Timer gameTimer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            time += 0.01;
            Obj.clearRender();
            lc.clearLines();
            for(Obj obj : objects) {
                obj.projectObject(vCamera, objects, 0);
                // using keyPressed function has an input delay, to fix use a hashset that stores flags whenever key is pressed
                // and a timer to remove input delay
//          System.out.println(Arrays.toString(pressedKeys.toArray()));
                if (!window.getPressedKeys().isEmpty()) {
//              System.out.println("--------------");
                    for (int keyCode : window.getPressedKeys()) {
//                  System.out.println(keyCode);
                        Vector3 vForward = MathStuff.mul(getMainCamera().getLookDir(), 0.1f);
                        Vector3 vSide = MathStuff.mul(getMainCamera().getSideDir(), 0.1f);
                        Vector3 vUp = MathStuff.mul(MathStuff.crossProduct(vForward, vSide), 10.0f); // update this if want to add up/down looking

                        if (obj.isTriCollide()) {
                            vForward = Camera.projectToPlane(vForward, Obj.getCollidingTri());
                            vSide = Camera.projectToPlane(vSide, Obj.getCollidingTri());
                            vUp = Camera.projectToPlane(vUp, Obj.getCollidingTri());
                        }
                        switch(keyCode) {
                            case KeyEvent.VK_W: getMainCamera().getCamera().setVector3(MathStuff.add(getMainCamera().getCamera(), vForward)); break;
                            case KeyEvent.VK_S: getMainCamera().getCamera().setVector3(MathStuff.sub(getMainCamera().getCamera(), vForward)); break;
                            case KeyEvent.VK_A: getMainCamera().getCamera().setVector3(MathStuff.add(getMainCamera().getCamera(), vSide)); break;
                            case KeyEvent.VK_D: getMainCamera().getCamera().setVector3(MathStuff.sub(getMainCamera().getCamera(), vSide)); break;
                            case KeyEvent.VK_LEFT: getMainCamera().updateYaw(-0.02f); break;
                            case KeyEvent.VK_RIGHT: getMainCamera().updateYaw(0.02f); break;
                            case KeyEvent.VK_UP: getMainCamera().getCamera().setVector3(MathStuff.add(getMainCamera().getCamera(), vUp)); break;
                            case KeyEvent.VK_DOWN: getMainCamera().getCamera().setVector3(MathStuff.sub(getMainCamera().getCamera(), vUp)); break;
                        }
                    }
                }
            }
        }
//            System.out.println(time);
    });
    
    
    public void createMainField() {
        window = new KeyJFrame(WIDTH, HEIGHT);
        createLinesComponent(window);
        time = 0.0f;

        vCamera = new Camera();
    }
    public void createLinesComponent(JFrame window) {
        lc = new LinesComponent();
        lc.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        window.getContentPane().add(lc, BorderLayout.CENTER);
        lc.setVisible(true);
    }
    public float getTime() {
        return time;
    }
    public Camera getMainCamera() {
        return vCamera;
    }
    
    public static void main(String[] args) {
        new GameManager();
    }
}
