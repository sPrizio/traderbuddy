package com.stephenprizio.traderbuddy.controllers.trades;

import com.stephenprizio.traderbuddy.converters.trades.TradeDTOConverter;
import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.exceptions.system.GenericSystemException;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.models.nonentities.dto.trades.TradeDTO;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.services.trades.TradeService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.*;

/**
 * API Controller for {@link Trade}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trades")
public class TradeApiController {

    private static final String TRADE_ID = "tradeId";

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Resource(name = "tradeDTOConverter")
    private TradeDTOConverter tradeDTOConverter;

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
    public StandardJsonResponse getTradesForTradeType(final @RequestParam("tradeType") String tradeType, final @RequestParam("includeNonRelevant") Boolean includeNonRelevant) {

        if (!EnumUtils.isValidEnumIgnoreCase(TradeType.class, tradeType)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid trade type", tradeType));
        }

        TradeType type = TradeType.valueOf(tradeType.toUpperCase());
        List<Trade> trades = this.tradeService.findAllByTradeType(type, includeNonRelevant);
        validateIfAnyResult(trades, "No trades were found for type %s", type);

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
    public StandardJsonResponse getTradesWithinInterval(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("includeNonRelevant") Boolean includeNonRelevant) {

        validateLocalDateTimeFormat(start, DATE_FORMAT, "The start date %s was not of the expected format %s", start, DATE_FORMAT);
        validateLocalDateTimeFormat(end, DATE_FORMAT, "The end date %s was not of the expected format %s", end, DATE_FORMAT);

        List<Trade> trades = this.tradeService.findAllTradesWithinDate(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME), includeNonRelevant);
        validateIfAnyResult(trades, "No trades were found within interval: [%s, %s]", start, end);

        return new StandardJsonResponse(true, this.tradeDTOConverter.convertAll(trades), StringUtils.EMPTY);
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


    //  ----------------- PUT REQUESTS -----------------

    @ResponseBody
    @PutMapping("/disregard")
    public StandardJsonResponse putDisregardTrade(final @RequestBody Map<String, Object> requestBody) {

        validateJsonIntegrity(requestBody, List.of(TRADE_ID), "json did not contain of the required keys : %s", List.of(TRADE_ID));

        String tradeId = requestBody.get(TRADE_ID).toString();
        boolean result = this.tradeService.disregardTrade(tradeId);

        if (!result) {
            throw new GenericSystemException(String.format("An error occurred while updating trade : %s. Please try again", tradeId));
        }

        return new StandardJsonResponse(true, true, StringUtils.EMPTY);
    }
}
