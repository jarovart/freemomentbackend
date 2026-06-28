package de.jarovart.freemoment.server.model.enums;

public enum AppDesignEnum {
    SYSTEM,
    LIGHTBLACK,
    LIGHTROSE,
    LIGHTWINE,
    DARKWHITE,
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
