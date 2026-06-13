package de.jarovart.freemoment.server.model.enums;

public enum AppDesignEnum {
    SYSTEM,
    LIGHTROSE,
    DARK,
    DARKPINK,
    DARKGOLD,
    CUSTOM;

    public static AppDesignEnum from(String value) {
        if (value == null) {
            return SYSTEM;
        }
        return AppDesignEnum.valueOf(value.toUpperCase());
    }
}
