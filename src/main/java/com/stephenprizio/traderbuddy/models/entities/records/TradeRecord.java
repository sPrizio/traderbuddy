package com.stephenprizio.traderbuddy.models.entities.records;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class representation of a record of trades over 1 calendar day
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "trading_records")
public class TradeRecord implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private AggregateInterval aggregateInterval;

    @Getter
    @Setter
    @Column
    private double balance;

    @Getter
    @Setter
    @Column
    private int numberOfTrades;

    @Getter
    @Setter
    @Column
    private double netProfit;

    @Getter
    @Setter
    @Column
    private double percentageProfit;

    @Getter
    @Setter
    @Column
    private int winPercentage;
}
