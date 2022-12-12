package com.traderbuddyv2.core.enums.plans;

import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.services.calculator.CompoundInterestCalculator;
import lombok.Getter;

/**
 * Enum representing the compound frequency for a {@link TradingPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum CompoundFrequency {
    DAILY("Daily", 365),
    DAILY_NO_WEEKENDS("Daily w/o Weekends", CompoundInterestCalculator.daysInYearExcludeWeekends()),
    WEEKLY("Weekly", 52),
    BI_WEEKLY("Bi_Weekly", 26),
    HALF_MONTHLY("Half-Monthly", 24),
    MONTHLY("Monthly", 12),
    BI_MONTHLY("Bi-Monthly", 6),
    QUARTERLY("Quarterly", 4),
    HALF_YEARLY("Half-Yearly", 1),
    YEARLY("Yearly", 1);

    @Getter
    private final String label;

    @Getter
    private final Integer frequency;

    CompoundFrequency(final String label, final Integer frequency) {
        this.label = label;
        this.frequency = frequency;
    }
}
