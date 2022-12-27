package com.traderbuddyv2.api.models.dto.retrospective;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.api.models.dto.trade.record.TradeRecordDTO;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * A DTO representation of {@link Retrospective}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class RetrospectiveDTO implements GenericDTO {

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
    private AggregateInterval intervalFrequency;

    @Getter
    @Setter
    private List<RetrospectiveEntryDTO> points;

    @Getter
    @Setter
    private AccountDTO account;

    @Getter
    @Setter
    private TradeRecordDTO totals;
}
