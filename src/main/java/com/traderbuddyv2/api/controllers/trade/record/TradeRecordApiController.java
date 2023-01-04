package com.traderbuddyv2.api.controllers.trade.record;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.trade.record.TradeRecordDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.record.TradeRecordDTO;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.records.trade.MonthRecord;
import com.traderbuddyv2.core.models.records.trade.YearRecord;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * API controller for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade-record")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradeRecordApiController extends AbstractApiController {

    @Resource(name = "tradeRecordDTOConverter")
    private TradeRecordDTOConverter tradeRecordDTOConverter;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


    //  METHODS

    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link TradeRecordDTO}s
     *
     * @param count number of results to return
     * @param aggregateInterval {@link AggregateInterval}
     * @param sort sort order, passing in a value of 'asc' or 'ascending' will sort in ascending order by start date,
     *             otherwise the default sort order is descending
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/log")
    public StandardJsonResponse getRecentHistory(
            final @RequestParam("count") int count,
            final @RequestParam("aggregateInterval") String aggregateInterval,
            final @RequestParam(value = "sortOrder", defaultValue = "desc") String sort
    ) {
        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, aggregateInterval)) {
            return new StandardJsonResponse(false, null, String.format(CoreConstants.Validation.INVALID_INTERVAL, aggregateInterval));
        }

        List<TradeRecordDTO> records = this.tradeRecordDTOConverter.convertAll(this.tradeRecordService.findRecentHistory(count, AggregateInterval.valueOf(aggregateInterval.toUpperCase())));
        if (sort.equalsIgnoreCase("asc") || sort.equalsIgnoreCase("ascending")) {
            Collections.reverse(records);
        }

        return new StandardJsonResponse(true, records, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link TradeRecordDTO}s
     *
     * @param start start date
     * @param end end date
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/history")
    public StandardJsonResponse getHistory(
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("aggregateInterval") String aggregateInterval,
            final @RequestParam(value = "sortOrder", defaultValue = "desc") String sort
    ) {
        validate(start, end, aggregateInterval);
        List<TradeRecordDTO> records = this.tradeRecordDTOConverter.convertAll(this.tradeRecordService.findHistory(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), AggregateInterval.valueOf(aggregateInterval.toUpperCase())));
        if (sort.equalsIgnoreCase("desc") || sort.equalsIgnoreCase("descending")) {
            Collections.reverse(records);
        }

        return new StandardJsonResponse(true, records, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link MonthRecord}
     *
     * @param year calendar year
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/active-months")
    public StandardJsonResponse getActiveMonths(final @RequestParam("year") int year) {
        return new StandardJsonResponse(true, this.tradeRecordService.findActiveMonths(year), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link YearRecord}
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/active-years")
    public StandardJsonResponse getActiveYears() {
        return new StandardJsonResponse(true, this.tradeRecordService.findActiveYears(), StringUtils.EMPTY);
    }
}
