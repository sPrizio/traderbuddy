package com.traderbuddyv2.api.controllers.retrospective;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.retrospective.RetrospectiveDTOConverter;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import com.traderbuddyv2.core.services.retrospective.RetrospectiveService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.validateIfAnyResult;
import static com.traderbuddyv2.core.validation.GenericValidator.validateJsonIntegrity;


/**
 * API Controller for {@link Retrospective}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/retrospective")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class RetrospectiveApiController extends AbstractApiController {

    private static final List<String> REQUIRED_JSON_VALUES = List.of("retrospective");

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
     * @param end   end date
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/timespan")
    public StandardJsonResponse getRetrospectivesForTimespan(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval) {

        validate(start, end, interval);
        List<Retrospective> retrospectives = this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), AggregateInterval.valueOf(interval));
        validateIfAnyResult(retrospectives, "No retrospectives were found within interval: [%s, %s] for interval %s", start, end, interval);

        return new StandardJsonResponse(true, this.retrospectiveDTOConverter.convertAll(retrospectives), StringUtils.EMPTY);
    }

    /**
     * Obtains a retrospective for the given start date, end date and interval
     *
     * @param start    start date
     * @param end      end date
     * @param interval {@link AggregateInterval}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/unique")
    public StandardJsonResponse getRetrospectiveForStartDateAndEndDateAndInterval(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval) {
        validate(start, end, interval);
        Optional<Retrospective> retrospective = this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), AggregateInterval.valueOf(interval));
        return retrospective.map(value -> new StandardJsonResponse(true, this.retrospectiveDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No retrospective was found for start date %s, end date %s and interval %s", start, end, interval)));

    }

    /**
     * Obtains a retrospective for the given uid
     *
     * @param uid uid
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/uid")
    public StandardJsonResponse getRetrospectiveForUid(final @RequestParam("uid") String uid) {
        Optional<Retrospective> retrospective = this.retrospectiveService.findRetrospectiveForUid(uid);
        return retrospective.map(value -> new StandardJsonResponse(true, this.retrospectiveDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(false, null, String.format("No retrospective was found for uid %s", uid)));

    }

    /**
     * Obtains a {@link List} of {@link LocalDate}s representing the first of the month for a list of months that a user has a retrospective
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/active-months")
    public StandardJsonResponse getActiveRetrospectiveMonths(final @RequestParam("year") int year) {
        return new StandardJsonResponse(true, this.retrospectiveService.findActiveRetrospectiveMonths(year), StringUtils.EMPTY);
    }

    /**
     * Obtains a {@link List} of {@link LocalDate}s representing the first of the year for a list of years that a user has a retrospective
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/active-years")
    public StandardJsonResponse getActiveRetrospectiveYears() {
        return new StandardJsonResponse(true, this.retrospectiveService.findActiveRetrospectiveYears(), StringUtils.EMPTY);
    }

    /**
     * Obtains the most recent {@link Retrospective} for the given interval
     *
     * @param interval string value of {@link AggregateInterval}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/most-recent")
    public StandardJsonResponse getMostRecentRetrospectiveForInterval(final @RequestParam("interval") String interval) {

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.INVALID_INTERVAL, interval));
        }

        Optional<Retrospective> retrospective = this.retrospectiveService.findMostRecentRetrospectiveForInterval(AggregateInterval.valueOf(interval.toUpperCase()));
        return
                retrospective
                        .map(value -> new StandardJsonResponse(true, this.retrospectiveDTOConverter.convert(value), StringUtils.EMPTY))
                        .orElseGet(() -> new StandardJsonResponse(false, null, String.format("No recent retrospectives were found for interval : %s", interval)));
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
     * @param uid         uid
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/update")
    public StandardJsonResponse putUpdateRetrospective(final @RequestParam("uid") String uid, final @RequestBody Map<String, Object> requestBody) {

        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());

        return new StandardJsonResponse(
                true,
                this.retrospectiveDTOConverter.convert(this.retrospectiveService.updateRetrospective(uid, requestBody)),
                StringUtils.EMPTY
        );
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes an existing {@link Retrospective}
     *
     * @param uid uid
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @DeleteMapping("/delete")
    public StandardJsonResponse deleteRetrospective(final @RequestParam("uid") String uid) {
        return new StandardJsonResponse(true, this.retrospectiveService.deleteRetrospective(uid), StringUtils.EMPTY);
    }
}
