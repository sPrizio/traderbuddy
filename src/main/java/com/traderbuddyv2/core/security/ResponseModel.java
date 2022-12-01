package com.traderbuddyv2.core.security;

/**
 * A generic response model that all security implementations should follow
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface ResponseModel {

    /**
     * Get the token
     *
     * @return {@link String}
     */
    String getToken();
}
