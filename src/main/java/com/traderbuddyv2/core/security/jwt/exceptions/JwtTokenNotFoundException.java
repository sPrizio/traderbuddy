package com.traderbuddyv2.core.security.jwt.exceptions;

import javax.servlet.http.HttpServletRequest;

/**
 * An exception used when a Jwt Token was not found within a {@link HttpServletRequest}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class JwtTokenNotFoundException extends RuntimeException {

    public JwtTokenNotFoundException(final String message) {
        super(message);
    }
}
