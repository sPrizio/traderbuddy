package com.stephenprizio.traderbuddy.models.records.json;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of a standard json response
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class StandardJsonResponse {

    @Getter
    @Setter
    private boolean success;

    @Getter
    @Setter
    private Object data;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String internalMessage;


    //  CONSTRUCTORS

    public StandardJsonResponse(final boolean success, final Object data, final String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.internalMessage = StringUtils.EMPTY;
    }

    public StandardJsonResponse(final boolean success, Object data, final String message, final String internalMessage) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.internalMessage = internalMessage;
    }
}
