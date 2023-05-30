package com.traderbuddyv2.api.controllers.advice;

import com.traderbuddyv2.api.constants.ApiConstants;
import com.traderbuddyv2.api.exceptions.InvalidEnumException;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.system.GenericSystemException;
import com.traderbuddyv2.core.exceptions.validation.*;
import com.traderbuddyv2.importing.exceptions.FileExtensionNotSupportedException;
import com.traderbuddyv2.integration.exceptions.IntegrationException;
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

import java.io.FileNotFoundException;
import java.sql.SQLSyntaxErrorException;
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
    @ExceptionHandler(value = {
            AuthenticationException.class,
            BadCredentialsException.class,
            DateTimeException.class,
            DisabledException.class,
            FileExtensionNotSupportedException.class,
            IllegalParameterException.class,
            InvalidEnumException.class,
            JsonMissingPropertyException.class,
            MissingRequiredDataException.class,
            NonUniqueItemFoundException.class,
            NoResultFoundException.class,
            UnsupportedOperationException.class,
            UsernameNotFoundException.class
    })
    public StandardJsonResponse handleClientError(final Exception exception) {
        LOGGER.error("Bad Request by the client. Please try again: ", exception);
        return generateResponse(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE, exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({
            EntityCreationException.class,
            EntityModificationException.class,
            ExpiredJwtException.class,
            FileNotFoundException.class,
            GenericSystemException.class,
            IllegalArgumentException.class,
            IntegrationException.class,
            SQLSyntaxErrorException.class
    })
    public StandardJsonResponse handleServerError(final Exception exception) {
        LOGGER.error("An internal server error occurred. ", exception);
        return generateResponse(ApiConstants.SERVER_ERROR_DEFAULT_MESSAGE, exception.getMessage());
    }


    //  HELPERS

    /**
     * Generates a {@link StandardJsonResponse}
     */
    private StandardJsonResponse generateResponse(final String message, final String internalMessage) {
        return new StandardJsonResponse(false, null, message, internalMessage);
    }
}
