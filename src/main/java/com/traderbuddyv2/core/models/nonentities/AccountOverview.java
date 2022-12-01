package com.traderbuddyv2.core.models.nonentities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class representation of an account's overview
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class AccountOverview {

    @Getter
    @Setter
    private LocalDateTime dateTime;

    @Getter
    @Setter
    private double balance;

    @Getter
    @Setter
    private double monthlyEarnings;

    @Getter
    @Setter
    private double dailyEarnings;

    @Getter
    @Setter
    private double nextTarget;
}
