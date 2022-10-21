package com.stephenprizio.traderbuddy.security.jwt.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles JWT Token operations
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("jwtTokenManager")
public class JwtTokenManager implements Serializable {

    @Serial
    private static final long serialVersionUID = 7008375124389347049L;

    public static final long TOKEN_VALIDITY = 10L * 60L * 60L;

    @Value("${secret}")
    private String jwtSecret;


    //  METHODS

    /**
     * Generates a Jwt token for the given {@link UserDetails}
     *
     * @param userDetails {@link UserDetails}
     * @return jwt token
     */
    public String generateJwtToken(final UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    /**
     * Validates the given Jwt token
     *
     * @param token jwt token
     * @param userDetails {@link UserDetails}
     * @return true if a valid token was given
     */
    public boolean validateJwtToken(final String token, final UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        boolean isTokenExpired = claims.getExpiration().before(new Date());
        return (username.equals(userDetails.getUsername()) && !isTokenExpired);
    }

    /**
     * Obtains the username from a token
     *
     * @param token jwt token
     * @return username
     */
    public String getUsernameFromToken(final String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}
