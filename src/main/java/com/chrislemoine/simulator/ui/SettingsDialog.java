package com.chrislemoine.simulator.ui;

import com.chrislemoine.simulator.core.DriveType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Modal dialog for selecting simulation settings:
 * <p> - Drive type (MECANUM or TANK)</p>
 * <p> - Field background (Dark or Light)</p>
 * <p> - Alliance color (Red or Blue)</p>
 *
 * Blocks until the user clicks "Continue," then exposes the selections via getters.
 */
public class SettingsDialog extends JDialog {
    private DriveType selectedDrive = DriveType.MECANUM;
    private Background selectedBackground = Background.FIELD_INTO_THE_DEEP_JUICE_DARK;
    private Alliance selectedAlliance = Alliance.RED;

    /**
     * Constructs and displays a modal settings dialog.
     * @param parent the parent JFrame for centering
     */
    public SettingsDialog(JFrame parent) {
        super(parent, "Simulator Settings", true);
        setLayout(new BorderLayout());
        setSize(500, 150);
        setLocationRelativeTo(parent);


        // --- DRIVE TYPE PANEL ---
        JRadioButton mecBtn = new JRadioButton("Mecanum Drive", true);
        JRadioButton tankBtn = new JRadioButton("Tank Drive");
        ButtonGroup driveGroup = new ButtonGroup();
        driveGroup.add(mecBtn);
        driveGroup.add(tankBtn);
        JPanel drivePanel = new JPanel(new GridLayout(2, 1));
        drivePanel.setBorder(BorderFactory.createTitledBorder("Drive Type"));
        drivePanel.add(mecBtn);
        drivePanel.add(tankBtn);

        // --- BACKGROUND OPTIONS ---
        JRadioButton darkBtn = new JRadioButton("Juice Dark", true);
        JRadioButton lightBtn = new JRadioButton("Juice light");
        ButtonGroup bgGroup = new ButtonGroup();
        bgGroup.add(darkBtn);
        bgGroup.add(lightBtn);
        JPanel bgPanel = new JPanel(new GridLayout(2, 1));
        bgPanel.setBorder(BorderFactory.createTitledBorder("Field Background"));
        bgPanel.add(darkBtn);
        bgPanel.add(lightBtn);

        // --- ALLIANCE PANEL ---
        JRadioButton redBtn = new JRadioButton("Red Alliance", true);
        JRadioButton blueBtn = new JRadioButton("Blue Alliance");
        ButtonGroup allianceGroup = new ButtonGroup();
        allianceGroup.add(redBtn);
        allianceGroup.add(blueBtn);
        JPanel alliancePanel = new JPanel(new GridLayout(2, 1));
        alliancePanel.setBorder(BorderFactory.createTitledBorder("Alliance"));
        alliancePanel.add(redBtn);
        alliancePanel.add(blueBtn);

        // Combine both panels side by side
        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.add(drivePanel);
        center.add(bgPanel);
        center.add(alliancePanel);
        add(center, BorderLayout.CENTER);

        // --- CONTINUE BUTTON ---
        JButton ok = new JButton("Continue");
        ok.addActionListener((ActionEvent e) -> {
            selectedDrive = mecBtn.isSelected() ? DriveType.MECANUM : DriveType.TANK;
            selectedBackground = darkBtn.isSelected()
                    ? Background.FIELD_INTO_THE_DEEP_JUICE_DARK
                    : Background.FIELD_INTO_THE_DEEP_JUICE_LIGHT;
            selectedAlliance = redBtn.isSelected()
                    ? Alliance.RED
                    : Alliance.BlUE;
            setVisible(false);
        });
        JPanel south = new JPanel();
        south.add(ok);
        add(south, BorderLayout.SOUTH);
    }

    /** @return the drive type the user selected */
    public DriveType getSelectedDrive() {
        return selectedDrive;
    }
    /** @return the background the user selected */
    public Background getSelectedBackground() {
        return selectedBackground;
    }
    /** @return the selected alliance */
    public Alliance getSelectedAlliance() { return selectedAlliance; }
}
