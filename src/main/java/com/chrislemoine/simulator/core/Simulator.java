package com.chrislemoine.simulator.core;

import javax.swing.Timer;

import com.chrislemoine.simulator.input.KeyboardController;
import com.chrislemoine.simulator.ui.DriveMode;
import com.chrislemoine.simulator.ui.FieldPanel;

/**
 * Manages the input, per-axis braking, and update-render loop at ~60 Hz.
 */
public class Simulator {
    private final SimBot bot;
    private final FieldPanel panel;
    private final KeyboardController controller;
    private final DriveMode driveMode;

    /**
     * @param bot         the robot model to update each frame
     * @param panel       the UI panel to repaint each frame
     * @param controller  provides axial, lateral, and yaw inputs
     * @param driveMode   robot- or field-centric driving
     */
    public Simulator(SimBot bot, FieldPanel panel, KeyboardController controller, DriveMode driveMode) {
        this.bot = bot;
        this.panel = panel;
        this.controller = controller;
        this.driveMode = driveMode;
    }

    /**
     * Starts the Swing timer ticking every ~16 ms (~60Hz), polls inputs,
     * maps them to motion targets with individual-axis braking, updates
     * the robot state, and repaints the panel.
     */
    public void start() {
        new Timer(16, e -> {
            double dt = 0.016;   // seconds
            controller.poll();

            double rawAxial   = controller.getAxial();   // +1 forward, -1 reverse
            double rawLateral = controller.getLateral(); // +1 right,  -1 left
            double rawYaw     = controller.getYaw();     // +1 CCW,   -1 CW

            double maxSpeed    = bot.getMaxSpeed();       // scalar speed limit
            double maxYawSpeed = bot.getMaxYawSpeed();    // scalar yaw speed limit

            double targetAxial, targetLateral;

            if (driveMode == DriveMode.ROBOT_CENTRIC) {
                // Robot-centric: direct mapping from controller axes
                targetAxial   = rawAxial   * maxSpeed;
                targetLateral = rawLateral * maxSpeed;
            } else {
                // Field-centric: convert field inputs into robot frame
                double fieldAxial   = rawAxial   * maxSpeed;   // world forward
                double fieldLateral = rawLateral * maxSpeed;   // world right
                double H = bot.getHeading();
                double cosH = Math.cos(H), sinH = Math.sin(H);

                // Inverse rotation: world->robot axes
                targetAxial   =  fieldLateral * cosH + fieldAxial * sinH;
                targetLateral = -fieldLateral * sinH + fieldAxial * cosH;
            }

            // Set per-axis targets; zero input ramps down according to accel limits
            bot.setTargetVel(targetAxial, targetLateral);
            bot.setTargetYawVel(rawYaw * maxYawSpeed);

            // Integrate physics and repaint
            bot.update(dt);
            panel.repaint();
        }).start();
    }
}