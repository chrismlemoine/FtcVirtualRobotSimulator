package com.chrislemoine.simulator.input;

/**
 * Abstraction for any drive input source (keyboard, gamepad, etc.).
 * <p>Poll this controller once per simulation tick, then query:</p>
 * <ul>
 *   <li>{@link #getAxial()} for forward/back input (+1 = full forward, -1 = full reverse)</li>
 *   <li>{@link #getLateral()} for strafe left/right (+1 = full right, -1 = full left)</li>
 *   <li>{@link #getYaw()} for rotation (+1 = full counterclockwise, -1 = full clockwise)</li>
 * </ul>
 * All values are in the range [-1, 1], representing full input in that axis.
 */
public interface DriveController {
    /**
     * Refresh internal input state (poll hardware, process events, etc.).
     * Must be called once each frame before querying axis getters.
     */
    void poll();

    /**
     * @return forward/backward drive input: +1 = full forward, -1 = full reverse
     */
    double getAxial();

    /**
     * @return strafe input: +1 = full right, -1 = full left
     */
    double getLateral();

    /**
     * @return rotational input (yaw): +1 = full counterclockwise, -1 = full clockwise
     */
    double getYaw();
}
