package com.traderbuddyv2.api.converters.trade.record;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.record.TradeRecordStatisticsDTO;
import com.traderbuddyv2.api.models.records.IntradayEquityCurvePoint;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Converts {@link TradeRecordStatistics}s into {@link TradeRecordStatisticsDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeRecordStatisticsDTOConverter")
public class TradeRecordStatisticsDTOConverter implements GenericDTOConverter<TradeRecordStatistics, TradeRecordStatisticsDTO> {

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "tradeService")
    private TradeService tradeService;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TradeRecordStatisticsDTO convert(final TradeRecordStatistics entity) {

        if (entity == null) {
            return new TradeRecordStatisticsDTO();
        }

        TradeRecordStatisticsDTO tradeRecordStatisticsDTO = new TradeRecordStatisticsDTO();

        tradeRecordStatisticsDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        tradeRecordStatisticsDTO.setNumberOfTrades(entity.getNumberOfTrades());
        tradeRecordStatisticsDTO.setNetProfit(this.mathService.getDouble(entity.getNetProfit()));
        tradeRecordStatisticsDTO.setWinPercentage(entity.getWinPercentage());
        tradeRecordStatisticsDTO.setNumberOfWinningTrades(entity.getNumberOfWinningTrades());
        tradeRecordStatisticsDTO.setNumberOfLosingTrades(entity.getNumberOfLosingTrades());
        tradeRecordStatisticsDTO.setAverageWinAmount(entity.getAverageWinAmount());
        tradeRecordStatisticsDTO.setAverageWinSize(entity.getAverageWinSize());
        tradeRecordStatisticsDTO.setAverageLossAmount(entity.getAverageLossAmount());
        tradeRecordStatisticsDTO.setAverageLossSize(entity.getAverageLossSize());
        tradeRecordStatisticsDTO.setLargestWinAmount(entity.getLargestWinAmount());
        tradeRecordStatisticsDTO.setLargestWinSize(entity.getLargestWinSize());
        tradeRecordStatisticsDTO.setLargestLossAmount(entity.getLargestLossAmount());
        tradeRecordStatisticsDTO.setLargestLossSize(entity.getLargestLossSize());
        tradeRecordStatisticsDTO.setTradingRate(entity.getTradingRate());
        tradeRecordStatisticsDTO.setPercentageProfit(entity.getPercentageProfit());
        tradeRecordStatisticsDTO.setPipsEarned(entity.getPipsEarned());
        tradeRecordStatisticsDTO.setPipsLost(entity.getPipsLost());
        tradeRecordStatisticsDTO.setNetPips(this.mathService.subtract(entity.getPipsEarned(), entity.getPipsLost()));
        tradeRecordStatisticsDTO.setProfitability(this.mathService.divide(entity.getPipsEarned(), entity.getPipsLost()));

        computeDeltas(tradeRecordStatisticsDTO, entity);
        computePoints(tradeRecordStatisticsDTO, entity);

        return tradeRecordStatisticsDTO;
    }


    //  HELPERS

    /**
     * Compute advanced statistics
     *
     * @param tradeRecordStatisticsDTO {@link TradeRecordStatisticsDTO}
     * @param entity {@link TradeRecordStatistics}
     */
    private void computeDeltas(final TradeRecordStatisticsDTO tradeRecordStatisticsDTO, final TradeRecordStatistics entity) {
        final List<TradeRecord> tradeRecords = this.tradeRecordService.findRecentHistory(-1, entity.getTradeRecord().getAggregateInterval());
        Optional<TradeRecord> previous = tradeRecords.stream().filter(r -> r.getEndDate().isEqual(entity.getTradeRecord().getStartDate())).findFirst();
        previous.ifPresent(rec -> {
            tradeRecordStatisticsDTO.setAverageWinDelta(this.mathService.percentageChange(entity.getAverageWinAmount(), rec.getStatistics().getAverageWinAmount()));
            tradeRecordStatisticsDTO.setLargestWinDelta(this.mathService.percentageChange(entity.getLargestWinAmount(), rec.getStatistics().getLargestWinAmount()));
            tradeRecordStatisticsDTO.setAverageLossDelta(this.mathService.percentageChange(entity.getAverageLossAmount(), rec.getStatistics().getAverageLossAmount()));
            tradeRecordStatisticsDTO.setLargestLossDelta(this.mathService.percentageChange(entity.getLargestLossAmount(), rec.getStatistics().getLargestLossAmount()));
        });
    }

    /**
     * Computes points for an equity curve graph
     *
     * @param tradeRecordStatisticsDTO {@link TradeRecordStatisticsDTO}
     * @param entity {@link TradeRecordStatistics}
     */
    private void computePoints(final TradeRecordStatisticsDTO tradeRecordStatisticsDTO, final TradeRecordStatistics entity) {
        final List<IntradayEquityCurvePoint> points = new ArrayList<>();
        final List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(entity.getTradeRecord().getStartDate().atStartOfDay(), entity.getTradeRecord().getEndDate().atStartOfDay(), false);

        if (CollectionUtils.isNotEmpty(trades)) {
            tradeRecordStatisticsDTO.setGrossWinAmount(this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d >= 0.0).sum()));
            tradeRecordStatisticsDTO.setGrossLossAmount(this.mathService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0.0).sum()));

            //  start every chart with a point at 0 to keep it smooth
            points.add(0, new IntradayEquityCurvePoint(trades.get(0).getTradeOpenTime().minusDays(2), 0));

            double sum = 0.0;
            for (Trade trade : trades) {
                sum = this.mathService.add(computePoints(trade), sum);
                points.add(new IntradayEquityCurvePoint(trade.getTradeCloseTime(), sum));
            }
        }

        tradeRecordStatisticsDTO.setPoints(points);
    }

    /**
     * Computes the pips within a {@link Trade}
     *
     * @param trade {@link Trade}
     * @return pips gained/lost
     */
    private double computePoints(final Trade trade) {
        if (trade.getNetProfit() >= 0.0) {
            return Math.abs(this.mathService.subtract(trade.getOpenPrice(), trade.getClosePrice()));
        } else {
            return Math.abs(this.mathService.subtract(trade.getOpenPrice(), trade.getClosePrice())) * -1.0;
        }
    }
}
