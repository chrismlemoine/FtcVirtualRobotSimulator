package sim.ui;

import sim.core.DriveType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Model dialog for collecting pre-simulation settings from the user
 * <p>
 *     Presents drive-train type options (Mecanum or Tank) and waits for the user to confirm before launching the
 *     simulator.
 */
public class SettingsDialog extends JDialog {
    private DriveType selectedDrive = DriveType.MECANUM;

    /**
     * Constructs the settings dialog
     * @param parent the parent frame used for centering the dialog
     */
    public SettingsDialog(JFrame parent) {
        super(parent, "Settings", true);
        setLayout(new BorderLayout());
        setSize(300, 150);
        setLocationRelativeTo(parent);

        // Radio buttons for selecting drive type
        JRadioButton mecanumButton = new JRadioButton("Mecanum Drive", true);
        JRadioButton tankButton = new JRadioButton("Tank Drive");
        ButtonGroup group = new ButtonGroup();
        group.add(mecanumButton);
        group.add(tankButton);

        JPanel choices = new JPanel(new GridLayout(2,1));
        choices.add(mecanumButton);
        choices.add(tankButton);
        add(choices, BorderLayout.CENTER);

        // Continue button to close dialog and record selection
        JButton ok = new JButton("Continue");
        ok.addActionListener((ActionEvent e) -> {
            selectedDrive = mecanumButton.isSelected() ? DriveType.MECANUM : DriveType.TANK;
            setVisible(false);
        });
        JPanel south = new JPanel();
        south.add(ok);
        add(south, BorderLayout.SOUTH);
    }

    /**
     * Returns the drive-train type selected by the user.
     * @return the chosen DriveType value
     */
    public DriveType getSelectedDrive() {
        return selectedDrive;
    }
}