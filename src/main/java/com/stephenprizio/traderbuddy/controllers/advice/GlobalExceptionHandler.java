package com.stephenprizio.traderbuddy.controllers.advice;

import com.stephenprizio.traderbuddy.exceptions.importing.FileExtensionNotSupportedException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.system.GenericSystemException;
import com.stephenprizio.traderbuddy.exceptions.validation.*;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            DateTimeException.class,
            FileExtensionNotSupportedException.class,
            IllegalParameterException.class,
            JsonMissingPropertyException.class,
            MissingRequiredDataException.class,
            NonUniqueItemFoundException.class,
            NoResultFoundException.class,
            UnsupportedOperationException.class
    })
    public StandardJsonResponse handleClientError(Exception exception) {
        LOGGER.error("Bad Request by the client. Please try again: ", exception);
        return generateResponse("Bad Request by the client. Please try again: " + exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler({
            EntityCreationException.class,
            EntityModificationException.class,
            GenericSystemException.class
    })
    public StandardJsonResponse handleServerError(Exception exception) {
        LOGGER.error("An internal server error occurred. Please try again later: ", exception);
        return generateResponse("An internal server error occurred. Please try again later: " + exception.getMessage());
    }


    //  HELPERS

    /**
     * Generates a {@link StandardJsonResponse}
     */
    private StandardJsonResponse generateResponse(String message) {
        return new StandardJsonResponse(false, null, message);
    }
}
