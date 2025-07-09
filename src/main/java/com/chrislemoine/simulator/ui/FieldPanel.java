package com.chrislemoine.simulator.ui;

import com.chrislemoine.simulator.core.SimBot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Swing panel responsible for rendering the FTC field and a basic robot
 * <p> - Draws a background image while preserving its aspect ratio.</p>
 * <p> - flids the field 180 degrees for Blue alliance.</p>
 * <p> - Centers and scales the robot to the FTC 144" coordinate system.</p>
 * <p> - Renders the robot body in a custom semi-transparent red.</p>
 * <p> - Draws wheels in a darker red.</p>
 * <p> - Draws a direction marker and thin black outline.</p>
 */
public class FieldPanel extends JPanel {
    private final SimBot robot;
    private final Background backgroundChoice;
    private final Alliance alliance;
    private BufferedImage backgroundImage;

    /**
     * @param robot            the SimBot model to render
     * @param backgroundChoice the selected background theme
     * @param alliance         the selected alliance (Red or Blue)
     */
    public FieldPanel(SimBot robot, Background backgroundChoice, Alliance alliance) {
        this.robot = robot;
        this.backgroundChoice = backgroundChoice;
        this.alliance = alliance;
        loadBackgroundImage();
    }

    /**
     * Loads the background image from resources or sets to null on failure.
     */
    private void loadBackgroundImage() {
        try {
            String path = "/background/" + backgroundChoice.getFilename();
            backgroundImage = ImageIO.read(getClass().getResource(path));
        } catch (Exception e) {
            backgroundImage = null;
            System.err.println("Failed to load background: " + backgroundChoice.getFilename());
        }
    }

    /**
     * Paints the background (aspect-ratio preserved) and the robot visuals.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Save the original transform for restoration
        AffineTransform orig = g2.getTransform();

        // Rotate background for Blue alliance
        if (alliance == Alliance.BlUE) {
            double pw = getWidth();
            double ph = getHeight();
            g2.rotate(Math.PI, pw / 2.0, ph / 2.0);
        }

        // Draw the selected background image or fallback to grey
        if (backgroundImage != null) {
            int imgW = backgroundImage.getWidth();
            int imgH = backgroundImage.getHeight();
            double panelW = getWidth();
            double panelH = getHeight();

            double scaleX = panelW / imgW;
            double scaleY = panelH / imgH;
            double scaleImage = Math.min(scaleX, scaleY);

            int drawW = (int)(imgW * scaleImage);
            int drawH = (int)(imgH * scaleImage);
            int x = (int)((panelW - drawW) / 2);
            int y = (int)((panelH - drawH) / 2);

            g2.drawImage(backgroundImage, x, y, drawW, drawH, null);
        } else {
            g2.setColor(Color.GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // Enable antialiasing for smooth lines
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        // Transform to FTC coordinates (144" field)
        double panelMin = Math.min(getWidth(), getHeight());
        double scale = panelMin / 144.0;
        g2.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2.scale(scale, -scale);

        g2.translate(robot.getX(), robot.getY());
        g2.rotate(robot.getHeading());

        // Body
        double w = robot.getWidth();
        double h = robot.getLength();
        if (alliance == Alliance.BlUE) {
            Color bodyColor = new Color(46, 49, 146, 200);
            g2.setColor(bodyColor);
        } else if (alliance == Alliance.RED){
            Color bodyColor = new Color(237, 28, 36, 200);
            g2.setColor(bodyColor);
        }
        Rectangle2D.Double bodyRect = new Rectangle2D.Double(-w/2,-h/2, w, h);
        g2.fill(bodyRect);

        // Wheels
        double wheelH = 2.0;
        double wheelW = 4.0;

        if (alliance == Alliance.BlUE) {
            Color wheelColor = new Color(23, 25, 73, 200);
            g2.setColor(wheelColor);
        } else if (alliance == Alliance.RED) {
            Color wheelColor = new Color(119, 13, 18, 200);
            g2.setColor(wheelColor);
        }

        // frontLeft
        Rectangle2D.Double fl = new Rectangle2D.Double(w/2 - wheelW - 1, h/2 - wheelH - 1, wheelW, wheelH);
        g2.fill(fl);
        // frontRight
        Rectangle2D.Double fr = new Rectangle2D.Double(w/2 - wheelW - 1, -h/2 + 1, wheelW, wheelH);
        g2.fill(fr);
        // rearLeft
        Rectangle2D.Double rl = new Rectangle2D.Double(-w/2 + 1, h/2 - wheelH - 1, wheelW, wheelH);
        g2.fill(rl);
        // rearRight
        Rectangle2D.Double rr = new Rectangle2D.Double(-w/2 + 1, -h/2 + 1, wheelW, wheelH);
        g2.fill(rr);

        // Direction Marker
        double dirH = h / 3;
        double dirW = w * 0.05;

        Rectangle2D.Double dir = new Rectangle2D.Double(dirH / 2, -dirW / 2, dirH, dirW);
        g2.fill(dir);

        g2.setStroke(new BasicStroke((float)(1.0 / scale)));
        g2.draw(bodyRect);

    }
}
