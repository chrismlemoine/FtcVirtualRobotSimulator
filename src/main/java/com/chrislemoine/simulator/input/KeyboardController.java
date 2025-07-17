package com.chrislemoine.simulator.input;

import com.chrislemoine.simulator.core.SimBot;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * Keyboard-based controller mapping WASD for translation and QE for rotation.
 */
public class KeyboardController extends KeyAdapter implements DriveController {
    private final SimBot robot;

    // Track key status
    private final Map<Key, Boolean> keys = new EnumMap<>(Key.class);

    // Raw inputs in [-1, 1]
    private double rawAxial = 0, rawLateral = 0, rawYaw = 0;

    public enum Key { FORWARD, BACKWARD, LEFT, RIGHT, CLOCKWISE, COUNTERCLOCKWISE }

    public KeyboardController(SimBot robot) {
        this.robot = robot;
        for (Key k : Key.values()) keys.put(k, false);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        mapKey(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        mapKey(e.getKeyCode(), false);
    }

    private void mapKey(int code, boolean pressed) {
        switch (code) {
            case KeyEvent.VK_W: keys.put(Key.FORWARD, pressed); break;
            case KeyEvent.VK_S: keys.put(Key.BACKWARD, pressed); break;
            case KeyEvent.VK_A: keys.put(Key.LEFT, pressed); break;
            case KeyEvent.VK_D: keys.put(Key.RIGHT, pressed); break;
            case KeyEvent.VK_Q: keys.put(Key.COUNTERCLOCKWISE, pressed); break;
            case KeyEvent.VK_E: keys.put(Key.CLOCKWISE, pressed); break;
        }
    }

    @Override
    public void poll() {
        rawAxial    = (keys.get(Key.FORWARD) ? 1 : 0) + (keys.get(Key.BACKWARD) ? -1 : 0);
        rawLateral  = (keys.get(Key.RIGHT) ? 1 : 0) + (keys.get(Key.LEFT) ? -1 : 0);
        rawYaw      = (keys.get(Key.COUNTERCLOCKWISE) ? 1 : 0) + (keys.get(Key.CLOCKWISE) ? -1 : 0);
    }

    @Override
    public double getAxial()  { return rawAxial; }
    @Override
    public double getLateral(){ return rawLateral; }
    @Override
    public double getYaw()    { return rawYaw; }
}
