package com.chrislemoine.simulator.ui;

import com.chrislemoine.simulator.core.SimBot;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Panel responsible for drawing the FTC field background and the robot.
 * <ul>
 *   <li>Preserves background aspect ratio and rotates 180° for BLUE alliance.</li>
 *   <li>Uses a 144" coordinate system, scaling to panel size.</li>
 *   <li>Renders the robot body, wheels, direction marker, and outline.</li>
 * </ul>
 */
public class FieldPanel extends JPanel {
    private final SimBot robot;
    private final Background backgroundChoice;
    private final Alliance alliance;
    private BufferedImage backgroundImage;

    /**
     * @param robot            the robot data model to render
     * @param backgroundChoice theme for field background
     * @param alliance         current alliance for tint/rotation
     */
    public FieldPanel(SimBot robot, Background backgroundChoice, Alliance alliance) {
        this.robot = robot;
        this.backgroundChoice = backgroundChoice;
        this.alliance = alliance;
        loadBackgroundImage();
    }

    /**
     * Attempts to load the selected background image, logs on failure.
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
     * Draws the field and robot each frame.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // --- Background ---
        if (backgroundImage != null) {
            double panelW = getWidth(), panelH = getHeight();
            double imgW = backgroundImage.getWidth(), imgH = backgroundImage.getHeight();
            double scale = Math.min(panelW / imgW, panelH / imgH);
            int drawW = (int) (imgW * scale), drawH = (int) (imgH * scale);
            int offsetX = (getWidth() - drawW) / 2;
            int offsetY = (getHeight() - drawH) / 2;

            // Flip for BLUE alliance
            if (alliance == Alliance.BLUE) {
                g2.rotate(Math.PI, getWidth() / 2.0, getHeight() / 2.0);
            }

            g2.drawImage(backgroundImage, offsetX, offsetY, drawW, drawH, null);
        } else {
            g2.setColor(Color.GRAY);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }

        // --- Robot rendering setup ---
        g2.setTransform(new AffineTransform());
        double pixelsPerInch = Math.min(getWidth(), getHeight()) / 144.0;
        g2.translate(getWidth() / 2.0, getHeight() / 2.0);
        g2.scale(pixelsPerInch, -pixelsPerInch);
        g2.translate(robot.getX(), robot.getY());
        g2.rotate(robot.getHeading());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // --- Choose colors ---
        Color bodyColor = (alliance == Alliance.BLUE)
                ? new Color(46, 49, 146, 200)
                : new Color(237, 28, 36, 200);
        Color wheelColor = (alliance == Alliance.BLUE)
                ? new Color(23, 25, 73, 200)
                : new Color(119, 13, 18, 200);

        // --- Draw body ---
        double w = robot.getWidth(), h = robot.getLength();
        Rectangle2D.Double body = new Rectangle2D.Double(-w/2, -h/2, w, h);
        g2.setColor(bodyColor);
        g2.fill(body);

        // --- Draw wheels ---
        double wheelW = 4.0, wheelH = 2.0;
        g2.setColor(wheelColor);
        g2.fill(new Rectangle2D.Double(w/2 - wheelW - 1,  h/2 - wheelH - 1, wheelW, wheelH)); // FL
        g2.fill(new Rectangle2D.Double(w/2 - wheelW - 1, -h/2 + 1,           wheelW, wheelH)); // FR
        g2.fill(new Rectangle2D.Double(-w/2 + 1,         h/2 - wheelH - 1,  wheelW, wheelH)); // RL
        g2.fill(new Rectangle2D.Double(-w/2 + 1,        -h/2 + 1,           wheelW, wheelH)); // RR

        // --- Direction marker ---
        double markerLen = h / 3.0, markerWid = w * 0.05;
        Rectangle2D.Double marker = new Rectangle2D.Double(markerLen/2, -markerWid/2, markerLen, markerWid);
        g2.setColor(wheelColor);
        g2.fill(marker);

        // --- Outline ---
        g2.setStroke(new BasicStroke((float)(1/pixelsPerInch)));
        g2.draw(body);
        g2.dispose();
    }
}
