package com.stephenprizio.traderbuddy.controllers.advice;

import com.stephenprizio.traderbuddy.exceptions.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.NoResultFoundException;
import com.stephenprizio.traderbuddy.exceptions.NonUniqueItemFoundException;
import com.stephenprizio.traderbuddy.models.nonentities.StandardJsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    @ExceptionHandler({
            DateTimeException.class,
            IllegalParameterException.class,
            NonUniqueItemFoundException.class,
            NoResultFoundException.class,
            UnsupportedOperationException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StandardJsonResponse handleClientError(Exception exception) {
        LOGGER.error("Bad Request by the client. Please try again: ", exception);
        return generateResponse("Bad Request by the client. Please try again." + exception.getMessage());
    }


    //  HELPERS

    /**
     * Generates a {@link StandardJsonResponse}
     */
    private StandardJsonResponse generateResponse(String message) {
        return new StandardJsonResponse(false, null, message);
    }
}
