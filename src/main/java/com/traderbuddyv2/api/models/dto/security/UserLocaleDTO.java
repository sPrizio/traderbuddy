package com.traderbuddyv2.api.models.dto.security;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.security.UserLocale;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * A DTO representation of a {@link UserLocale}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class UserLocaleDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private String townCity;

    @Getter
    @Setter
    private String country;

    @Getter
    @Setter
    private List<String> languages;

    @Getter
    @Setter
    private String timeZoneOffset;
}
