package com.chrislemoine.simulator.input;

/**
 * Provides X/Y/rotational drive inputs in the range [-1, 1]
 */
public interface DriveController {
    /** Called once per frame to update internal state (poll hardware, etc.) */
    void poll();

    /** @return forward/backward input +1 = full ahead, -1 = full reverse */
    double getAxial();

    /** @return left/right input +1 = right, -1 = left */
    double getLateral();

    /** @return rotation input: +1 = Counterclockwise, -1 = Clockwise */
    double getYaw();
}
