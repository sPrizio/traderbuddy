package com.stephenprizio.traderbuddy.models.dto.security;

import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import javax.persistence.Column;

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
    @Column
    private String firstName;

    @Getter
    @Setter
    @Column
    private String lastName;

    @Getter
    @Setter
    @Column
    private String username;

    @Getter
    @Setter
    @Column
    private String email;
}
