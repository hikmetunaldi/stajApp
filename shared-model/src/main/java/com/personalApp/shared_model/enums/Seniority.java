package com.personalApp.shared_model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Seniority {
    JUNIOR(0),
    MID(1),
    SENIOR(2),
    LEAD(3),
    MANAGER(4);

    private final int value;

    Seniority(int value) {
        this.value = value;
    }

    public static Seniority fromValue(int value) {
        for (Seniority s : Seniority.values()) {
            if (s.getValue() == value) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid seniority value: " + value);
    }

    @JsonCreator
    public static Seniority fromString(String key) {
        return Arrays.stream(Seniority.values())
                .filter(e -> e.name().equalsIgnoreCase(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid seniority value: " + key));
    }
}
