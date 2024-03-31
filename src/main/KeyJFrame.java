// https://www.youtube.com/watch?v=BJ7fr9XwS2o
// https://stackoverflow.com/questions/2623995/swings-keylistener-and-multiple-keys-pressed-at-the-same-time
package main;

import Containers.Vector3;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import javax.swing.*;

public class KeyJFrame extends JFrame implements KeyListener{
    private UI ui;
    private final Set<Integer> pressedKeys;

    public KeyJFrame(UI ui, int width, int height) {
        this.ui = ui;
        this.setSize(width, height);
        this.getContentPane().setBackground(Color.GRAY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
//        this.setLayout(null); This line breaks LinesComponent
        pressedKeys = new HashSet<>();
        keyAction.start();
    }

    // using keyPressed function has an input delay, to fix use a hashset that stores flags whenever key is pressed
    // and a timer to remove input delay
    private Timer keyAction = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println(Arrays.toString(pressedKeys.toArray()));
            if (!pressedKeys.isEmpty()) {
//                System.out.println("--------------");
                for (int keyCode : pressedKeys) {
//                    System.out.println(keyCode);
                    Vector3 vForward = Vector3.mul(ui.getMainCamera().getLookDir(), 0.1f);
                    Vector3 vSide = Vector3.mul(ui.getMainCamera().getSideDir(), 0.1f);
                    Vector3 vUp = Vector3.mul(Vector3.crossProduct(vForward, vSide), 10.0f); // update this if want to add up/down looking

                    if (ui.getSpaceShip().isTriCollide()) {
                        vForward = Camera.projectToPlane(vForward, ui.getSpaceShip().getCollidingTri());
                        vSide = Camera.projectToPlane(vSide, ui.getSpaceShip().getCollidingTri());
                        vUp = Camera.projectToPlane(vUp, ui.getSpaceShip().getCollidingTri());
                    }
                    switch(keyCode) {
                        case KeyEvent.VK_W: ui.getMainCamera().getCamera().setVector3(Vector3.add(ui.getMainCamera().getCamera(), vForward)); break;
                        case KeyEvent.VK_S: ui.getMainCamera().getCamera().setVector3(Vector3.sub(ui.getMainCamera().getCamera(), vForward)); break;
                        case KeyEvent.VK_A: ui.getMainCamera().getCamera().setVector3(Vector3.add(ui.getMainCamera().getCamera(), vSide)); break;
                        case KeyEvent.VK_D: ui.getMainCamera().getCamera().setVector3(Vector3.sub(ui.getMainCamera().getCamera(), vSide)); break;
                        case KeyEvent.VK_LEFT: ui.getMainCamera().updateYaw(-0.05f); break;
                        case KeyEvent.VK_RIGHT: ui.getMainCamera().updateYaw(0.05f); break;
                        case KeyEvent.VK_UP: ui.getMainCamera().getCamera().setVector3(Vector3.add(ui.getMainCamera().getCamera(), vUp)); break;
                        case KeyEvent.VK_DOWN: ui.getMainCamera().getCamera().setVector3(Vector3.sub(ui.getMainCamera().getCamera(), vUp)); break;
                    }
                }
            }
        }
    });

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) { pressedKeys.add(e.getKeyCode()); }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
}
