package com.chrislemoine.simulator;

import com.chrislemoine.simulator.core.DriveType;
import com.chrislemoine.simulator.core.SimBot;
import com.chrislemoine.simulator.core.SimBotBuilder;
import com.chrislemoine.simulator.core.Simulator;
import com.chrislemoine.simulator.ui.Alliance;
import com.chrislemoine.simulator.ui.Background;
import com.chrislemoine.simulator.ui.FieldPanel;
import com.chrislemoine.simulator.ui.SettingsDialog;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] args) {
        // Create and configure the main window
        JFrame frame = new JFrame("FTC Virtual Robot Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        // Show settings dialog
        SettingsDialog settings = new SettingsDialog(frame);
        settings.setVisible(true);

        DriveType driveType   = settings.getSelectedDrive();
        Background background = settings.getSelectedBackground();
        Alliance alliance     = settings.getSelectedAlliance();

        // Load the selected background so we can get its dimensions
        BufferedImage bgImage = null;
        try {
            bgImage = ImageIO.read(
                    Main.class.getResource("/Background/" + background.getFilename())
            );
        } catch (Exception e) {
            System.err.println("Could not load background for sizing: " +e.getMessage());
        }

        if (bgImage != null) {
            double imgAspect = (double) bgImage.getHeight() / bgImage.getHeight();
            int    contentH  = 800;
            int    contentW  = (int)(contentH * imgAspect);

            // Account for the window boarders & title bar
            Insets insets = frame.getInsets();
            frame.setSize(
                    contentW + insets.left + insets.right,
                    contentH + insets.top + insets.bottom
            );
        } else {
            frame.setSize(800, 800);
        }
        frame.setLocationRelativeTo(null);

        // Build your robot model
        SimBot robot = new SimBotBuilder()
                .setStartPose(0, 0, Math.PI / 2)
                .setConstraints(60, 60, Math.PI, Math.PI)
                .setDimensions(17.25, 17.25)
                .build();

        // 4) Set up the rendering panel with the chosen background
        FieldPanel panel = new FieldPanel(robot, background, alliance);
        frame.add(panel);
        frame.setVisible(true);

        // 5) Kick off the simulation loop
        new Simulator(robot, panel).start();
    }
}
