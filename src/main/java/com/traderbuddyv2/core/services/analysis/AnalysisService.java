package com.traderbuddyv2.core.services.analysis;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.analysis.AnalysisSort;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.nonentities.analysis.TradePerformance;
import com.traderbuddyv2.core.services.trade.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for analyzing trades and performances for insights
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("analysisService")
public class AnalysisService {

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS

    /**
     * Obtains the top performance for {@link Trade}s within the given time span
     *
     * @param start {@link LocalDate} start
     * @param end {@link LocalDate} end
     * @param sort {@link AnalysisSort}
     * @param sortByLosses flag to sort by wins or losses
     * @param count size limit
     * @return {@link List} of {@link TradePerformance}
     */
    public List<TradePerformance> getTopTradePerformance(final LocalDate start, final LocalDate end, final AnalysisSort sort, final boolean sortByLosses, final int count) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(sort, "sort cannot be null");

        final Comparator<TradePerformance> performanceSort = sortByLosses ? sort.getSort() : sort.getSort().reversed();
        final Predicate<Trade> profitFilter = sortByLosses ? trade -> trade.getNetProfit() < 0 : trade -> trade.getNetProfit() >= 0;
        final List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(start.atStartOfDay(), end.atStartOfDay(), false);

        if (CollectionUtils.isNotEmpty(trades)) {
            return trades
                    .stream()
                    .filter(profitFilter)
                    .map(TradePerformance::new)
                    .sorted(performanceSort)
                    .limit(count)
                    .toList();
        }

        return Collections.emptyList();
    }
}
