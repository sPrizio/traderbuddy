package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import com.stephenprizio.traderbuddy.models.records.reporting.retrospectives.RetrospectiveStatistics;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingSummary;
import com.stephenprizio.traderbuddy.services.investing.InvestingService;
import com.stephenprizio.traderbuddy.services.plans.TradingPlanService;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Converter for {@link Retrospective}s into {@link RetrospectiveDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveDTOConverter")
public class RetrospectiveDTOConverter implements GenericDTOConverter<Retrospective, RetrospectiveDTO> {

    @Resource(name = "investingService")
    private InvestingService investingService;

    @Resource(name = "retrospectiveEntryDTOConverter")
    private RetrospectiveEntryDTOConverter retrospectiveEntryDTOConverter;

    @Resource(name = "tradingPlanService")
    private TradingPlanService tradingPlanService;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public RetrospectiveDTO convert( final Retrospective entity) {

        if (entity == null) {
            return new RetrospectiveDTO();
        }

        RetrospectiveDTO retrospectiveDTO = new RetrospectiveDTO();

        retrospectiveDTO.setStartDate(entity.getStartDate());
        retrospectiveDTO.setEndDate(entity.getEndDate());
        retrospectiveDTO.setIntervalFrequency(entity.getIntervalFrequency());
        retrospectiveDTO.setUid(this.uniqueIdentifierService.generateUid(entity));

        if (CollectionUtils.isNotEmpty(entity.getPoints())) {
            retrospectiveDTO.setPoints(this.retrospectiveEntryDTOConverter.convertAll(entity.getPoints()));
        }

        Optional<TradingPlan> tradingPlan = this.tradingPlanService.findCurrentlyActiveTradingPlan();
        tradingPlan.ifPresent(plan -> {
            TradingSummary tradingSummary = this.investingService.obtainTradingPerformanceForForecast(tradingPlan.get(), AggregateInterval.DAILY, entity.getStartDate(), entity.getEndDate());
            if (tradingSummary != null && CollectionUtils.isNotEmpty(tradingSummary.records())) {
                long daysCount = tradingSummary.records().stream().filter(tr -> tr.getTotalNumberOfTrades() > 0).count();
                double tradingRate = daysCount > 0 ? BigDecimal.valueOf(tradingSummary.records().stream().mapToInt(TradingRecord::getTotalNumberOfTrades).sum()).divide(BigDecimal.valueOf(daysCount), 10, RoundingMode.HALF_EVEN).setScale(2, RoundingMode.HALF_EVEN).doubleValue() : 0.0;

                retrospectiveDTO.setRetrospectiveStatistics(
                        new RetrospectiveStatistics(
                                tradingSummary.records().stream().mapToInt(TradingRecord::getTotalNumberOfTrades).sum(),
                                tradingRate,
                                BigDecimal.valueOf(tradingSummary.records().stream().mapToInt(TradingRecord::getWinPercentage).filter(i -> i != 0).average().orElse(0.0)).setScale(0, RoundingMode.HALF_EVEN).intValue(),
                                BigDecimal.valueOf(tradingSummary.records().stream().mapToDouble(TradingRecord::getNetProfit).sum()).setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                                BigDecimal.valueOf(tradingSummary.records().stream().mapToDouble(TradingRecord::percentageProfit).filter(i -> i != 0).average().orElse(0.0)).setScale(2, RoundingMode.HALF_EVEN).doubleValue()
                        )
                );
            }
        });

        return retrospectiveDTO;
    }
}
