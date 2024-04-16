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
    private final Set<Integer> pressedKeys;

    public KeyJFrame(int width, int height) {
        this.setSize(width, height);
        this.getContentPane().setBackground(Color.GRAY);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.setVisible(true);
//        this.setLayout(null); This line breaks LinesComponent
        pressedKeys = new HashSet<>();
    }
    
    public Set<Integer> getPressedKeys() {
    	return pressedKeys;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) { pressedKeys.add(e.getKeyCode()); }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
}
