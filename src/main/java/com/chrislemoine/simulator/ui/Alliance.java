package com.chrislemoine.simulator.ui;

/**
 * Represents the FTC match alliance side.
 * Determines the field orientation and robot color scheme:
 * - RED: no field rotation, red robot tint
 * - BLUE: 180° field rotation, blue robot tint
 */
public enum Alliance {
    /** Red alliance; field shown as originally oriented. */
    RED,

    /** Blue alliance; field flipped 180° for driver perspective. */
    BLUE;
}
