package com.chrislemoine.simulator.input;

/**
 * Defines a generic drive input source for the SimBot.
 * <p>
 * Call {@link #poll()} once per frame to refresh input state,
 * then query the axes:
 * </p>
 * <ul>
 *   <li>{@link #getAxial()}: forward/backward input (+1 = full forward, -1 = full reverse)</li>
 *   <li>{@link #getLateral()}: strafe input (+1 = full right, -1 = full left)</li>
 *   <li>{@link #getYaw()}: rotational input (+1 = full counterclockwise, -1 = full clockwise)</li>
 * </ul>
 * All return values are in the range [-1, 1].
 */
public interface DriveController {
    /**
     * Refreshes the internal input state (poll hardware or process events).
     * Must be invoked before reading any axis getters each frame.
     */
    void poll();

    /**
     * @return axial (forward/backward) input: +1 = full forward, -1 = full reverse
     */
    double getAxial();

    /**
     * @return lateral (strafe left/right) input: +1 = full right, -1 = full left
     */
    double getLateral();

    /**
     * @return yaw (rotational) input: +1 = full counterclockwise, -1 = full clockwise
     */
    double getYaw();
}