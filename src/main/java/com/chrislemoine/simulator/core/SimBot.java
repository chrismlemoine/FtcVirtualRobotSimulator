package com.chrislemoine.simulator.core;

/**
 * Represents a robot on the FTC field with independent, acceleration-limited motion
 * in three axes:
 * <ul>
 *   <li>Axial (forward/backward)</li>
 *   <li>Lateral (strafe left/right)</li>
 *   <li>Yaw (rotation)</li>
 * </ul>
 * All distances are in inches, angles in radians, and velocities in their respective units per second.
 */
public class SimBot {
    // --- Pose ---
    private double x;                 // Field X coordinate (+right)
    private double y;                 // Field Y coordinate (+forward)
    private double heading;           // Orientation: 0=+X axis, +π/2=+Y axis

    // --- Motion limits (final) ---
    private final double maxSpeed;        // inches/sec
    private final double maxAccel;        // inches/sec^2
    private final double maxBrakeAccel;   // inches/sec^2 when decelerating

    private final double maxYawSpeed;     // rad/sec
    private final double maxYawAccel;     // rad/sec^2
    private final double maxBrakeYaw;     // rad/sec^2 when decelerating yaw

    // Robot footprint (inches)
    private final double width;
    private final double length;

    // --- Dynamic state ---
    private double axialVel = 0.0;
    private double lateralVel = 0.0;
    private double yawVel = 0.0;

    // --- Target speeds ---
    private double targetAxial = 0.0;
    private double targetLateral = 0.0;
    private double targetYaw = 0.0;

    /**
     * Constructs a SimBot with full motion constraints.
     * @param x               initial X (inches)
     * @param y               initial Y (inches)
     * @param heading         initial heading (radians)
     * @param maxSpeed        max linear speed (inches/sec)
     * @param maxAccel        max linear acceleration (inches/sec^2)
     * @param maxBrakeAccel   max linear deceleration (inches/sec^2)
     * @param maxYawSpeed     max rotational speed (rad/sec)
     * @param maxYawAccel     max rotational acceleration (rad/sec^2)
     * @param maxBrakeYaw     max rotational deceleration (rad/sec^2)
     * @param width           robot width (inches)
     * @param length          robot length (inches)
     */
    public SimBot(double x, double y, double heading,
                  double maxSpeed, double maxAccel, double maxBrakeAccel,
                  double maxYawSpeed, double maxYawAccel, double maxBrakeYaw,
                  double width, double length) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.maxBrakeAccel = maxBrakeAccel;
        this.maxYawSpeed = maxYawSpeed;
        this.maxYawAccel = maxYawAccel;
        this.maxBrakeYaw = maxBrakeYaw;
        this.width = width;
        this.length = length;
    }

    // --- Pose getters ---
    public double getX() { return x; }
    public double getY() { return y; }
    public double getHeading() { return heading; }

    // --- Motion limit getters ---
    public double getMaxSpeed() { return maxSpeed; }
    public double getMaxAccel() { return maxAccel; }
    public double getMaxBrakeAccel() { return maxBrakeAccel; }
    public double getMaxYawSpeed() { return maxYawSpeed; }
    public double getMaxYawAccel() { return maxYawAccel; }
    public double getMaxBrakeYaw() { return maxBrakeYaw; }

    // --- Dimension getters ---
    public double getWidth() { return width; }
    public double getLength() { return length; }

    /**
     * Sets the target forward speed.
     * @param speed desired axial velocity (inches/sec)
     */
    public void setTargetAxial(double speed) {
        this.targetAxial = clamp(speed, -maxSpeed, maxSpeed);
    }

    /**
     * Sets the target strafe speed.
     * @param speed desired lateral velocity (inches/sec)
     */
    public void setTargetLateral(double speed) {
        this.targetLateral = clamp(speed, -maxSpeed, maxSpeed);
    }

    /**
     * Sets the target yaw speed.
     * @param speed desired rotational velocity (rad/sec)
     */
    public void setTargetYaw(double speed) {
        this.targetYaw = clamp(speed, -maxYawSpeed, maxYawSpeed);
    }

    /**
     * Advances the robot state by dt seconds, applying accel/brake logic.
     * @param dt time increment in seconds
     */
    public void update(double dt) {
        // Linear: axial
        double dAx = targetAxial - axialVel;
        double capAx = (Math.signum(dAx) == Math.signum(targetAxial)) ? maxAccel : maxBrakeAccel;
        axialVel += clamp(dAx, -capAx * dt, capAx * dt);

        // Linear: lateral
        double dLat = targetLateral - lateralVel;
        double capLat = (Math.signum(dLat) == Math.signum(targetLateral)) ? maxAccel : maxBrakeAccel;
        lateralVel += clamp(dLat, -capLat * dt, capLat * dt);

        // Rotational: yaw
        double dYaw = targetYaw - yawVel;
        double capYaw = (Math.signum(dYaw) == Math.signum(targetYaw)) ? maxYawAccel : maxBrakeYaw;
        yawVel += clamp(dYaw, -capYaw * dt, capYaw * dt);

        // Integrate pose (field frame)
        double dx = axialVel * dt;
        double dy = lateralVel * dt;
        double cosH = Math.cos(heading), sinH = Math.sin(heading);
        x += dx * cosH - dy * sinH;
        y += dx * sinH + dy * cosH;

        // Integrate heading
        heading += yawVel * dt;
    }

    /**
     * Instantly stops all motion (zero velocities and targets).
     */
    public void stop() {
        axialVel = lateralVel = yawVel = 0;
        targetAxial = targetLateral = targetYaw = 0;
    }

    // --- Utility ---
    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}