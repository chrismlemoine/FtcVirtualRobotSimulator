package com.chrislemoine.simulator.ui;

/**
 * Enumeration of available FTC field backgrounds.
 * <p>Each constant corresponds to a PNG file under <code>/resources/background/</code>.<p>
 */
public enum Background {
    /** Dark theme from the 'Into the Deep' 2024 season. */
    FIELD_INTO_THE_DEEP_JUICE_DARK("season-2024-intothedeep/field-2024-juice-dark.png"),

    /** Light theme from the 'Into the Deep' 2024 season. */
    FIELD_INTO_THE_DEEP_JUICE_LIGHT("season-2024-intothedeep/field-2024-juice-light.png");

    private final String filename;

    /**
     * @param filename relative path under /background/ for resource loading
     */
    Background(String filename) {
        this.filename = filename;
    }

    /**
     * @return the filename to load from the /background/ resources directory
     */
    public String getFilename() {
        return filename;
    }
}