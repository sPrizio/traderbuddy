package com.traderbuddyv2.core.services.trade.record;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import com.traderbuddyv2.core.repositories.trade.record.TradeRecordStatisticsRepository;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.trade.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link TradeRecordStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeRecordStatisticsService")
public class TradeRecordStatisticsService {

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "tradeRecordStatisticsRepository")
    private TradeRecordStatisticsRepository tradeRecordStatisticsRepository;

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS

    /**
     * Generates a {@link TradeRecordStatistics} entity from a {@link TradeRecord} and a {@link List} of {@link Trade}s
     *
     * @param tradeRecord {@link TradeRecord}
     * @param trades {@link List} of {@link Trade}
     * @return {@link TradeRecordStatistics}
     */
    public TradeRecordStatistics generateStatistics(final TradeRecord tradeRecord, final List<Trade> trades) {

        validateParameterIsNotNull(tradeRecord, CoreConstants.Validation.TRADE_RECORD_CANNOT_BE_NULL);
        validateParameterIsNotNull(trades, "trades cannot be null");

        double profit = this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).sum());
        List<Trade> sortedWinningTrades = trades.stream().filter(t -> t.getNetProfit() >= 0.0).sorted(Comparator.comparing(Trade::getNetProfit).reversed()).toList();
        List<Trade> sortedLosingTrades = trades.stream().filter(t -> t.getNetProfit() < 0.0).sorted(Comparator.comparing(Trade::getNetProfit)).toList();

        final TradeRecordStatistics statistics = tradeRecord.getStatistics() != null ? tradeRecord.getStatistics() : new TradeRecordStatistics();
        final int originalWinningCount = statistics.getNumberOfWinningTrades();
        final int originalLosingCount = statistics.getNumberOfLosingTrades();

        statistics.setNumberOfTrades(statistics.getNumberOfTrades() + trades.size());
        statistics.setNetProfit(this.mathService.add(statistics.getNetProfit(), profit));
        statistics.setNumberOfWinningTrades(statistics.getNumberOfWinningTrades() + sortedWinningTrades.size());
        statistics.setNumberOfLosingTrades(statistics.getNumberOfLosingTrades() + sortedLosingTrades.size());
        statistics.setWinPercentage(this.mathService.wholePercentage(statistics.getNumberOfWinningTrades(), statistics.getNumberOfTrades()));
        statistics.setPercentageProfit(this.mathService.delta(statistics.getNetProfit(), tradeRecord.getBalance()));
        statistics.setPipsEarned(this.mathService.add(statistics.getPipsEarned(), sortedWinningTrades.stream().mapToDouble(trade -> Math.abs(this.mathService.subtract(trade.getOpenPrice(), trade.getClosePrice()))).sum()));
        statistics.setPipsLost(this.mathService.add(statistics.getPipsLost(), sortedLosingTrades.stream().mapToDouble(trade -> Math.abs(this.mathService.subtract(trade.getOpenPrice(), trade.getClosePrice()))).sum()));

        final List<Trade> recordTrades = this.tradeService.findAllTradesForTradeRecord(tradeRecord);
        final Set<LocalDate> dates = new HashSet<>();
        recordTrades.forEach(tr -> dates.add(tr.getTradeOpenTime().toLocalDate()));

        statistics.setTradingRate(this.mathService.divide(recordTrades.size(), dates.size()));

        statistics.setAverageWinAmount(
                this.mathService.weightedAverage(
                        statistics.getAverageWinAmount(),
                        originalWinningCount,
                        this.mathService.getDouble(sortedWinningTrades.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0)),
                        sortedWinningTrades.size()
                )
        );

        statistics.setAverageWinSize(
                this.mathService.weightedAverage(
                        statistics.getAverageWinSize(),
                        originalWinningCount,
                        this.mathService.getDouble(sortedWinningTrades.stream().mapToDouble(Trade::getLotSize).average().orElse(0.0)),
                        sortedWinningTrades.size()
                )
        );

        statistics.setAverageLossAmount(
                this.mathService.weightedAverage(
                        statistics.getAverageLossAmount(),
                        originalLosingCount,
                        this.mathService.getDouble(sortedLosingTrades.stream().mapToDouble(Trade::getNetProfit).average().orElse(0.0)),
                        sortedLosingTrades.size()
                )
        );

        statistics.setAverageLossSize(
                this.mathService.weightedAverage(
                        statistics.getAverageLossSize(),
                        originalLosingCount,
                        this.mathService.getDouble(sortedLosingTrades.stream().mapToDouble(Trade::getLotSize).average().orElse(0.0)),
                        sortedLosingTrades.size()
                )
        );

        if (CollectionUtils.isNotEmpty(sortedWinningTrades)) {
            double largest = this.mathService.getDouble(sortedWinningTrades.get(0).getNetProfit());
            if (largest > statistics.getLargestWinAmount()) {
                statistics.setLargestWinAmount(this.mathService.getDouble(largest));
                statistics.setLargestWinSize(this.mathService.getDouble(sortedWinningTrades.get(0).getLotSize()));
            }
        }

        if (CollectionUtils.isNotEmpty(sortedLosingTrades)) {
            double largest = this.mathService.getDouble(sortedLosingTrades.get(0).getNetProfit());
            if (largest < statistics.getLargestLossAmount()) {
                statistics.setLargestLossAmount(largest);
                statistics.setLargestLossSize(this.mathService.getDouble(sortedLosingTrades.get(0).getLotSize()));
            }
        }

        return this.tradeRecordStatisticsRepository.save(statistics);
    }
}
