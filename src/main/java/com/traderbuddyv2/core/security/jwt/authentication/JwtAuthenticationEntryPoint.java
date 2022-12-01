package com.traderbuddyv2.core.security.jwt.authentication;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * Extends {@link AuthenticationEntryPoint} to include custom logic
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("jwtAuthenticationEntryPoint")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {


    //  METHODS

    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
