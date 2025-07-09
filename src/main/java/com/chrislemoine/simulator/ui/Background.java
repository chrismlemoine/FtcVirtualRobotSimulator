package com.chrislemoine.simulator.ui;

/**
 * Available FTC field background themes.
 * Each constant maps to a PNG under /resources/background/.
 */
public enum Background {
    /** Last  */
    FIELD_INTO_THE_DEEP_JUICE_DARK("season-2024-intothedeep/field-2024-juice-dark.png"),

    FIELD_INTO_THE_DEEP_JUICE_LIGHT("season-2024-intothedeep/field-2024-juice-light.png");

    private final String filename;

    Background(String filename) {
        this.filename = filename;
    }

    /** @return the file name in /background/ to load for this theme */
    public String getFilename() {
        return filename;
    }
}
