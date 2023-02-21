package com.traderbuddyv2.core.models.nonentities.analysis.bucket;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Class representation of a breakdown of trades for a given time span
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeDateBucket extends Bucket {

    @Getter
    @Setter
    private LocalDate start;

    @Getter
    @Setter
    private LocalDate end;

    @Getter
    @Setter
    private Double averagePips;

    @Getter
    @Setter
    private Double averageProfit;


    //  CONSTRUCTORS

    public TradeDateBucket(final LocalDate start, final LocalDate end, final List<Trade> trades) {
        super(trades);
        this.start = start;
        this.end = end;

        double count = (double) trades.stream().map(Trade::getTradeOpenTime).map(LocalDateTime::toLocalDate).distinct().count();
        this.averagePips = computeAverage(this.getPips(), count);
        this.averageProfit = computeAverage(this.getNetProfit(), count);
    }


    //  HELPERS

    /**
     * Computes the average of the 2 numbers
     *
     * @param sum sum
     * @param count count
     * @return average
     */
    private double computeAverage(final double sum, final double count) {

        if (count == 0) {
            return 0.0;
        }

        return BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(count), 10, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
