package com.traderbuddyv2.api.models.dto.account;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.dto.levelling.rank.RankDTO;
import com.traderbuddyv2.api.models.dto.levelling.skill.SkillDTO;
import com.traderbuddyv2.api.models.records.CurrencyDisplay;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation for {@link Account}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class AccountDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private LocalDateTime accountOpenTime;

    @Getter
    @Setter
    private double balance;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private long accountNumber;

    @Getter
    @Setter
    private CurrencyDisplay currency;

    @Getter
    @Setter
    private String broker;

    @Getter
    @Setter
    private String accountType;

    @Getter
    @Setter
    private LocalDateTime lastTraded;

    @Getter
    @Setter
    private SkillDTO skill;

    @Getter
    @Setter
    private RankDTO rank;
}
