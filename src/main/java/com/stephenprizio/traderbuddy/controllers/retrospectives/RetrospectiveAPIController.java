package com.stephenprizio.traderbuddy.controllers.retrospectives;

import com.stephenprizio.traderbuddy.converters.retrospectives.RetrospectiveDTOConverter;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.services.retrospectives.RetrospectiveService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateIfAnyResult;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateLocalDateFormat;

/**
 * API Controller for {@link Retrospective}s
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/retrospectives")
public class RetrospectiveAPIController {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Resource(name = "retrospectiveDTOConverter")
    private RetrospectiveDTOConverter retrospectiveDTOConverter;

    @Resource(name = "retrospectiveService")
    private RetrospectiveService retrospectiveService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Obtains retrospectives for the given start and end dates
     *
     * @param start start date
     * @param end end date
     * @return {@link StandardJsonResponse}
     */
    @GetMapping("/timespan")
    public StandardJsonResponse getRetrospectivesForTimespan(final @RequestParam("start") String start, final @RequestParam("end") String end) {

        validateLocalDateFormat(start, DATE_FORMAT, "The start date %s was not of the expected format %s", start, DATE_FORMAT);
        validateLocalDateFormat(end, DATE_FORMAT, "The end date %s was not of the expected format %s", end, DATE_FORMAT);

        List<Retrospective> retrospectives = this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT)));
        validateIfAnyResult(retrospectives, "No retrospectives were found within interval: [%s, %s]", start, end);

        return new StandardJsonResponse(true, this.retrospectiveDTOConverter.convertAll(retrospectives), StringUtils.EMPTY);
    }
}
