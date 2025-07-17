package com.chrislemoine.simulator.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Settings dialog presented before simulation launch.
 * <ul>
 *   <li>Select <b>Drive Mode</b>: ROBOT_CENTRIC or FIELD_CENTRIC</li>
 *   <li>Select <b>Field Background</b>: dark or light theme</li>
 *   <li>Select <b>Alliance</b>: RED or BLUE</li>
 * </ul>
 * Blocks until the user confirms, then getters expose selections.
 */
public class SettingsDialog extends JDialog {
    private DriveMode selectedDriveMode = DriveMode.ROBOT_CENTRIC;
    private Background selectedBackground = Background.FIELD_INTO_THE_DEEP_JUICE_DARK;
    private Alliance selectedAlliance = Alliance.RED;

    /**
     * Constructs and displays the modal settings dialog.
     * @param parent parent frame for centering and modality
     */
    public SettingsDialog(JFrame parent) {
        super(parent, "Simulator Settings", true);
        setLayout(new BorderLayout(10, 10));
        setSize(600, 200);
        setLocationRelativeTo(parent);

        // --- Drive Mode Selection ---
        JRadioButton robotBtn = new JRadioButton("Robot-centric", true);
        JRadioButton fieldBtn = new JRadioButton("Field-centric");
        ButtonGroup driveGroup = new ButtonGroup();
        driveGroup.add(robotBtn);
        driveGroup.add(fieldBtn);
        JPanel drivePanel = new JPanel(new GridLayout(2, 1));
        drivePanel.setBorder(BorderFactory.createTitledBorder("Drive Mode"));
        drivePanel.add(robotBtn);
        drivePanel.add(fieldBtn);

        // --- Field Background Selection ---
        JRadioButton darkBtn = new JRadioButton("Juice Dark", true);
        JRadioButton lightBtn = new JRadioButton("Juice Light");
        ButtonGroup bgGroup = new ButtonGroup();
        bgGroup.add(darkBtn);
        bgGroup.add(lightBtn);
        JPanel bgPanel = new JPanel(new GridLayout(2, 1));
        bgPanel.setBorder(BorderFactory.createTitledBorder("Field Background"));
        bgPanel.add(darkBtn);
        bgPanel.add(lightBtn);

        // --- Alliance Selection ---
        JRadioButton redBtn = new JRadioButton("Red Alliance", true);
        JRadioButton blueBtn = new JRadioButton("Blue Alliance");
        ButtonGroup allianceGroup = new ButtonGroup();
        allianceGroup.add(redBtn);
        allianceGroup.add(blueBtn);
        JPanel alliancePanel = new JPanel(new GridLayout(2, 1));
        alliancePanel.setBorder(BorderFactory.createTitledBorder("Alliance"));
        alliancePanel.add(redBtn);
        alliancePanel.add(blueBtn);

        // --- Assemble center panels in a row ---
        JPanel center = new JPanel(new GridLayout(1, 3, 20, 10));
        center.add(drivePanel);
        center.add(bgPanel);
        center.add(alliancePanel);
        add(center, BorderLayout.CENTER);

        // --- Continue Button ---
        JButton okButton = new JButton("Continue");
        okButton.addActionListener((ActionEvent e) -> {
            // Capture selections
            selectedDriveMode = robotBtn.isSelected()
                    ? DriveMode.ROBOT_CENTRIC
                    : DriveMode.FIELD_CENTRIC;
            selectedBackground = darkBtn.isSelected()
                    ? Background.FIELD_INTO_THE_DEEP_JUICE_DARK
                    : Background.FIELD_INTO_THE_DEEP_JUICE_LIGHT;
            selectedAlliance = redBtn.isSelected()
                    ? Alliance.RED
                    : Alliance.BLUE;
            setVisible(false);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /** @return the drive mode chosen by the user */
    public DriveMode getSelectedDriveMode() {
        return selectedDriveMode;
    }

    /** @return the field background chosen by the user */
    public Background getSelectedBackground() {
        return selectedBackground;
    }

    /** @return the alliance chosen by the user */
    public Alliance getSelectedAlliance() {
        return selectedAlliance;
    }
}