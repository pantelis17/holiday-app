package com.assignment.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

/**
 * Enum representing the various modes in which the application can operate.
 */
@AllArgsConstructor
public enum ModeE {
    LAST3("Last3"),
    COUNT_HOLIDAYS("Count_Holidays"),
    COMMON_HOLIDAYS("Common_Holidays"),
    AVAILABLE_CODES("Available_Codes"),
    HELP("Help");

    private final String label;

    /**
     * Parses a string and returns the corresponding ModeE enum value.
     * 
     * @param mode the input string representing the mode
     * @return the matching ModeE value
     * @throws IllegalArgumentException if the input does not match any valid mode
     */
    public static ModeE fromString(String mode) {
        if (mode == null || mode.isBlank()) {
            throw new IllegalArgumentException("Mode cannot be null or blank.");
        }

        // Attempt to match the mode with defined labels (case-insensitive)
        for (final var value : ModeE.values()) {
            if (value.label.equalsIgnoreCase(mode)) {
                return value;
            }
        }

        // Collect valid modes for a user-friendly error message
        final var validModes = Arrays.stream(ModeE.values())
                .map(v -> v.label)
                .collect(Collectors.joining(", "));

        throw new IllegalArgumentException(
                String.format("Unknown mode '%s'. Valid modes are [%s]", mode, validModes));
    }

    /**
     * Returns the user-friendly label for this mode.
     */
    public String getLabel() {
        return label;
    }
}
