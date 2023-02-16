package com.traderbuddyv2.api.models.records.json;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of a standard json response
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record StandardJsonResponse(
        @Getter
        boolean success,
        @Getter
        Object data,
        @Getter
        String message,
        @Getter
        String internalMessage
) {

    public StandardJsonResponse(final boolean success, final Object data, final String message) {
        this(success, data, message, StringUtils.EMPTY);
    }
}
