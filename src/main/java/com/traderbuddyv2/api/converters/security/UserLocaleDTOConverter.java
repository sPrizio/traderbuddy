package com.traderbuddyv2.api.converters.security;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.security.UserLocaleDTO;
import com.traderbuddyv2.core.enums.system.Language;
import com.traderbuddyv2.core.models.entities.security.UserLocale;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter that converts {@link UserLocale}s into {@link UserLocaleDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("userLocaleDTOConverter")
public class UserLocaleDTOConverter implements GenericDTOConverter<UserLocale, UserLocaleDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public UserLocaleDTO convert(final UserLocale entity) {

        if (entity == null) {
            return new UserLocaleDTO();
        }

        final UserLocaleDTO userLocaleDTO = new UserLocaleDTO();

        userLocaleDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        userLocaleDTO.setLanguages(entity.getLanguages().stream().map(Language::getLabel).toList());
        userLocaleDTO.setCountry(entity.getCountry().getLabel());
        userLocaleDTO.setTownCity(entity.getTownCity());
        userLocaleDTO.setTimeZoneOffset(entity.getTimeZoneOffset());

        return userLocaleDTO;
    }
}
