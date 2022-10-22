package com.stephenprizio.traderbuddy.services.security;

import com.stephenprizio.traderbuddy.models.entities.security.User;
import com.stephenprizio.traderbuddy.repositories.security.UserRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link User}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("userService")
public class UserService {

    @Resource(name = "userRepository")
    private UserRepository userRepository;


    //  METHODS

    /**
     * Finds a {@link User} by their username
     *
     * @param username username
     * @return {@link Optional} {@link User}
     */
    public Optional<User> findUserByUsername(final String username) {
        validateParameterIsNotNull(username, "username cannot be null");
        return Optional.ofNullable(this.userRepository.findUserByUsername(username));
    }

    /**
     * Finds a {@link User} by their email
     *
     * @param email email
     * @return {@link Optional} {@link User}
     */
    public Optional<User> findUserByEmail(final String email) {
        validateParameterIsNotNull(email, "email cannot be null");
        return Optional.ofNullable(this.userRepository.findUserByEmail(email));
    }

    /**
     * Finds a {@link User} by their username or email. This method is essentially a combination of findUserByUsername() and findUserByEmail().
     *
     * @param usernameOrEmail username / email
     * @return {@link Optional} {@link User}
     */
    public Optional<User> findUser(final String usernameOrEmail) {
        validateParameterIsNotNull(usernameOrEmail, "username/email cannot be null");
        Optional<User> user = findUserByUsername(usernameOrEmail);
        if (user.isEmpty()) {
            user = findUserByEmail(usernameOrEmail);
        }

        return user;
    }
}
