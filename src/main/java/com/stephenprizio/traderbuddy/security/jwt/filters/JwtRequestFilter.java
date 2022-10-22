package com.stephenprizio.traderbuddy.security.jwt.filters;

import com.stephenprizio.traderbuddy.security.jwt.authentication.JwtTokenManager;
import com.stephenprizio.traderbuddy.security.jwt.exceptions.JwtTokenNotFoundException;
import com.stephenprizio.traderbuddy.services.security.TraderBuddyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A custom filter that checks each incoming request to ensure that it contains a valid Jwt Token
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("jwtRequestFilter")
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService userDetailsService;

    @Resource(name = "jwtTokenManager")
    private JwtTokenManager jwtTokenManager;


    //  METHODS

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {

        String token;
        String username;
        String tokenHeader = request.getHeader("Authorization");

        /*if (tokenHeader != null && tokenHeader.startsWith(BEARER_PREFIX)) {
            token = tokenHeader.replace(BEARER_PREFIX, StringUtils.EMPTY);
            username = this.jwtTokenManager.getUsernameFromToken(token);
        } else {
            throw new JwtTokenNotFoundException("A Jwt Token was not found within the given http request");
        }

        if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (this.jwtTokenManager.validateJwtToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }*/

        filterChain.doFilter(request, response);
    }
}
