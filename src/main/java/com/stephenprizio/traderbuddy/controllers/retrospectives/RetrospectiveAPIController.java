package com.stephenprizio.traderbuddy.controllers.retrospectives;

import com.stephenprizio.traderbuddy.converters.retrospectives.RetrospectiveDTOConverter;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.services.retrospectives.RetrospectiveService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.*;

/**
 * API Controller for {@link Retrospective}s
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/retrospectives")
public class RetrospectiveAPIController {

    private static final String START_DATE_INVALID_FORMAT = "The start date %s was not of the expected format %s";
    private static final String END_DATE_INVALID_FORMAT = "The end date %s was not of the expected format %s";
    private static final List<String> REQUIRED_JSON_VALUES = List.of("retrospective");
    private static final String INVALID_INTERVAL = "%s was not a valid interval";

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
    @ResponseBody
    @GetMapping("/timespan")
    public StandardJsonResponse getRetrospectivesForTimespan(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval) {

        validateLocalDateFormat(start, DATE_FORMAT, START_DATE_INVALID_FORMAT, start, DATE_FORMAT);
        validateLocalDateFormat(end, DATE_FORMAT, END_DATE_INVALID_FORMAT, end, DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(INVALID_INTERVAL, interval));
        }

        List<Retrospective> retrospectives = this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.parse(start, DateTimeFormatter.ofPattern(DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(DATE_FORMAT)), AggregateInterval.valueOf(interval));
        validateIfAnyResult(retrospectives, "No retrospectives were found within interval: [%s, %s] for interval %s", start, end, interval);

        return new StandardJsonResponse(true, this.retrospectiveDTOConverter.convertAll(retrospectives), StringUtils.EMPTY);
    }

    /**
     * Obtains a retrospective for the given start date, end date and interval
     *
     * @param start start date
     * @param end end date
     * @param interval {@link AggregateInterval}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/unique")
    public StandardJsonResponse getRetrospectiveForStartDateAndEndDateAndInterval(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval) {

        validateLocalDateFormat(start, DATE_FORMAT, START_DATE_INVALID_FORMAT, start, DATE_FORMAT);
        validateLocalDateFormat(end, DATE_FORMAT, END_DATE_INVALID_FORMAT, end, DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(INVALID_INTERVAL, interval));
        }

        Optional<Retrospective> retrospective = this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), AggregateInterval.valueOf(interval));
        if (retrospective.isEmpty()) {
            return new StandardJsonResponse(false, null, String.format("No retrospective was found for start date %s, end date %s and interval %s", start, end, interval));
        }

        return new StandardJsonResponse(true, this.retrospectiveDTOConverter.convert(retrospective.get()), StringUtils.EMPTY);
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link Retrospective}
     *
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PostMapping("/create")
    public StandardJsonResponse postCreateRetrospective(final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.retrospectiveDTOConverter.convert(this.retrospectiveService.createRetrospective(requestBody)), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates an existing {@link Retrospective}
     *
     * @param start start date
     * @param end end date
     * @param interval {@link AggregateInterval}
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/update")
    public StandardJsonResponse putUpdateRetrospective(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval, final @RequestBody Map<String, Object> requestBody) {

        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        validateLocalDateFormat(start, DATE_FORMAT, START_DATE_INVALID_FORMAT, start, DATE_FORMAT);
        validateLocalDateFormat(end, DATE_FORMAT, END_DATE_INVALID_FORMAT, end, DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(INVALID_INTERVAL, interval));
        }

        return new StandardJsonResponse(
                true,
                this.retrospectiveDTOConverter.convert(this.retrospectiveService.updateRetrospective(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), AggregateInterval.valueOf(interval), requestBody)),
                StringUtils.EMPTY
        );
    }
}
