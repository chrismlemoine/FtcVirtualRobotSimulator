package sim.core;

import sim.ui.FieldPanel;

import javax.swing.*;

/**
 * Manages the simulation update loop, invoking robot logic and repainting the view.
 * <p>
 *     Uses a Swing Timer to perform updates at approximately 60 Hz.
 */
public class Simulator {
    private final SimBot bot;
    private final FieldPanel fieldPanel;

    /**
     * @param bot           the robot model to update each frame
     * @param fieldPanel    the UI component to repaint each frame
     */
    public Simulator(SimBot bot, FieldPanel fieldPanel) {
        this.bot = bot;
        this.fieldPanel = fieldPanel;
    }

    /**
     * Starts the Swing timer that drives the update-render cycle.
     */
    public void start() {
        Timer timer = new Timer(16, e -> {
            double deltaTime = 0.016;   // seconds per frame (~60 FPS)
            bot.update(deltaTime);      // update robot state
            fieldPanel.repaint();       // redraw the field and robot
        });
        timer.start();
    }
}