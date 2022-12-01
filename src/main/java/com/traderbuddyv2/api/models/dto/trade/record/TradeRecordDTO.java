package com.traderbuddyv2.api.models.dto.trade.record;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * A DTO representation of a {@link TradeRecord}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeRecordDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    private AggregateInterval aggregateInterval;

    @Getter
    @Setter
    private double balance;

    @Getter
    @Setter
    private TradeRecordStatisticsDTO statistics;

    @Getter
    @Setter
    private AccountDTO account;
}
