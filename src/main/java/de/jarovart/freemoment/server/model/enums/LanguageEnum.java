package de.jarovart.freemoment.server.model.enums;

public enum LanguageEnum {
    SYS,
    EN,
    DE;

    public static LanguageEnum from(String value) {
        if (value == null) {
            return null;
        }

        return LanguageEnum.valueOf(value.toUpperCase());
    }
}
