package com.stephenprizio.traderbuddy.controllers.summary;

import com.stephenprizio.traderbuddy.enums.TradesSummaryInterval;
import com.stephenprizio.traderbuddy.models.nonentities.StandardJsonResponse;
import com.stephenprizio.traderbuddy.models.records.TradeSummary;
import com.stephenprizio.traderbuddy.services.summary.TradesSummaryService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.TraderBuddyValidator.validateLocalDateTimeFormat;

/**
 * API Controller for {@link TradeSummary}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade-summary")
public class TradesSummaryApiController {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Resource(name = "tradesSummaryService")
    private TradesSummaryService tradesSummaryService;


    //  METHODS

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link TradeSummary} for the given time span
     *
     * @param start start of interval
     * @param end end of interval
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/time-span")
    public StandardJsonResponse getSummaryForTimeSpan(final @RequestParam("start") String start, final @RequestParam("end") String end) {

        validateLocalDateTimeFormat(start, DATE_FORMAT, "The start date %s was not of the expected format %s", start, DATE_FORMAT);
        validateLocalDateTimeFormat(end, DATE_FORMAT, "The end date %s was not of the expected format %s", end, DATE_FORMAT);

        TradeSummary tradeSummary = this.tradesSummaryService.getSummaryForTimeSpan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME));
        return new StandardJsonResponse(true, tradeSummary, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link TradeSummary} for the given time span
     * @param start start of time span
     * @param end end of time span
     * @param interval time interval : daily, weekly, monthly, yearly
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/report")
    public StandardJsonResponse getReportOfSummariesForTimeSpan(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval) {

        validateLocalDateTimeFormat(start, DATE_FORMAT, "The start date %s was not of the expected format %s", start, DATE_FORMAT);
        validateLocalDateTimeFormat(end, DATE_FORMAT, "The end date %s was not of the expected format %s", end, DATE_FORMAT);

        if (!EnumUtils.isValidEnumIgnoreCase(TradesSummaryInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid time interval", interval));
        }

        List<TradeSummary> summary =
                this.tradesSummaryService.getReportOfSummariesForTimeSpan(
                        LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME),
                        LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME),
                        TradesSummaryInterval.valueOf(interval.toUpperCase())
                );
        return new StandardJsonResponse(true, summary, StringUtils.EMPTY);
    }
}
