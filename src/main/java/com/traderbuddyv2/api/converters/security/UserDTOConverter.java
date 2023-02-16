package com.traderbuddyv2.api.converters.security;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.converters.system.PhoneNumberDTOConverter;
import com.traderbuddyv2.api.models.dto.security.UserDTO;
import com.traderbuddyv2.api.models.dto.system.PhoneNumberDTO;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converts {@link User}s into {@link UserDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("userDTOConverter")
public class UserDTOConverter implements GenericDTOConverter<User, UserDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "phoneNumberDTOConverter")
    private PhoneNumberDTOConverter phoneNumberDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;

    @Resource(name = "userLocaleDTOConverter")
    private UserLocaleDTOConverter userLocaleDTOConverter;


    //  METHODS

    @Override
    public UserDTO convert(final User entity) {

        if (entity == null) {
            return new UserDTO();
        }

        final UserDTO userDTO = new UserDTO();

        userDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        userDTO.setEmail(entity.getEmail());
        userDTO.setUsername(entity.getUsername());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setLastName(entity.getLastName());
        userDTO.setUserLocale(this.userLocaleDTOConverter.convert(entity.getUserLocale()));
        userDTO.setPhoneNumber(this.phoneNumberDTOConverter.convert(entity.getPhone()));
        userDTO.setAccount(this.accountDTOConverter.convert(entity.getAccount()));

        return userDTO;
    }
}
