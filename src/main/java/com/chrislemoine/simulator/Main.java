package com.chrislemoine.simulator;

import com.chrislemoine.simulator.core.SimBot;
import com.chrislemoine.simulator.core.SimBotBuilder;
import com.chrislemoine.simulator.core.Simulator;
import com.chrislemoine.simulator.input.DriveController;
import com.chrislemoine.simulator.input.KeyboardController;
import com.chrislemoine.simulator.ui.Alliance;
import com.chrislemoine.simulator.ui.Background;
import com.chrislemoine.simulator.ui.DriveMode;
import com.chrislemoine.simulator.ui.FieldPanel;
import com.chrislemoine.simulator.ui.SettingsDialog;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.Insets;
import java.awt.image.BufferedImage;

/**
 * Entry point for the FTC Virtual Robot Simulator.
 * <p>
 * Responsible for:
 * <ul>
 *   <li>Launching the settings dialog</li>
 *   <li>Configuring window dimensions</li>
 *   <li>Computing physics-based drive limits</li>
 *   <li>Initializing input controllers (gamepad + keyboard)</li>
 *   <li>Starting the simulation loop</li>
 * </ul>
 * </p>
 */
public class Main {
    public static void main(String[] args) {
        // --- Create main application window ---
        JFrame frame = new JFrame("FTC Virtual Robot Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // --- Show settings dialog and capture user choices ---
        SettingsDialog settings = new SettingsDialog(frame);
        settings.setVisible(true);

        DriveMode driveMode       = settings.getSelectedDriveMode();
        Background background     = settings.getSelectedBackground();
        Alliance alliance         = settings.getSelectedAlliance();

        // --- Determine window size based on background aspect ratio ---
        final int baseHeight = 800;
        int contentWidth = baseHeight;
        try {
            BufferedImage bgImg = ImageIO.read(
                    Main.class.getResource("/background/" + background.getFilename())
            );
            if (bgImg != null) {
                double aspect = (double) bgImg.getWidth() / bgImg.getHeight();
                contentWidth = (int)(baseHeight * aspect);
            }
        } catch (Exception e) {
            System.err.println("Warning: failed to load background for sizing: " + e.getMessage());
        }
        Insets insets = frame.getInsets();
        frame.setSize(
                contentWidth + insets.left + insets.right,
                baseHeight    + insets.top  + insets.bottom
        );
        frame.setLocationRelativeTo(null);

        // --- Compute realistic drive constraints from hardware specs ---
        double wheelRadiusIn = 2.0;      // inches
        int motorCount       = 4;
        double stallTorque   = 2.69;     // N·m per motor
        double robotMass     = 18.0;     // kg
        double m2in          = 39.37;    // meters → inches

        double totalTorque   = stallTorque * motorCount;
        double wheelForceN   = totalTorque / (wheelRadiusIn / m2in);
        double realLinAccel  = Math.min((wheelForceN / robotMass) * m2in, 450.0);

        double maxLinearSpeed = 60.0;          // inches/sec
        double halfTrackIn    = 17.25 / 2.0;    // inches
        double realYawSpeed   = maxLinearSpeed / halfTrackIn;
        double realYawAccel   = Math.min(realLinAccel / halfTrackIn, 8.0);

        // --- Build the robot model ---
        SimBot robot = new SimBotBuilder()
                .setStartPose(0, 0, Math.PI / 2)
                .setConstraints(
                        maxLinearSpeed,    // max linear speed
                        realLinAccel,      // max linear accel
                        realYawSpeed,      // max rotational speed
                        realYawAccel       // max rotational accel
                )
                .setBrakeFactors(5, 5)
                .setDimensions(17.25, 17.25)
                .build();

        // --- Initialize input controllers ---
        KeyboardController keyboard = new KeyboardController();

        // Register keyboard listener on the frame
        frame.addKeyListener(keyboard);

        // --- Set up rendering panel ---
        FieldPanel panel = new FieldPanel(robot, background, alliance);
        frame.add(panel);
        frame.setVisible(true);

        // --- Start the simulation loop ---
        Simulator simulator = new Simulator(robot, panel, keyboard, driveMode);
        simulator.start();
    }
}