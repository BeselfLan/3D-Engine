package main;

import Containers.*;
import MathStuff.Mat4x4;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UI {
    private final int WIDTH = 1000, HEIGHT = 1000;
    private JFrame window;
    private GameManager gm;
    private Obj spaceShip;
    private Camera vCamera;
    private LinesComponent lc;
    private float time;

    public Timer rotationTimer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent arg0) {
            time += 0.01;
            spaceShip.projectObject(vCamera, 0);
//            System.out.println(time);
        }
    });

    public UI(GameManager gm) {
        this.gm = gm;
        createMainField();
        spaceShip = new Cube(new Vector3(0, 1, 0), 1, lc);
//        spaceShip.getObj("res/mountains.obj");
//        spaceShip.projectObject(vCamera, fYaw,0.0f);
        rotationTimer.start();
        window.setVisible(true);
    }

    public void createMainField() {
        window = new KeyJFrame(this, WIDTH, HEIGHT);
        createLinesComponent(window);
        time = 0.0f;

        spaceShip = new Obj(lc);
        vCamera = new Camera();
    }
    public void createLinesComponent(JFrame window) {
        lc = new LinesComponent();
        lc.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        window.getContentPane().add(lc, BorderLayout.CENTER);
        lc.setVisible(true);
    }
    public Obj getSpaceShip() {
        return spaceShip;
    }
    public float getTime() {
        return time;
    }
    public Camera getMainCamera() {
        return vCamera;
    }
}
