package com.traderbuddyv2.core.security.jwt.models;

import com.traderbuddyv2.core.security.RequestModel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Jwt implementation of {@link RequestModel}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class JwtRequestModel implements RequestModel, Serializable {

    @Serial
    private static final long serialVersionUID = 2636936156391265891L;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    public JwtRequestModel() {}

    public JwtRequestModel(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }
}
