package com.chrislemoine.simulator.core;

/**
 * Supported drive train types for manual control.
 * <ul>
 *     <li>MECANUM: holonomic movement using mecanum wheels.</li>
 *     <li>TANK: differential drive (left/right sticks control each side).</li>
 * </ul>
 */
public enum DriveType {
    /** Holonomic mecanum drive */
    MECANUM,

    /** Differential "tank" drive. */
    TANK
}
