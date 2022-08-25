package com.stephenprizio.traderbuddy.controllers;

import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.models.nonentities.StandardJsonResponse;
import com.stephenprizio.traderbuddy.services.TradeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.TraderBuddyValidator.*;

/**
 * API Controller for {@link Trade}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/trades")
public class TradeApiController {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @return {@link StandardJsonResponse}
     */
    @GetMapping("/for-type")
    public StandardJsonResponse getTradesForTradeType(final @RequestParam("tradeType") TradeType tradeType) {
        List<Trade> trades = this.tradeService.findAllByTradeType(tradeType);
        validateIfAnyResult(trades, "No trades were found for type %s", tradeType.name());
        return new StandardJsonResponse(true, trades, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing {@link Trade}s for the given interval of time
     *
     * @param start start date & time
     * @param end end date & time
     * @return {@link StandardJsonResponse}
     */
    @GetMapping("/for-interval")
    public StandardJsonResponse getTradesWithinInterval(final @RequestParam("start") String start, final @RequestParam("end") String end) {

        validateLocalDateTimeFormat(start, DATE_FORMAT, "The start date %s was not of the expected format %s", start, DATE_FORMAT);
        validateLocalDateTimeFormat(end, DATE_FORMAT, "The end date %s was not of the expected format %s", end, DATE_FORMAT);

        List<Trade> trades = this.tradeService.findAllTradesWithinDate(LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME), LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME));
        validateIfAnyResult(trades, "No trades were found within interval: [%s, %s]", start, end);

        return new StandardJsonResponse(true, trades, StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link Trade} for the given trade id
     *
     * @param tradeId trade id
     * @return {@link StandardJsonResponse}
     */
    @GetMapping("/for-trade-id")
    public StandardJsonResponse getTradeForTradeId(final @RequestParam("tradeId") String tradeId) {
        Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId);
        validateIfPresent(trade, "No trade was found with trade id: %s", tradeId);
        return new StandardJsonResponse(true, trade.get(), StringUtils.EMPTY);
    }
}
