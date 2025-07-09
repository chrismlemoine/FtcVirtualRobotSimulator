package com.chrislemoine.simulator.core;

/**
 * Fluent builder for SimBot: configure start pose, constraints, and size in a chainable way.
 */
public class SimBotBuilder {
    // Default values
    private double x = 0, y = 0, heading = 0;
    private double maxVel = 60, maxAccel = 60;
    private double maxAngVel = Math.PI, maxAngAccel = Math.PI;
    private double width = 17.25, length= 17.25;

    /**
     * Sets the initial pose of the robot.
     * @param x       starting X position (inches)
     * @param y       starting Y position (inches)
     * @param heading starting heading (radians)
     * @return this builder for chaining
     */
    public SimBotBuilder setStartPose(double x, double y, double heading) {
        this.x       = x;
        this.y       = y;
        this.heading = heading;
        return this;
    }

    /**
     * Configures motion constraints.
     * @param maxVel      max linear velocity (inches/sec)
     * @param maxAccel    max linear acceleration (inches/sec^2)
     * @param maxAngVel   max angular velocity (rad/sec)
     * @param maxAngAccel max angular acceleration (rad/sec^2)
     * @return this builder for chaining
     */
    public SimBotBuilder setConstraints (double maxVel, double maxAccel,
                                         double maxAngVel, double maxAngAccel) {
        this.maxVel      = maxVel;
        this.maxAccel    = maxAccel;
        this.maxAngVel   = maxAngVel;
        this.maxAngAccel = maxAngAccel;
        return this;
    }

    /**
     * Sets the robot's footprint dimensions.
     * @param width  robots width (inches)
     * @param length robots length (inches)
     * @return this builder for chaining
     */
    public SimBotBuilder setDimensions(double width, double length) {
        this.width  = width;
        this.length = length;
        return this;
    }

    /**
     * Builds a SimBot instance with the configured parameters.
     * @return a new SimBot
     */
    public SimBot build() {
        return new SimBot(x, y, heading,
                          maxVel, maxAccel,
                          maxAngVel, maxAngAccel,
                          width, length);
    }
}
