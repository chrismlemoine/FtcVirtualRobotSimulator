package com.chrislemoine.simulator.core;

import javax.swing.Timer;

import com.chrislemoine.simulator.input.KeyboardController;
import com.chrislemoine.simulator.ui.DriveMode;
import com.chrislemoine.simulator.ui.FieldPanel;

/**
 * Manages the update-render loop at ~60 Hz.
 */
public class Simulator {
    private final SimBot bot;
    private final FieldPanel panel;
    private final KeyboardController controller;
    private final DriveMode driveMode;

    /**
     * @param bot   the robot model to update each frame
     * @param panel the panel to repaint each frame
     */
    public Simulator(SimBot bot, FieldPanel panel, KeyboardController controller, DriveMode driveMode) {
        this.bot   = bot;
        this.panel = panel;
        this.controller = controller;
        this.driveMode = driveMode;
    }

    /**
     * Starts the Swing timer that ticks every 16 ms.
     */
    public void start() {
        new Timer(16, e -> {
            double dt = 0.016;   // seconds
            controller.poll();

            double maxV = bot.getMaxVel();
            double maxA = bot.getMaxAngVel();

            bot.setTargetVel(
                    controller.getLateral() * maxV,
                    controller.getAxial()   * maxV
            );
            bot.setTargetAngVel(
                    controller.getYaw() * maxA
            );

            bot.update(dt);
            panel.repaint();
        }).start();
    }
}