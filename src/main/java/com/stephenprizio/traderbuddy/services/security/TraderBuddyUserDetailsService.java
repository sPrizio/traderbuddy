package com.stephenprizio.traderbuddy.services.security;

import com.stephenprizio.traderbuddy.models.entities.security.User;
import com.stephenprizio.traderbuddy.repositories.security.UserRepository;
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

    @Resource
    private UserRepository userRepository;


    //  METHODS


    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(this.userRepository.findUserByUsername(username));
        return user.map(value -> new org.springframework.security.core.userdetails.User(value.getUsername(), value.getPassword(), AuthorityUtils.NO_AUTHORITIES)).orElse(null);
    }
}
