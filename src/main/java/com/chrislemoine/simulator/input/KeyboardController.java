package com.chrislemoine.simulator.input;

import com.chrislemoine.simulator.core.SimBot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * Keyboard-based DriveController mapping:
 * <ul>
 *   <li>W/S → axial (forward/back): +1 = forward, -1 = reverse</li>
 *   <li>A/D → lateral (strafe right/left): +1 = right, -1 = left</li>
 *   <li>Q/E → yaw (rotation): +1 = counterclockwise, -1 = clockwise</li>
 * </ul>
 * Produces analog-like [-1,1] inputs with natural braking when released.
 */
public class KeyboardController extends KeyAdapter implements DriveController {
    private final SimBot robot;
    private final Map<Key, Boolean> keyStates = new EnumMap<>(Key.class);

    // Raw axis values [-1, 1]
    private double rawAxial = 0.0;
    private double rawLateral = 0.0;
    private double rawYaw = 0.0;

    /** Logical keys mapped to movement axes */
    private enum Key { FORWARD, BACKWARD, LEFT, RIGHT, ROTATE_CCW, ROTATE_CW }

    /**
     * @param robot the SimBot to control
     */
    public KeyboardController(SimBot robot) {
        this.robot = robot;
        // Initialize all key states to false
        for (Key key : Key.values()) {
            keyStates.put(key, false);
        }
    }

    /**
     * Called when a key is pressed; updates internal state
     */
    @Override
    public void keyPressed(KeyEvent e) {
        mapKey(e.getKeyCode(), true);
    }

    /**
     * Called when a key is released; updates internal state
     */
    @Override
    public void keyReleased(KeyEvent e) {
        mapKey(e.getKeyCode(), false);
    }

    /**
     * Maps AWT key codes to our logical Key enum
     */
    private void mapKey(int keyCode, boolean pressed) {
        switch (keyCode) {
            case KeyEvent.VK_W: keyStates.put(Key.FORWARD, pressed); break;
            case KeyEvent.VK_S: keyStates.put(Key.BACKWARD, pressed); break;
            case KeyEvent.VK_A: keyStates.put(Key.LEFT, pressed); break;
            case KeyEvent.VK_D: keyStates.put(Key.RIGHT, pressed); break;
            case KeyEvent.VK_Q: keyStates.put(Key.ROTATE_CCW, pressed); break;
            case KeyEvent.VK_E: keyStates.put(Key.ROTATE_CW, pressed); break;
            default: break;
        }
    }

    /**
     * Poll this each frame to update raw axis values based on key states
     */
    @Override
    public void poll() {
        rawAxial   = (keyStates.get(Key.FORWARD)  ?  1 : 0)
                + (keyStates.get(Key.BACKWARD) ? -1 : 0);
        rawLateral = (keyStates.get(Key.RIGHT)    ?  1 : 0)
                + (keyStates.get(Key.LEFT)     ? -1 : 0);
        rawYaw     = (keyStates.get(Key.ROTATE_CCW)?  1 : 0)
                + (keyStates.get(Key.ROTATE_CW)  ? -1 : 0);
    }

    /** @return forward/back input: +1 = forward, -1 = reverse */
    @Override
    public double getAxial()  { return rawAxial; }

    /** @return strafe input: +1 = right, -1 = left */
    @Override
    public double getLateral(){ return rawLateral; }

    /** @return yaw input: +1 = CCW, -1 = CW */
    @Override
    public double getYaw()    { return rawYaw; }
}