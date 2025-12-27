package de.jarovart.freemoment.server.model.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_ROOT("ROLE_ROOT");

    private final String roleName;

    UserRole(String role) {
        this.roleName = role;
    }

}
