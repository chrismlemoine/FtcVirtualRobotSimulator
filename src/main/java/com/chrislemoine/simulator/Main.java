package com.chrislemoine.simulator;

import com.chrislemoine.simulator.ui.DriveMode;
import com.chrislemoine.simulator.core.SimBot;
import com.chrislemoine.simulator.core.SimBotBuilder;
import com.chrislemoine.simulator.core.Simulator;
import com.chrislemoine.simulator.input.KeyboardController;
import com.chrislemoine.simulator.ui.Alliance;
import com.chrislemoine.simulator.ui.Background;
import com.chrislemoine.simulator.ui.FieldPanel;
import com.chrislemoine.simulator.ui.SettingsDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Entry point for the FTC Virtual Robot Simulator.
 * <p>Creates the main window, shows the settings dialog,
 * initializes the robot, input, rendering, and starts the simulation loop.</p>
 */
public class Main {
    public static void main(String[] args) {
        // --- Window setup ---
        JFrame frame = new JFrame("FTC Virtual Robot Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // --- Settings dialog ---
        SettingsDialog settings = new SettingsDialog(frame);
        settings.setVisible(true);
        DriveMode driveMode       = settings.getSelectedDriveMode();
        Background background     = settings.getSelectedBackground();
        Alliance alliance         = settings.getSelectedAlliance();

        // --- Determine content size based on background aspect ratio ---
        int contentHeight = 800;
        int contentWidth  = contentHeight;
        try {
            BufferedImage bgImage = ImageIO.read(
                    Main.class.getResource("/background/" + background.getFilename())
            );
            if (bgImage != null) {
                double aspect = (double) bgImage.getWidth() / bgImage.getHeight();
                contentWidth = (int) (contentHeight * aspect);
            }
        } catch (Exception e) {
            System.err.println("Warning: could not load background for sizing: " + e.getMessage());
        }
        // Account for window borders and title bar
        Insets insets = frame.getInsets();
        frame.setSize(
                contentWidth + insets.left + insets.right,
                contentHeight + insets.top + insets.bottom
        );
        frame.setLocationRelativeTo(null);

        // --- Build robot model ---
        SimBot robot = new SimBotBuilder()
                .setStartPose(0, 0, Math.PI / 2)
                .setConstraints(60, 60, Math.PI, Math.PI)
                .setDimensions(17.25, 17.25)
                .build();

        // --- Input controller ---
        KeyboardController keyboardController = new KeyboardController(robot);
        frame.addKeyListener(keyboardController);
        frame.setFocusable(true);
        frame.requestFocusInWindow();

        // --- Rendering panel ---
        FieldPanel panel = new FieldPanel(robot, background, alliance);
        frame.add(panel);
        frame.setVisible(true);

        // --- Start simulation loop ---
        Simulator simulator = new Simulator(robot, panel, keyboardController, driveMode);
        simulator.start();
    }
}