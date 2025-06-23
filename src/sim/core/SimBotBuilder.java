package sim.core;

/**
 * Fluent builder for creating and configuring a SimBot instance.
 * <p>
 *     Provides sensible defaults to simplify quick instantiation
 */
public class SimBotBuilder {
    private double x = 0, y = 0, heading = 0;
    private double maxVel = 60, maxAccel = 60, maxAngVel = Math.PI, maxAngAccel = Math.PI;
    private double width = 17.25, length = 17.25; // Inches (standard FTC sizing)

    /**
     * Sets the robot's initial pose.
     * @param x         starting X position (inches)
     * @param y         starting Y position (inches)
     * @param heading   starting heading (radians)
     * @return this builder instance for chaining
     */
    public SimBotBuilder setStartPose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
        return this;
    }

    /**
     * Configures the robot's motion constraints.
     * @param maxVel        max linear velocity (inches/sec)
     * @param maxAccel      max linear acceleration (inches/sec^2)
     * @param maxAngVel     max angular velocity (rad/sec)
     * @param maxAngAccel   max angular acceleration (rad/sec^2)
     * @return this builder instance for chaining
     */
    public SimBotBuilder setConstraints(
            double maxVel, double maxAccel,
            double maxAngVel, double maxAngAccel) {
        this.maxVel = maxVel;
        this.maxAccel = maxAccel;
        this.maxAngVel = maxAngVel;
        this.maxAngAccel = maxAngAccel;
        return this;
    }

    /**
     * Defines the robot's footprint dimensions.
     * @param width     robot width (inches)
     * @param length    robot length (inches)
     * @return this builder instance for chaining
     */
    public SimBotBuilder setDimensions(double width, double length) {
        this.width = width;
        this.length = length;
        return this;
    }

    /**
     * Constructs a new SimBot with the configured parameters/
     * @return a fully configured SimBot instance
     */
    public SimBot build() {
        return new SimBot(
                x, y, heading,
                maxVel, maxAccel,
                maxAngVel, maxAngAccel,
                width, length
        );
    }
}