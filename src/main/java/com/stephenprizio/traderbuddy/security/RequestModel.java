package com.stephenprizio.traderbuddy.security;

/**
 * A generic request model that security implementations should use
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface RequestModel {

    /**
     * Get the username
     *
     * @return {@link String}
     */
    String getUsername();

    /**
     * Get the password
     *
     * @return {@link String}
     */
    String getPassword();
}
