package sim;

import sim.core.DriveType;
import sim.core.SimBot;
import sim.core.SimBotBuilder;
import sim.core.Simulator;
import sim.ui.FieldPanel;
import sim.ui.SettingsDialog;

import javax.swing.*;

/**
 * Entry point for the FTC Virtual Robot Simulator.
 * <p> - Creates the main application window </p>
 * <p> - Displays a model settings dialog to capture user preference </p>
 * <p> - Constructs and configures the SimBot instance </p>
 * <p> - Sets up the FieldPanel for rendering and starts the simulation loop </p>
 */
public class Main {
    public static void main(String[] args) {
        // Initialize the main window which will host the field view
        JFrame frame = new JFrame("FTC Virtual Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);

        // Display settings dialog before simulation starts
        SettingsDialog settingsDialog = new SettingsDialog(frame);
        settingsDialog.setVisible(true); // Blocks until user clicks Continue
        DriveType driveType = settingsDialog.getSelectedDrive();
        System.out.println("Drive type selected " + driveType);

        // Build and configure the robot model
        SimBot robot = new SimBotBuilder()
                .setStartPose(0, 0, Math.PI / 2) // Centered, facing positive Y
                .setConstraints(
                        60.0,   // max linear velocity (inches/sec)
                        60.0,           // max linear acceleration (inches/sec^2)
                        Math.PI,        // max angular velocity (radians/sec)
                        Math.PI         // max angular acceleration (radians/sec^2)
                )
                .setDimensions(
                        17.25,    // robot width in inches
                        17.25           // robot length in inches
                )
                .build();

        // Add the field view to the window and display it
        FieldPanel fieldPanel = new FieldPanel(robot);
        frame.add(fieldPanel);
        frame.setVisible(true);

        // Start the update-render loop
        Simulator simulator = new Simulator(robot, fieldPanel);
        simulator.start();
    }
}