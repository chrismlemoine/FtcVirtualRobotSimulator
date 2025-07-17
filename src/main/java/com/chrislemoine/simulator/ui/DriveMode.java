package com.chrislemoine.simulator.ui;

/**
 * Specifies how controller inputs are mapped to robot motion:
 * <ul>
 *   <li><b>ROBOT_CENTRIC</b>: inputs move the robot relative to its own heading.</li>
 *   <li><b>FIELD_CENTRIC</b>: inputs move the robot relative to the fixed field axes.</li>
 * </ul>
 */
public enum DriveMode {
    /** Map forward/back and strafe relative to robot orientation. */
    ROBOT_CENTRIC,

    /** Map inputs so "forward" always means toward the top of the field. */
    FIELD_CENTRIC;
}