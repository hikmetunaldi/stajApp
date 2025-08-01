package com.personalApp.shared_model.enums;

import lombok.Getter;

@Getter
public enum ChangeType {
    CREATE(0),
    UPDATE(1),
    DELETE(2);

    private final int value;

    ChangeType(int value) {
        this.value = value;
    }

    public static ChangeType fromValue(int value) {
        for (ChangeType type : ChangeType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid change type" + value);
    }
}
