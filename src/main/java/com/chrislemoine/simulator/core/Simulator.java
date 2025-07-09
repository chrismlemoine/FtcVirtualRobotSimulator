package com.chrislemoine.simulator.core;

import javax.swing.Timer;
import com.chrislemoine.simulator.ui.FieldPanel;

/**
 * Manages the update-render loop at ~60 Hz.
 */
public class Simulator {
    private final SimBot bot;
    private final FieldPanel panel;

    /**
     * @param bot   the robot model to update each frame
     * @param panel the panel to repaint each frame
     */
    public Simulator(SimBot bot, FieldPanel panel) {
        this.bot   = bot;
        this.panel = panel;
    }

    /**
     * Starts the Swing timer that ticks every 16 ms.
     */
    public void start() {
        new Timer(16, e -> {
            double dt = 0.016;   // seconds
            bot.update(dt);
            panel.repaint();
        }).start();
    }
}