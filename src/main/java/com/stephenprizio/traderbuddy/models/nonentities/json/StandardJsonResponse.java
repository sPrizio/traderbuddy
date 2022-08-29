package com.stephenprizio.traderbuddy.models.nonentities.json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Class representation of a standard json response
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class StandardJsonResponse {

    @Getter
    @Setter
    private Boolean success;

    @Getter
    @Setter
    private Object data;

    @Getter
    @Setter
    private String message;
}
