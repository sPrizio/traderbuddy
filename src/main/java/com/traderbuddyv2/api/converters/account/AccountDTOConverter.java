package com.traderbuddyv2.api.converters.account;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.converters.levelling.rank.RankDTOConverter;
import com.traderbuddyv2.api.converters.levelling.skill.SkillDTOConverter;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converts {@link Account}s into {@link AccountDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("accountDTOConverter")
public class AccountDTOConverter implements GenericDTOConverter<Account, AccountDTO> {

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "rankDTOConverter")
    private RankDTOConverter rankDTOConverter;

    @Resource(name = "skillDTOConverter")
    private SkillDTOConverter skillDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public AccountDTO convert(final Account entity) {

        if (entity == null) {
            return new AccountDTO();
        }

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        accountDTO.setAccountOpenTime(entity.getAccountOpenTime());
        accountDTO.setActive(entity.isActive());
        accountDTO.setBalance(this.mathService.getDouble(entity.getBalance()));
        accountDTO.setSkill(this.skillDTOConverter.convert(entity.getSkill()));
        accountDTO.setRank(this.rankDTOConverter.convert(entity.getRank()));

        return accountDTO;
    }
}
