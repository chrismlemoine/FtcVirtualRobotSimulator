package com.chrislemoine.simulator.core;

import javax.swing.Timer;
import com.chrislemoine.simulator.input.DriveController;
import com.chrislemoine.simulator.ui.DriveMode;
import com.chrislemoine.simulator.ui.FieldPanel;

/**
 * Manages the main simulation loop at approximately 60Hz.
 * <p>
 * Each frame: polls the input controller, computes drive targets
 * in either robot-centric or field-centric mode, updates the
 * SimBot model, and repaints the FieldPanel.
 * </p>
 */
public class Simulator {
    private final SimBot bot;
    private final FieldPanel panel;
    private final DriveController controller;
    private final DriveMode driveMode;

    /**
     * @param bot         the robot model to update each frame
     * @param panel       the UI panel to repaint each frame
     * @param controller  provides axial/lateral/yaw inputs in [-1..1]
     * @param driveMode   whether to drive ROBOT_CENTRIC or FIELD_CENTRIC
     */
    public Simulator(SimBot bot,
                     FieldPanel panel,
                     DriveController controller,
                     DriveMode driveMode) {
        this.bot = bot;
        this.panel = panel;
        this.controller = controller;
        this.driveMode = driveMode;
    }

    /**
     * Starts a Swing timer at ~16ms intervals (~60FPS).
     */
    public void start() {
        int intervalMs = 16;
        double dt = intervalMs / 1000.0;

        new Timer(intervalMs, e -> {
            // 1) Poll inputs
            controller.poll();
            double axial   = controller.getAxial();   // +1 forward, -1 backward
            double lateral = controller.getLateral(); // +1 right,   -1 left
            double yaw     = controller.getYaw();     // +1 CCW,     -1 CW

            // 2) Compute targets
            double linLimit = bot.getMaxSpeed();
            double yawLimit = bot.getMaxYawSpeed();
            double targetAxial;
            double targetLateral;

            if (driveMode == DriveMode.ROBOT_CENTRIC) {
                // Direct mapping in robot frame
                targetAxial   = axial * linLimit;
                targetLateral = -lateral * linLimit; // invert for robot coords
            } else {
                // Field-centric: rotate world inputs into robot's frame
                double worldFwd   = axial * linLimit;
                double worldRight = lateral * linLimit;
                double h = bot.getHeading();
                double cosH = Math.cos(h), sinH = Math.sin(h);
                targetAxial   =  worldFwd * cosH + worldRight * sinH;
                targetLateral = -worldFwd * sinH + worldRight * cosH;
            }

            // 3) Apply targets to bot
            bot.setTargetAxial(targetAxial);
            bot.setTargetLateral(targetLateral);
            bot.setTargetYaw(yaw * yawLimit);

            // 4) Update model and repaint
            bot.update(dt);
            panel.repaint();
        }).start();
    }
}