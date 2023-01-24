package com.traderbuddyv2.api.converters.account;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.account.AccountBalanceModificationDTO;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter that converts {@link AccountBalanceModification}s into {@link AccountBalanceModificationDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("accountBalanceModificationDTOConverter")
public class AccountBalanceModificationDTOConverter implements GenericDTOConverter<AccountBalanceModification, AccountBalanceModificationDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public AccountBalanceModificationDTO convert(final AccountBalanceModification entity) {

        if (entity == null) {
            return new AccountBalanceModificationDTO();
        }

        final AccountBalanceModificationDTO accountBalanceModificationDTO = new AccountBalanceModificationDTO();

        accountBalanceModificationDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        accountBalanceModificationDTO.setModificationType(entity.getModificationType().getDescription());
        accountBalanceModificationDTO.setAmount(entity.getAmount());
        accountBalanceModificationDTO.setDateTime(entity.getDateTime());
        accountBalanceModificationDTO.setProcessed(entity.isProcessed());
        accountBalanceModificationDTO.setDescription(entity.getDescription());

        return accountBalanceModificationDTO;
    }
}
