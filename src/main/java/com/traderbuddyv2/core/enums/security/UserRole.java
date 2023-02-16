package com.traderbuddyv2.core.enums.security;

import com.traderbuddyv2.core.models.entities.security.User;
import lombok.Getter;

/**
 * Enumeration of a various roles that a {@link User} may possess
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum UserRole {
    ADMINISTRATOR("Admin"),
    TRADER("Trader");

    @Getter
    private final String label;

    UserRole(final String label) {
        this.label = label;
    }
}
