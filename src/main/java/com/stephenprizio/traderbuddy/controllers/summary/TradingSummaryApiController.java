package com.stephenprizio.traderbuddy.controllers.summary;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingSummary;
import com.stephenprizio.traderbuddy.services.summary.TradingSummaryService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateLocalDateTimeFormat;

/**
 * API Controller for {@link TradingRecord}s & {@link TradingSummary}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade-summary")
public class TradingSummaryApiController {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Resource(name = "tradingSummaryService")
    private TradingSummaryService tradingSummaryService;


    //  METHODS

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link TradingRecord} for the given time span
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

        TradingRecord tradingRecord = this.tradingSummaryService.getSummaryForTimeSpan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME));
        return new StandardJsonResponse(true, tradingRecord, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link TradingSummary} for the given time span
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

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid time interval", interval));
        }

        TradingSummary summary =
                this.tradingSummaryService.getReportOfSummariesForTimeSpan(
                        LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME),
                        LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME),
                        AggregateInterval.valueOf(interval.toUpperCase())
                );
        return new StandardJsonResponse(true, summary, StringUtils.EMPTY);
    }
}
