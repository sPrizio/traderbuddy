package com.stephenprizio.traderbuddy.models.records.investing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Represents a collection of statistics for a collection of {@link ForecastEntry}s
 *
 * @param entries {@link List} of {@link ForecastEntry}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record ForecastStatistics(Double startingBalance, List<ForecastEntry> entries) {


    //  METHODS

    /**
     * Obtains the sum of deposits
     *
     * @return {@link Double}
     */
    public Double getTotalDeposits() {

        double sum =
                this.entries
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(ForecastEntry::deposits)
                        .sum();

        return
                BigDecimal
                        .valueOf(sum)
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Obtains the sum of earnings
     *
     * @return {@link Double}
     */
    public Double getTotalEarnings() {

        double earnings =
                this.entries
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(ForecastEntry::earnings)
                        .sum();

        return
                BigDecimal
                        .valueOf(earnings)
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Obtains the sum of netEarnings
     *
     * @return {@link Double}
     */
    public Double getTotalNetEarnings() {
        return
                BigDecimal
                        .valueOf(this.getTotalEarnings())
                        .add(BigDecimal.valueOf(this.getTotalDeposits()))
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Obtains the sum of withdrawals
     *
     * @return {@link Double}
     */
    public Double getTotalWithdrawals() {

        double withdrawals =
                this.entries
                        .stream()
                        .filter(Objects::nonNull)
                        .mapToDouble(ForecastEntry::withdrawals)
                        .sum();

        return
                BigDecimal
                        .valueOf(withdrawals)
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Obtains the sum of balance
     *
     * @return {@link Double}
     */
    public Double getTotalBalance() {

        if (this.entries.isEmpty()) {
            return 0.0;
        }

        return
                BigDecimal
                        .valueOf(this.entries.get(this.entries.size() - 1).balance())
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }
}
