package de.jarovart.freemoment.server.model.enums;

public enum LocationType {
    CREATED,
    JOINED,
    LIKED;

    public static LocationType from(String value) {
        return LocationType.valueOf(value.toUpperCase());
    }
}
