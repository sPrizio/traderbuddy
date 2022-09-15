package com.stephenprizio.traderbuddy.models.records.investing;

import java.time.LocalDate;

/**
 * Class representation of a forecasting entry, which is a singular time period of an executed plan
 *
 * @param startDate start date of period
 * @param endDate  end date of period
 * @param earnings earnings for period
 * @param netEarnings net earnings including previous periods
 * @param balance total balance at end of period
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record ForecastEntry(LocalDate startDate, LocalDate endDate, Double earnings, Double netEarnings, Double balance, Double deposits, Double withdrawals) {

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
