package sim.core;

/**
 * Core data model representing the robot's pose and physical constraints
 * <p>
 *     Coordinates and dimensions are in inches; heading is in radians.
 */
public class SimBot {
    private double x, y, heading; // Pose: (inches, inches, radians)
    private double maxVel, maxAccel; // Linear constraints (inches/sec, inches/sec^2)
    private double maxAngVel, maxAngAccel; // Angular constraints (rad/sec. rad/sec^2)
    private double width, length; // Robot footprint (inches)

    /**
     * @param x             initial X position (inches)
     * @param y             initial Y position (inches)
     * @param heading       initial heading (radians)
     * @param maxVel        maximum linear velocity (inches/sec)
     * @param maxAccel      maximum linear acceleration (inches/sec^2)
     * @param maxAngVel     maximum angular velocity (rad/sec)
     * @param maxAngAccel   maximum angular acceleration (rad/sec^2)
     * @param width         robot width (inches)
     * @param length        robot length (inches)
     */
    public SimBot(double x, double y, double heading,
                  double maxVel, double maxAccel, double maxAngVel, double maxAngAccel,
                  double width , double length) {
        this.x              = x;
        this.y              = y;
        this.heading        = heading;
        this.maxVel         = maxVel;
        this.maxAccel       = maxAccel;
        this.maxAngVel      = maxAngVel;
        this.maxAngAccel    = maxAngAccel;
        this.width          = width;
        this.length         = length;
    }

    // Pose getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getHeading() { return heading; }

    // Constraint getters
    public double getMaxVel() { return maxVel; }
    public double getMaxAccel() { return maxAccel; }
    public double getMaxAngVel() { return maxAngVel; }
    public double getMaxAngAccel() { return maxAngAccel; }

    // Dimension getters
    public double getWidth() { return width; }
    public double getLength() { return length; }

    /**
     * Updates the robot's pose based on elapsed time.
     * <p>
     *     Placeholder until manual or autonomous control is implemented.
     * @param deltaTime elapsed time in seconds since last update
     */
    public void update(double deltaTime) {
        // No-op: motion handled by control logic (manual or scripted)
    }

    /**
     * Directly sets the robot's pose.
     * @param x         new X position (inches)
     * @param y         new Y position (inches)
     * @param heading   new heading (radians)
     */
    public void setPose(double x, double y, double heading) {
        this.x = x;
        this.y = y;
        this.heading = heading;
    }
}