package com.traderbuddyv2.api.models.dto.security;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

/**
 * A DTO representation of a {@link User}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class UserDTO implements GenericDTO {

    @Getter
    @Setter
    public String uid;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private AccountDTO account;
}
