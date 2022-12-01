package com.traderbuddyv2.core.services.security;

import com.traderbuddyv2.core.models.entities.security.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link User}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("traderBuddyUserDetailsService")
public class TraderBuddyUserDetailsService implements UserDetailsService {

    @Resource(name = "userService")
    private UserService userService;


    //  METHODS

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        validateParameterIsNotNull(username, "username cannot be null");

        Optional<User> user = this.userService.findUserByUsername(username);
        if (user.isEmpty()){
            user = this.userService.findUserByEmail(username);
        }

        return
                user
                        .map(value -> new org.springframework.security.core.userdetails.User(value.getUsername(), value.getPassword(), AuthorityUtils.NO_AUTHORITIES))
                        .orElseThrow(() -> new BadCredentialsException(String.format("No username or email was found for value %s", username)));
    }

    /**
     * Obtains the currently logged in {@link User}
     *
     * @return {@link User}
     */
    public User getCurrentUser() {

        //  TODO: TEMP
        return this.userService.findUser("s.prizio").get();

        /*if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof UserDetails) {
                return this.userService.findUser(((UserDetails) principal).getUsername()).orElseThrow(() -> new UsernameNotFoundException("No one is currently authenticated"));
            }
        }

        throw new AnonymousUserException("No one is currently authenticated");*/
    }
}
