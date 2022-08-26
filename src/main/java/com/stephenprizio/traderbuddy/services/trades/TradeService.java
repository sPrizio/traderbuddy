package com.stephenprizio.traderbuddy.services.trades;

import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.repositories.TradeRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.TraderBuddyValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.TraderBuddyValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeService")
public class TradeService {

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllByTradeType(TradeType tradeType) {
        validateParameterIsNotNull(tradeType, "tradeType cannot be null");

        return this.tradeRepository.findAllByTradeType(tradeType);
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllTradesWithinDate(LocalDateTime start, LocalDateTime end) {
        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        return this.tradeRepository.findAllTradesWithinDate(start, end);
    }

    /**
     * Returns an {@link Optional} containing a {@link Trade}
     *
     * @param tradeId trade id
     * @return {@link Optional} {@link Trade}
     */
    public Optional<Trade> findTradeByTradeId(String tradeId) {
        validateParameterIsNotNull(tradeId, "tradeId cannot be null");

        return Optional.ofNullable(this.tradeRepository.findTradeByTradeId(tradeId));
    }
}
