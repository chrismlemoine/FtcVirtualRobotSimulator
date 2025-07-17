package com.chrislemoine.simulator.core;

/**
 * Core data model representing the robot's pose and physical constraints.
 * Coordinates and dimensions are in inches; heading is in radians.
 */
public class SimBot {
    private double x, y, heading;          // Pose: inches, inches, radians
    private double maxVel, maxAccel;       // Linear limits (inches/sec, inches/sec^2)
    private double maxAngVel, maxAngAccel; // Angular limits (rad/sec, rad/sec^2)
    private double width, length;          // Robot footprint

    // --- dynamic state ---
    private double velX = 0.0, velY = 0.0;             // current linear velocities
    private double targetVelX = 0.0, targetVelY = 0.0; // desired linear velocities
    private double angVel = 0.0;                       // current angular velocity
    private double targetAngVel = 0.0;                 // desired angular velocity

    /**
     * @param x           initial X position (inches)
     * @param y           initial Y position (inches)
     * @param heading     initial heading (radians)
     * @param maxVel      maximum linear velocity (inches/sec)
     * @param maxAccel    maximum linear acceleration (inches/sec^2)
     * @param maxAngVel   maximum angular velocity (rad/sec)
     * @param maxAngAccel maximum angular acceleration (inches/sec^2)
     * @param width       robots width (inches)
     * @param length      robots length (inches)
     */
    public SimBot(double x, double y, double heading,
                  double maxVel, double maxAccel,
                  double maxAngVel, double maxAngAccel,
                  double width, double length) {
        this.x           = x;
        this.y           = y;
        this.heading     = heading;
        this.maxVel      = maxVel;
        this.maxAccel    = maxAccel;
        this.maxAngVel   = maxAngVel;
        this.maxAngAccel = maxAngAccel;
        this.width       = width;
        this.length      = length;
    }

    // Pose getters
    public double getX()           { return x; }
    public double getY()           { return y; }
    public double getHeading()     { return heading; }

    // Constraint getters
    public double getMaxVel()      { return maxVel; }
    public double getMaxAccel()    { return maxAccel; }
    public double getMaxAngVel()   { return  maxAngVel; }
    public double getMaxAngAccel() { return maxAngAccel; }

    // Dimension getters
    public double getWidth()       { return width; }
    public double getLength()      { return length; }

    /**
     * Sets desired linear velocity.
     * @param vx strafe velocity (inches/sec)
     * @param vy drive velocity (inches/sec)
     */
    public void setTargetVel(double vx, double vy) {
        this.targetVelX = clamp(vx, -maxVel, maxVel);
        this.targetVelY = clamp(vy, -maxVel, maxVel);
    }

    /**
     * Sets desired angular velocity.
     * @param av angular velocity (rad/sec)
     */
    public void setTargetAngVel(double av) {
        this.targetAngVel = clamp(av, -maxAngVel, maxAngVel);
    }

    /**
     * Updates the robot's dynamic state and pose with ramping
     * @param dt time step (seconds)
     */
    public void update (double dt) {
        double dvx = targetVelX - velX;
        double maxDV = maxAccel * dt;
        velX += clamp(dvx, -maxDV, maxDV);

        double dvy = targetVelY - velY;
        velY += clamp(dvy, -maxDV, maxDV);

        double dav = targetAngVel - angVel;
        double maxDA = maxAngAccel * dt;
        angVel += clamp(dav, -maxDA, maxDA);

        double forward = velY * dt;
        double strafe = velX * dt;
        double cosH = Math.cos(heading);
        double sinH = Math.sin(heading);
        x += forward * cosH + strafe * sinH;
        y += forward * sinH - strafe *cosH;

        heading += angVel * dt;
    }

    /**
     * Clamp helper
     */
    private double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * Directly set the robot's pose.
     */
    public void setPose(double x, double y, double heading) {
        this.x       = x;
        this.y       = y;
        this.heading = heading;
    }
}
