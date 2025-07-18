package com.chrislemoine.simulator.core;

/**
 * Fluent builder for {@link SimBot}: configure starting pose, drive constraints,
 * brake behavior, and physical dimensions in a clear, chainable API.
 */
public class SimBotBuilder {
    // --- Initial pose ---
    private double startX = 0;
    private double startY = 0;
    private double startHeading = 0;

    // --- Linear drive limits ---
    private double maxLinearSpeed = 60.0;    // inches/sec
    private double maxLinearAccel = 60.0;    // inches/sec^2
    private double brakeLinearFactor = 5.0;  // deceleration = accel * factor

    // --- Rotational drive limits ---
    private double maxYawSpeed = Math.PI;    // rad/sec
    private double maxYawAccel = Math.PI;    // rad/sec^2
    private double brakeYawFactor = 5.0;     // deceleration = accel * factor

    // --- Physical footprint ---
    private double width = 17.25;   // inches
    private double length = 17.25;  // inches

    /**
     * Sets the robot's initial field pose.
     * @param x       X position in inches (+right)
     * @param y       Y position in inches (+forward)
     * @param heading heading in radians (0 = +X axis)
     * @return this builder for chaining
     */
    public SimBotBuilder setStartPose(double x, double y, double heading) {
        this.startX = x;
        this.startY = y;
        this.startHeading = heading;
        return this;
    }

    /**
     * Configures the robot's linear and rotational motion constraints.
     * @param maxLinearSpeed max forward/strafe speed (inches/sec)
     * @param maxLinearAccel max linear acceleration (inches/sec^2)
     * @param maxYawSpeed    max rotational speed (rad/sec)
     * @param maxYawAccel    max rotational acceleration (rad/sec^2)
     * @return this builder for chaining
     */
    public SimBotBuilder setConstraints(
            double maxLinearSpeed,
            double maxLinearAccel,
            double maxYawSpeed,
            double maxYawAccel
    ) {
        this.maxLinearSpeed = maxLinearSpeed;
        this.maxLinearAccel = maxLinearAccel;
        this.maxYawSpeed    = maxYawSpeed;
        this.maxYawAccel    = maxYawAccel;
        return this;
    }

    /**
     * Adjusts brake (deceleration) behavior relative to acceleration.
     * @param linearFactor multiplier for linear deceleration
     * @param yawFactor    multiplier for rotational deceleration
     * @return this builder for chaining
     */
    public SimBotBuilder setBrakeFactors(double linearFactor, double yawFactor) {
        this.brakeLinearFactor = linearFactor;
        this.brakeYawFactor    = yawFactor;
        return this;
    }

    /**
     * Sets the robot's physical footprint dimensions.
     * @param width  side-to-side width in inches
     * @param length front-to-back length in inches
     * @return this builder for chaining
     */
    public SimBotBuilder setDimensions(double width, double length) {
        this.width  = width;
        this.length = length;
        return this;
    }

    /**
     * Builds and returns a {@link SimBot} instance with the configured parameters.
     * @return a SimBot ready for simulation
     */
    public SimBot build() {
        double brakeLinearAccel = maxLinearAccel * brakeLinearFactor;
        double brakeYawAccel    = maxYawAccel    * brakeYawFactor;
        return new SimBot(
                startX,
                startY,
                startHeading,
                maxLinearSpeed,
                maxLinearAccel,
                brakeLinearAccel,
                maxYawSpeed,
                maxYawAccel,
                brakeYawAccel,
                width,
                length
        );
    }
}