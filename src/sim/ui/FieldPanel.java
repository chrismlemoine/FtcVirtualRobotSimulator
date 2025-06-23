package sim.ui;

import sim.core.SimBot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Swing panel responsible for rendering the FTC field and robot.
 * <p> - Centers coordinate system at (0,0). </p>
 * <p> - Scales the view so that 144 inches equals the smalledst panel dimension. </p>
 * <p> - Flips the Y-axis so that positive Y is up, matching FTC conversions. </p>
 * <p> - Optionally renders a background image behind field elements. </p>
 */
public class FieldPanel extends JPanel {
    private final SimBot robot;

    /**
     * @param robot the SimBot instance whose pose will be visualized
     */
    public FieldPanel(SimBot robot) {
        this.robot = robot;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Antialiasing for smoother lines
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Determine scale: 144" field fits into the smaller panel dimension
        double size = Math.min(getWidth(), getHeight());
        double scale = size / 144.0;

        // Translate origin to panel center and flip Y-axis
        g2.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2.scale(scale, -scale);

        // Draw Field boundary (12' x 12')
        g2.setColor(Color.BLACK);
        g2.drawRect(-72, -72, 144, 144);

        // Draw the robot as a filled rectangle at its pose
        g2.setColor(Color.RED);
        g2.translate(robot.getX(), robot.getY());
        g2.rotate(robot.getHeading());
        double w = robot.getWidth();
        double h = robot.getLength();
        g2.fillRect((int) (-w / 2), (int) (-h / 2), (int) w, (int) h);

        // Restore transform for any further painting
        g2.rotate(-robot.getHeading());
        g2.translate(-robot.getX(), -robot.getY());
    }
}