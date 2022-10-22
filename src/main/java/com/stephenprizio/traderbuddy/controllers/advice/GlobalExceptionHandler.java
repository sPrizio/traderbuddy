package com.stephenprizio.traderbuddy.controllers.advice;

import com.stephenprizio.traderbuddy.exceptions.importing.FileExtensionNotSupportedException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.system.GenericSystemException;
import com.stephenprizio.traderbuddy.exceptions.validation.*;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import io.jsonwebtoken.ExpiredJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.DateTimeException;

/**
 * Handles the exceptions thrown by the application
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    //  METHODS

    @ResponseBody
    @ExceptionHandler({
            AuthenticationException.class,
            BadCredentialsException.class,
            DateTimeException.class,
            DisabledException.class,
            FileExtensionNotSupportedException.class,
            IllegalParameterException.class,
            JsonMissingPropertyException.class,
            MissingRequiredDataException.class,
            NonUniqueItemFoundException.class,
            NoResultFoundException.class,
            UnsupportedOperationException.class,
            UsernameNotFoundException.class
    })
    public StandardJsonResponse handleClientError(final Exception exception) {
        LOGGER.error("Bad Request by the client. Please try again: ", exception);
        return generateResponse("Looks like your request could not be processed. Check your inputs and try again!");
    }

    @ResponseBody
    @ExceptionHandler({
            EntityCreationException.class,
            EntityModificationException.class,
            ExpiredJwtException.class,
            GenericSystemException.class,
            IllegalArgumentException.class
    })
    public StandardJsonResponse handleServerError(final Exception exception) {
        LOGGER.error("An internal server error occurred. ", exception);
        return generateResponse("An error on our side occurred. Please try again.");
    }


    //  HELPERS

    /**
     * Generates a {@link StandardJsonResponse}
     */
    private StandardJsonResponse generateResponse(String message) {
        return new StandardJsonResponse(false, null, message);
    }
}
