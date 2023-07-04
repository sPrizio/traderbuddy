package com.traderbuddyv2.core.services.security;

import com.traderbuddyv2.core.enums.security.UserRole;
import com.traderbuddyv2.core.enums.system.Country;
import com.traderbuddyv2.core.enums.system.PhoneType;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.exceptions.validation.NoResultFoundException;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.models.entities.security.UserLocale;
import com.traderbuddyv2.core.models.entities.system.PhoneNumber;
import com.traderbuddyv2.core.repositories.security.UserLocaleRepository;
import com.traderbuddyv2.core.repositories.security.UserRepository;
import com.traderbuddyv2.core.repositories.system.PhoneNumberRepository;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link User}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("userService")
public class UserService {

    @Resource(name = "phoneNumberRepository")
    private PhoneNumberRepository phoneNumberRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Resource(name = "userLocaleRepository")
    private UserLocaleRepository userLocaleRepository;


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

    /**
     * Creates a new {@link User} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link User}
     */
    public User createUser(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a User was null or empty");
        }

        try {
            return applyChanges(new User(), data);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A User could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link User} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param email user email
     * @param data  {@link Map}
     * @return modified {@link User}
     */
    public User updateUser(final String email, final Map<String, Object> data) {

        validateParameterIsNotNull(email, "email cannot be null");

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating a User was null or empty");
        }

        try {
            User user =
                    findUserByEmail(email)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No User found for email %s", email)));

            return applyChanges(user, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the User : %s", e.getMessage()), e);
        }
    }


    //  HELPERS

    /**
     * Applies changes to the given {@link User} with the given data
     *
     * @param user {@link User}
     * @param data {@link Map}
     * @return updated {@link Retrospective}
     */
    private User applyChanges(User user, final Map<String, Object> data) {

        Map<String, Object> ud = (Map<String, Object>) data.get("user");

        PhoneNumber phoneNumber = (user.getPhone() == null) ? new PhoneNumber() : user.getPhone();
        UserLocale userLocale = (user.getUserLocale() == null) ? new UserLocale() : user.getUserLocale();

        user.setEmail(ud.get("email").toString());
        user.setPassword(ud.get("password").toString());
        user.setLastName(ud.get("lastName").toString());
        user.setFirstName(ud.get("firstName").toString());
        user.setUsername(ud.get("username").toString());
        user.setRoles(List.of(UserRole.TRADER));

        user = this.userRepository.save(user);

        phoneNumber.setPhoneType(PhoneType.valueOf(ud.get("phoneType").toString()));
        phoneNumber.setCountryCode(Short.parseShort(ud.get("countryCode").toString()));
        phoneNumber.setTelephoneNumber(Long.parseLong(ud.get("phoneNumber").toString()));
        phoneNumber.setUser(user);

        userLocale.setCountry(Country.getByIsoCode(ud.get("country").toString()));
        userLocale.setLanguages(null);
        userLocale.setTownCity(ud.get("townCity").toString());
        userLocale.setTimeZoneOffset(ud.get("timeZoneOffset").toString());
        userLocale.setCurrencies(null);
        userLocale.setUser(user);

        phoneNumber = this.phoneNumberRepository.save(phoneNumber);

        user.setPhone(phoneNumber);
        user.setUserLocale(userLocale);
        return this.userRepository.save(user);
    }
}
