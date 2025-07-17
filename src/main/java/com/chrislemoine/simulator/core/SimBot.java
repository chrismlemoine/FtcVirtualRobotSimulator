package com.chrislemoine.simulator.core;

/**
 * Core data model representing the robot's pose (x, y, heading)
 * and its dynamic state with acceleration-limited motion in three axes:
 *   - Axial (forward/back)
 *   - Lateral (strafe left/right)
 *   - Yaw (rotation)
 * All distances are in inches and angles in radians
 */
public class SimBot {
    // --- Pose (field coordinates) ---
    private double x;       // X position (+right)
    private double y;       // Y position (+forward)
    private double heading; // Rotation: 0 = +X axis, pi/2 = +Y axis

    // --- Physical limits ---
    private double maxSpeed;        // Max linear speed (inches/sec)
    private double maxAccel;        // Max linear acceleration (inches/sec^2)
    private double maxYawSpeed;     // Max rotational speed (rad/sec)
    private double maxYawAccel;     // Max rotational acceleration (rad/sec^2)
    private double width, length;   // Robot footprint (inches)

    // --- Dynamic state ---
    private double axialVel = 0.0;      // Current forward velocity
    private double lateralVel = 0.0;    // Current strafe velocity
    private double yawVel = 0.0;        // Current rotational velocity

    private double targetAxialVel = 0.0;    // Desired forward velocity
    private double targetLateralVel = 0.0;  // Desired strafe velocity
    private double targetYawVel = 0.0;      // Desired rotational velocity

    /**
     * @param x           initial X position (inches)
     * @param y           initial Y position (inches)
     * @param heading     initial heading (radians)
     * @param maxSpeed    maximum linear velocity (inches/sec)
     * @param maxAccel    maximum linear acceleration (inches/sec^2)
     * @param maxYawVel   maximum angular velocity (rad/sec)
     * @param maxYawAccel maximum angular acceleration (inches/sec^2)
     * @param width       robots width (inches)
     * @param length      robots length (inches)
     */
    public SimBot(double x, double y, double heading,
                  double maxSpeed, double maxAccel,
                  double maxYawVel, double maxYawAccel,
                  double width, double length) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.maxYawSpeed = maxYawVel;
        this.maxYawAccel = maxYawAccel;
        this.width = width;
        this.length = length;
    }

    // --- Pose getters ---
    public double getX() { return x; }
    public double getY() { return y; }
    public double getHeading() { return heading; }

    // --- Limit getters ---
    public double getMaxSpeed() { return maxSpeed; }
    public double getMaxAccel() { return maxAccel; }
    public double getMaxYawSpeed() { return maxYawSpeed; }
    public double getMaxYawAccel() { return maxYawAccel; }

    // --- Dimension getters ---
    public double getWidth() { return width; }
    public double getLength() { return length; }

    /**
     * Set desired linear speed.
     * @param axialSpeed     forward/backward speed (inches/sec): +forward, -reverse
     * @param lateralSpeed   strafe speed (inches/sec): +right, -left
     */
    public void setTargetVel(double axialSpeed, double lateralSpeed) {
        this.targetAxialVel = clamp(axialSpeed, -maxSpeed, maxSpeed);
        this.targetLateralVel = clamp(lateralSpeed, -maxSpeed, maxSpeed);
    }

    /**
     * Set desired rotational speed.
     * @param yawSpeed rotational speed (rad/sec): +CCW, -CW
     */
    public void setTargetYawVel(double yawSpeed) {
        this.targetYawVel = clamp(yawSpeed, -maxYawSpeed, maxYawSpeed);
    }

    /**
     * Update position and velocities over time dt, respecting acceleration limits.
     * @param dt time step in seconds
     */
    public void update (double dt) {
        // --- Linear acceleration ramp ---
        double dAxial = targetAxialVel - axialVel;
        axialVel += clamp(dAxial, -maxAccel * dt, maxAccel * dt);

        double dLat = targetLateralVel - lateralVel;
        lateralVel += clamp(dLat, -maxAccel * dt, maxAccel * dt);

        // --- Rotational acceleration ramp ---
        double dYaw = targetYawVel - yawVel;
        yawVel += clamp(dYaw, -maxYawAccel * dt, maxYawAccel * dt);

        // --- Integrate position in field frame ---
        double forward = axialVel * dt;
        double strafe = lateralVel * dt;
        double cosH = Math.cos(heading);
        double sinH = Math.sin(heading);
        x += forward * cosH - strafe * sinH;
        y += forward * sinH + strafe *cosH;

        // --- Integrate heading ---
        heading += yawVel * dt;
    }

    /**
     * Constrain v to [min. max]
     */
    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * Instantly set pose (teleport)
     */
    public void setPose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }
}
