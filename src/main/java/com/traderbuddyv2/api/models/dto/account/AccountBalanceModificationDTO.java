package com.traderbuddyv2.api.models.dto.account;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation of a {@link AccountBalanceModification}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class AccountBalanceModificationDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private LocalDateTime dateTime;

    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    private String modificationType;

    @Getter
    @Setter
    private boolean processed;

    @Getter
    @Setter
    private String description;
}
