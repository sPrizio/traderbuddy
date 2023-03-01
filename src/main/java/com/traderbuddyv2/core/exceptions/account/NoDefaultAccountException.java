package com.traderbuddyv2.core.exceptions.account;

import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.security.User;

/**
 * Exception thrown when a {@link User} does not have a default {@link Account}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class NoDefaultAccountException extends RuntimeException {

    public NoDefaultAccountException(final String message) {
        super(message);
    }

    public NoDefaultAccountException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
