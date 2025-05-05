package org.example.wellness_hub.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityLevel {
    SEDENTARY("Sedentary"),
    LIGHTLY_ACTIVE("Lightly active"),
    MODERATELY_ACTIVE("Moderately active"),
    VERY_ACTIVE("Very active");

    private final String value;

    ActivityLevel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ActivityLevel fromString(String value) {
        for (ActivityLevel level : ActivityLevel.values()) {
            if (level.value.equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid ActivityLevel value: " + value);
    }
}
