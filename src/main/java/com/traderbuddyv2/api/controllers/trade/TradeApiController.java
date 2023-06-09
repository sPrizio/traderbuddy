package com.traderbuddyv2.api.controllers.trade;

import com.traderbuddyv2.api.converters.trade.TradeDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.TradeDTO;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.trade.info.TradeType;
import com.traderbuddyv2.core.exceptions.system.GenericSystemException;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import com.traderbuddyv2.importing.services.GenericImportService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.*;

/**
 * Api controller for {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trade")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class TradeApiController {

    private static final String TRADE_ID = "tradeId";

    @Resource(name = "genericImportService")
    private GenericImportService genericImportService;

    @Resource(name = "tradeDTOConverter")
    private TradeDTOConverter tradeDTOConverter;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-type")
    public StandardJsonResponse getTradesForTradeType(final @RequestParam("tradeType") String tradeType, final @RequestParam("includeNonRelevant") boolean includeNonRelevant) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradeType.class, tradeType)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid trade type", tradeType));
        }

        TradeType type = TradeType.valueOf(tradeType.toUpperCase());
        List<Trade> trades = this.tradeService.findAllByTradeType(type, includeNonRelevant);
        validateIfAnyResult(trades, "No trades were found for type %s", type.name());

        return new StandardJsonResponse(true, this.tradeDTOConverter.convertAll(trades), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given interval of time
     *
     * @param start start date & time
     * @param end end date & time
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-interval")
    public StandardJsonResponse getTradesWithinInterval(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("includeNonRelevant") boolean includeNonRelevant) {

        validateLocalDateTimeFormat(start, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.START_DATE_INVALID_FORMAT, end, CoreConstants.DATE_TIME_FORMAT));

        List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME), includeNonRelevant);
        validateIfAnyResult(trades, "No trades were found within interval: [%s, %s]", start, end);

        return new StandardJsonResponse(true, this.tradeDTOConverter.convertAll(trades), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given interval of time
     *
     * @param start start date & time
     * @param end end date & time
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-interval-paged")
    public StandardJsonResponse getTradesWithinIntervalPaged(
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("includeNonRelevant") boolean includeNonRelevant,
            final @RequestParam(value = "page", defaultValue = "0") int page,
            final @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        validateLocalDateTimeFormat(start, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.START_DATE_INVALID_FORMAT, start, CoreConstants.DATE_TIME_FORMAT));
        validateLocalDateTimeFormat(end, CoreConstants.DATE_TIME_FORMAT, String.format(CoreConstants.Validation.START_DATE_INVALID_FORMAT, end, CoreConstants.DATE_TIME_FORMAT));

        Page<Trade> trades = this.tradeService.findAllTradesWithinTimespan(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME), includeNonRelevant, page, pageSize);
        return new StandardJsonResponse(true, trades.map(tr -> this.tradeDTOConverter.convert(tr)), StringUtils.EMPTY);
    }


    /**
     * Returns a {@link StandardJsonResponse} containing a {@link Trade} for the given trade id
     *
     * @param tradeId trade id
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-trade-id")
    public StandardJsonResponse getTradeForTradeId(final @RequestParam(TRADE_ID) String tradeId) {
        Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId);
        validateIfPresent(trade, "No trade was found with trade id: %s", tradeId);
        return trade.map(value -> new StandardJsonResponse(true, this.tradeDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, new TradeDTO(), StringUtils.EMPTY));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a chart recap for the given trade
     *
     * @param tradeId trade id
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/recap")
    public StandardJsonResponse getTradeRecap(final @RequestParam(TRADE_ID) String tradeId) {
        return new StandardJsonResponse(true, this.tradeService.findTradeRecap(tradeId), StringUtils.EMPTY);
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * File upload endpoint to obtain import files to import {@link Trade}s into the system. The system will only accept CSV files
     *
     * @param file {@link MultipartFile}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PostMapping("/import-trades")
    public StandardJsonResponse postImportTrades(final @RequestParam("file") MultipartFile file) throws IOException {

        String result = this.genericImportService.importTrades(file.getInputStream());
        if (StringUtils.isEmpty(result)) {
            return new StandardJsonResponse(true, true, StringUtils.EMPTY);
        }

        return new StandardJsonResponse(false, null, result);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * PUT endpoint that will modify a {@link Trade} with the given id to be marked as not relevant
     *
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/disregard")
    public StandardJsonResponse putDisregardTrade(final @RequestBody Map<String, Object> requestBody) {

        validateJsonIntegrity(requestBody, List.of(TRADE_ID), "json did not contain of the required keys : %s", List.of(TRADE_ID));

        String tradeId = requestBody.get(TRADE_ID).toString();
        boolean result = this.tradeService.disregardTrade(tradeId);

        if (result) {
            this.tradeRecordService.processDisregardedTrade(tradeId);
        } else {
            throw new GenericSystemException(String.format("An error occurred while updating trade : %s. Please try again", tradeId));
        }

        return new StandardJsonResponse(true, true, StringUtils.EMPTY);
    }
}
