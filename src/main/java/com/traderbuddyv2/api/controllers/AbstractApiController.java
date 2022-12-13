package com.traderbuddyv2.api.controllers;

import com.traderbuddyv2.api.exceptions.InvalidEnumException;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import org.apache.commons.lang3.EnumUtils;

import static com.traderbuddyv2.core.validation.GenericValidator.validateLocalDateFormat;

/**
 * Parent-level controller providing common functionality
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public abstract class AbstractApiController {

    /**
     * Basic data integrity validation
     *
     * @param start start date of format yyyy-MM-dd
     * @param end end date of format yyyy-MM-dd
     */
    public void validate(final String start, final String end) {
        validateLocalDateFormat(start, CoreConstants.DATE_FORMAT, CoreConstants.Validation.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_FORMAT);
        validateLocalDateFormat(end, CoreConstants.DATE_FORMAT, CoreConstants.Validation.END_DATE_INVALID_FORMAT, end, CoreConstants.DATE_FORMAT);
    }

    /**
     * Basic data integrity validation
     *
     * @param start start date of format yyyy-MM-dd
     * @param end end date of format yyyy-MM-dd
     * @param interval string value of {@link AggregateInterval}
     */
    public void validate(final String start, final String end, final String interval) {

        validateLocalDateFormat(start, CoreConstants.DATE_FORMAT, CoreConstants.Validation.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_FORMAT);
        validateLocalDateFormat(end, CoreConstants.DATE_FORMAT, CoreConstants.Validation.END_DATE_INVALID_FORMAT, end, CoreConstants.DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            throw new InvalidEnumException(String.format(CoreConstants.Validation.INVALID_INTERVAL, interval));
        }
    }
}
