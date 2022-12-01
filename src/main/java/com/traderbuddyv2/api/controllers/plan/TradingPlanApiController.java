package com.traderbuddyv2.api.controllers.plan;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.plan.TradingPlanDTOConverter;
import com.traderbuddyv2.api.models.dto.plans.TradingPlanDTO;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.records.plan.ForecastEntry;
import com.traderbuddyv2.core.models.records.plan.ForecastStatistics;
import com.traderbuddyv2.core.models.records.plan.ForecastSummary;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.*;

/**
 * API Controller for {@link TradingPlan}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trading-plan")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradingPlanApiController extends AbstractApiController {

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
     * @param interval {@link AggregateInterval}
     * @param begin start of period look back
     * @param limit end of period look back
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/forecast")
    public StandardJsonResponse getForecast(final @RequestParam("interval") String interval, final @RequestParam("begin") String begin, final @RequestParam("limit") String limit) {
        Optional<TradingPlan> tradingPlan = obtainTradingPlan(interval, begin, limit);
        return
                tradingPlan
                        .map(plan -> {
                            final List<ForecastEntry> entries =
                                    this.tradingPlanService.forecast(
                                            plan,
                                            AggregateInterval.valueOf(interval.toUpperCase()),
                                            LocalDate.parse(begin, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)),
                                            LocalDate.parse(limit, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT))
                                    );
                            return new StandardJsonResponse(true, new ForecastSummary(entries, new ForecastStatistics(plan.getStartingBalance(), entries)), StringUtils.EMPTY);
                        }).orElseGet(() ->
                                new StandardJsonResponse(false, null, String.format("No active trading plan was found for interval %s, start %s, end %s", interval, begin, limit))
                        );
    }


    //  HELPERS

    /**
     * Obtains a trading plan
     *
     * @param interval {@link AggregateInterval}
     * @param begin start of period look back
     * @param limit end of period look back
     * @return {@link Optional} {@link TradingPlan}
     */
    private Optional<TradingPlan> obtainTradingPlan(final String interval, final String begin, final String limit) {

        validateLocalDateFormat(begin, CoreConstants.DATE_FORMAT, "The start date %s was not of the expected format %s", begin, CoreConstants.DATE_FORMAT);
        validateLocalDateFormat(limit, CoreConstants.DATE_FORMAT, "The end date %s was not of the expected format %s", limit, CoreConstants.DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return Optional.empty();
        }

        return this.tradingPlanService.findCurrentlyActiveTradingPlan();
    }
}
