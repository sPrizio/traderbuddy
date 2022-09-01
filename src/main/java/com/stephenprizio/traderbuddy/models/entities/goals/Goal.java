package com.stephenprizio.traderbuddy.models.entities.goals;

import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingRecord;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class representation of a target/goal. In this system a Goal is the end result of a trading plan. A {@link Goal} will be used
 * to assess the effectiveness of individual {@link Trade}s as part of a {@link TradingRecord}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private String name;

    @Getter
    @Setter
    @Column
    private LocalDate startDate;

    @Getter
    @Setter
    @Column
    private LocalDate endDate;

    @Getter
    @Setter
    @Column
    private Double profitTarget;

    @Getter
    @Setter
    @Column
    private Boolean active;

    @Getter
    @Setter
    @Column
    private GoalStatus status;
}
