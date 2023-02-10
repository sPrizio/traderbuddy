package com.traderbuddyv2.api.models.dto.system;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.system.PhoneNumber;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link PhoneNumber}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class PhoneNumberDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private String phoneType;

    @Getter
    @Setter
    private short countryCode;

    @Getter
    @Setter
    private long telephoneNumber;

    @Getter
    @Setter
    private String display;
}
