package com.traderbuddyv2.api.models.dto.account;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.dto.levelling.rank.RankDTO;
import com.traderbuddyv2.api.models.dto.levelling.skill.SkillDTO;
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
    private SkillDTO skill;

    @Getter
    @Setter
    private RankDTO rank;
}
