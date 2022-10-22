package com.stephenprizio.traderbuddy.services.security;

import com.stephenprizio.traderbuddy.models.entities.security.User;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

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

        Optional<User> user = this.userService.findUserByUsername(username);
        if (user.isEmpty()){
            user = this.userService.findUserByEmail(username);
        }

        return
                user
                        .map(value -> new org.springframework.security.core.userdetails.User(value.getUsername(), value.getPassword(), AuthorityUtils.NO_AUTHORITIES))
                        .orElseThrow(() -> new BadCredentialsException(String.format("No username or email was found for value %s", username)));
    }
}
