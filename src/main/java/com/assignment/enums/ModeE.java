package com.assignment.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ModeE {
    LAST3("Last3"),
    COUNT_HOLIDAYS("Count_Holidays"),
    COMMON_HOLIDAYS("Common_Holidays");

    private String label;

    public static ModeE fromString(String mode) {
        for (ModeE value : ModeE.values()) {
            if (value.label.equalsIgnoreCase(mode)) {
                return value;
            }
        }
        throw new IllegalArgumentException("Unknown mode '%s'. Valid modes are %s".format(mode, ModeE.values()));
    }
}
