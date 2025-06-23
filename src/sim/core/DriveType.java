package sim.core;

/**
 * Enumeration of supported drive train types for manual control.
 */
public enum DriveType {
    /**
     * Standard mecanum implementation (holonomic movement)
     */
    MECANUM,
    /**
     * Differential "tank" drive (left/right control).
     */
    TANK
}