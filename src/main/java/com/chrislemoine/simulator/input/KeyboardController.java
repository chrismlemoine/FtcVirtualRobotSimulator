package com.chrislemoine.simulator.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.EnumMap;
import java.util.Map;

/**
 * DriveController that maps keyboard keys to control axes.
 * <p>
 *   W/S → axial (forward/back): +1 = forward, -1 = reverse<br>
 *   A/D → lateral (strafe right/left): +1 = right, -1 = left<br>
 *   Q/E → yaw (rotation): +1 = counterclockwise, -1 = clockwise
 * </p>
 * <p>
 * Instantaneous braking: when keys are released, that axis input snaps to zero.
 * </p>
 */
public class KeyboardController extends KeyAdapter implements DriveController {
    // Map AWT key codes to logical actions
    private static final Map<Integer, KeyAction> KEY_CODE_MAP = Map.of(
            KeyEvent.VK_W, KeyAction.FORWARD,
            KeyEvent.VK_S, KeyAction.BACKWARD,
            KeyEvent.VK_A, KeyAction.LEFT,
            KeyEvent.VK_D, KeyAction.RIGHT,
            KeyEvent.VK_Q, KeyAction.ROTATE_CCW,
            KeyEvent.VK_E, KeyAction.ROTATE_CW
    );

    // Tracks which actions are currently pressed
    private final Map<KeyAction, Boolean> keyPressed = new EnumMap<>(KeyAction.class);

    // Current raw axis inputs in [-1, 1]
    private double axialInput = 0.0;
    private double lateralInput = 0.0;
    private double yawInput = 0.0;

    /**
     * Logical actions corresponding to movement axes.
     */
    private enum KeyAction {
        FORWARD, BACKWARD,
        LEFT, RIGHT,
        ROTATE_CCW, ROTATE_CW
    }

    /**
     * Constructs a KeyboardController, initializing all keys to unpressed.
     */
    public KeyboardController() {
        for (KeyAction action : KeyAction.values()) {
            keyPressed.put(action, false);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        KeyAction action = KEY_CODE_MAP.get(e.getKeyCode());
        if (action != null) {
            keyPressed.put(action, true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        KeyAction action = KEY_CODE_MAP.get(e.getKeyCode());
        if (action != null) {
            keyPressed.put(action, false);
        }
    }

    /**
     * Updates the axis inputs based on current key states.
     * Call once per simulation tick before querying getAxial(), etc.
     */
    @Override
    public void poll() {
        axialInput = (keyPressed.get(KeyAction.FORWARD)  ?  1 : 0)
                + (keyPressed.get(KeyAction.BACKWARD) ? -1 : 0);
        lateralInput = (keyPressed.get(KeyAction.RIGHT) ?  1 : 0)
                + (keyPressed.get(KeyAction.LEFT)  ? -1 : 0);
        yawInput = (keyPressed.get(KeyAction.ROTATE_CCW) ?  1 : 0)
                + (keyPressed.get(KeyAction.ROTATE_CW)   ? -1 : 0);
    }

    /**
     * @return current axial input: +1 = full forward, -1 = full reverse, 0 = none
     */
    @Override
    public double getAxial() {
        return axialInput;
    }

    /**
     * @return current lateral input: +1 = full right, -1 = full left, 0 = none
     */
    @Override
    public double getLateral() {
        return lateralInput;
    }

    /**
     * @return current yaw input: +1 = CCW, -1 = CW, 0 = none
     */
    @Override
    public double getYaw() {
        return yawInput;
    }
}