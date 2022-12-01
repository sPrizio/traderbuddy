package com.traderbuddyv2.core.models.records;

import lombok.Getter;

import java.time.LocalDate;

/**
 * Class representation of a forecasting entry, which is a singular time period of an executed plan
 *
 * @param startDate   start date of period
 * @param endDate     end date of period
 * @param earnings    earnings for period
 * @param netEarnings net earnings including previous periods
 * @param balance     total balance at end of period
 * @param deposits    credits
 * @param withdrawals debits
 * @param goal        plan target
 * @author Stephen Prizio
 * @version 1.0
 */
public record ForecastEntry(
        @Getter LocalDate startDate,
        @Getter LocalDate endDate,
        @Getter Double earnings,
        @Getter Double netEarnings,
        @Getter Double balance,
        @Getter Double deposits,
        @Getter Double withdrawals,
        @Getter Double goal
) {

    /**
     * Returns true if the current date is between the start and end date
     *
     * @return true if current date > start date and current date < end date
     */
    public Boolean getActive() {
        LocalDate now = LocalDate.now();
        return (now.isAfter(this.startDate) || now.isEqual(this.startDate)) && (now.isBefore(this.endDate));
    }
}
