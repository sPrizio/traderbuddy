package com.traderbuddyv2.api.converters.system;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.system.PhoneNumberDTO;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.system.PhoneNumber;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converter that converts {@link PhoneNumber}s into {@link PhoneNumberDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("phoneNumberDTOConverter")
public class PhoneNumberDTOConverter implements GenericDTOConverter<PhoneNumber, PhoneNumberDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public PhoneNumberDTO convert(final PhoneNumber entity) {

        if (entity == null) {
            return new PhoneNumberDTO();
        }

        final PhoneNumberDTO phoneNumberDTO = new PhoneNumberDTO();

        phoneNumberDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        phoneNumberDTO.setPhoneType(entity.getPhoneType().name());
        phoneNumberDTO.setTelephoneNumber(entity.getTelephoneNumber());
        phoneNumberDTO.setCountryCode(entity.getCountryCode());
        phoneNumberDTO.setDisplay(getDisplayString(entity));

        return phoneNumberDTO;
    }


    //  HELPERS

    /**
     * Displays a phone number string
     *
     * @param entity {@link PhoneNumber}
     * @return {@link String}
     */
    private String getDisplayString(final PhoneNumber entity) {

        final String num = "+" + entity.getCountryCode() + entity.getTelephoneNumber();
        final Pattern pattern = Pattern.compile(CoreConstants.PHONE_NUMBER_REGEX);
        final Matcher matcher = pattern.matcher(num);

        if (matcher.find()) {
            return "+" + matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3) + " " + matcher.group(4);
        }

        return StringUtils.EMPTY;
    }
}
