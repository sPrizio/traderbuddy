package com.traderbuddyv2.core.models.entities.trade.record;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class representation of a record of trades over 1 calendar period
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "trade_records")
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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "statistics_id")
    private TradeRecordStatistics statistics;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TradeRecord that = (TradeRecord) o;

        if (!startDate.equals(that.startDate)) return false;
        if (!endDate.equals(that.endDate)) return false;
        if (aggregateInterval != that.aggregateInterval) return false;
        return this.account.getId().equals(that.account.getId());
    }

    @Override
    public int hashCode() {
        int result = startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        result = 31 * result + aggregateInterval.hashCode();
        result = 31 * result + account.hashCode();
        return result;
    }
}

