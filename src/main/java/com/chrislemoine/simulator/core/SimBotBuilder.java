package com.chrislemoine.simulator.core;

/**
 * Fluent builder for SimBot: configure its starting pose, motion constraints,
 * and physical dimensions in a chainable way.
 */
public class SimBotBuilder {
    // --- Default configuration values ---
    private double x = 0, y = 0, heading = 0;
    private double maxSpeed = 60, maxAccel = 60;
    private double maxYawSpeed = Math.PI, maxYawAccel = Math.PI;
    private double width = 17.25, length= 17.25;

    /**
     * Set the robots initial pose on the field.
     * @param x       starting X position in inches
     * @param y       starting Y position in inches
     * @param heading heading angle in radians (0 = +X axis)
     * @return this builder instance (for chaining)
     */
    public SimBotBuilder setStartPose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        return this;
    }

    /**
     * Configure the robot's motion limits.
     * @param maxSpeed    maximum linear speed (inches/sec)
     * @param maxAccel    maximum linear acceleration (inches/sec^2)
     * @param maxYawSpeed maximum rotational speed (rad/sec)
     * @param maxAngAccel maximum rotational acceleration (rad/sec^2)
     * @return this builder instance (for chaining)
     */
    public SimBotBuilder setConstraints (double maxSpeed, double maxAccel,
                                         double maxYawSpeed, double maxAngAccel) {
        this.maxSpeed = maxSpeed;
        this.maxAccel = maxAccel;
        this.maxYawSpeed = maxYawSpeed;
        this.maxYawAccel = maxAngAccel;
        return this;
    }

    /**
     * Sets the robot's physical footprint dimensions.
     * @param width  robot width in inches (side-to-side)
     * @param length robot length in inches (front-to-back)
     * @return this builder instance (for chaining)
     */
    public SimBotBuilder setDimensions(double width, double length) {
        this.width = width;
        this.length = length;
        return this;
    }

    /**
     * Build a new SimBot instance using the configured parameters.
     * @return a SimBot ready for simulation
     */
    public SimBot build() {
        return new SimBot(
                x, y, heading,
                maxSpeed, maxAccel,
                maxYawSpeed, maxYawAccel,
                width, length
        );
    }
}
