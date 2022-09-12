package com.stephenprizio.traderbuddy.controllers.plans;

import com.stephenprizio.traderbuddy.converters.plans.TradingPlanDTOConverter;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.dto.plans.TradingPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.services.investing.InvestingService;
import com.stephenprizio.traderbuddy.services.plans.TradingPlanService;
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
 * API Controller for {@link TradingPlan}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trading-plans")
public class TradingPlanApiController {

    private static final List<String> REQUIRED_JSON_VALUES = List.of("status", "active", "name", "startDate", "endDate", "profitTarget", "compoundFrequency", "startingBalance");
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    @Resource(name = "investingService")
    private InvestingService investingService;

    @Resource(name = "tradingPlanDTOConverter")
    private TradingPlanDTOConverter tradingPlanDTOConverter;

    @Resource(name = "tradingPlanService")
    private TradingPlanService tradingPlanService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing the currently active {@link TradingPlan}
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/current")
    public StandardJsonResponse getCurrentlyActiveTradingPlan() {
        Optional<TradingPlan> tradingPlan = this.tradingPlanService.findCurrentlyActiveTradingPlan();
        validateIfPresent(tradingPlan, "No currently active tradingPlan was found");
        return tradingPlan.map(value -> new StandardJsonResponse(true, this.tradingPlanDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, new TradingPlanDTO(), StringUtils.EMPTY));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link TradingPlan}s for the given {@link TradingPlanStatus}
     *
     * @param status {@link TradingPlanStatus}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-status")
    public StandardJsonResponse getTradingPlansForStatus(final @RequestParam("status") String status) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradingPlanStatus.class, status)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid status", status));
        }

        TradingPlanStatus tradingPlanStatus = TradingPlanStatus.valueOf(status.toUpperCase());
        List<TradingPlan> tradingPlans = this.tradingPlanService.findTradingPlansForStatus(tradingPlanStatus);
        validateIfAnyResult(tradingPlans, "No tradingPlans were found for type %s", tradingPlanStatus.name());

        return new StandardJsonResponse(true, this.tradingPlanDTOConverter.convertAll(tradingPlans), StringUtils.EMPTY);
    }

    /**
     * Obtains the forecast for the currently active {@link TradingPlan}
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/forecast")
    public StandardJsonResponse getForecast(final @RequestParam("interval") String interval, final @RequestParam("begin") String begin, final @RequestParam("limit") String limit) {

        validateLocalDateFormat(begin, DATE_FORMAT, "The start date %s was not of the expected format %s", begin, DATE_FORMAT);
        validateLocalDateFormat(limit, DATE_FORMAT, "The end date %s was not of the expected format %s", limit, DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid interval", interval));
        }

        Optional<TradingPlan> tradingPlan = this.tradingPlanService.findCurrentlyActiveTradingPlan();
        return
                tradingPlan
                        .map(plan ->
                                new StandardJsonResponse(true, this.investingService.forecast(plan, AggregateInterval.valueOf(interval.toUpperCase()), LocalDate.parse(begin, DateTimeFormatter.ofPattern(DATE_FORMAT)), LocalDate.parse(limit, DateTimeFormatter.ofPattern(DATE_FORMAT))), StringUtils.EMPTY)
                        ).orElseGet(() ->
                                new StandardJsonResponse(false, null, "No active trading plan was found")
                        );
    }




    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link TradingPlan}
     *
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PostMapping("/create")
    public StandardJsonResponse postCreateTradingPlan(final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        validateAdditionalParameters(requestBody);
        return new StandardJsonResponse(true, this.tradingPlanDTOConverter.convert(this.tradingPlanService.createTradingPlan(requestBody)), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Modifies an existing {@link TradingPlan}
     *
     * @param name plan name
     * @param startDate plan start date
     * @param endDate plan end date
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/update")
    public StandardJsonResponse putUpdateTradingPlan(final @RequestParam("name") String name, final @RequestParam("startDate") String startDate, final @RequestParam("endDate") String endDate, final @RequestBody Map<String, Object> requestBody) {

        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        validateAdditionalParameters(requestBody);
        validateLocalDateFormat(startDate, DATE_FORMAT, "The start date %s was not of the expected format %s", startDate, DATE_FORMAT);
        validateLocalDateFormat(endDate, DATE_FORMAT, "The end date %s was not of the expected format %s", endDate, DATE_FORMAT);

        return new StandardJsonResponse(true, this.tradingPlanDTOConverter.convert(this.tradingPlanService.updateTradingPlan(name, LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE), LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE), requestBody)), StringUtils.EMPTY);
    }


    //  HELPERS

    /**
     * Validates extra parameters
     *
     * @param requestBody json request
     */
    private void validateAdditionalParameters(final Map<String, Object> requestBody) {
        if (requestBody.containsKey("depositPlan")) {
            validateJsonIntegrity((Map<String, Object>) requestBody.get("depositPlan"), List.of("amount", "frequency"), "json did contain the required keys: [amount, frequency]");
        }

        if (requestBody.containsKey("withdrawalPlan")) {
            validateJsonIntegrity((Map<String, Object>) requestBody.get("withdrawalPlan"), List.of("amount", "frequency"), "json did contain the required keys: [amount, frequency]");
        }
    }
}
