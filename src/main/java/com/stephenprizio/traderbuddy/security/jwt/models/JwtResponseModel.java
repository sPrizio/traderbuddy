package com.stephenprizio.traderbuddy.security.jwt.models;

import com.stephenprizio.traderbuddy.security.ResponseModel;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Jwt implementation of {@link ResponseModel}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record JwtResponseModel(@Getter String token) implements ResponseModel, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
}