package com.stephenprizio.traderbuddy.services.cron;

import com.stephenprizio.traderbuddy.models.entities.records.TradeRecord;
import com.stephenprizio.traderbuddy.services.records.TradeRecordService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Cron service to periodically create and update {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradingRecordCronService")
public class TradeRecordCronService {

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


    //  METHODS

    /**
     * Closes trades and generates {@link TradeRecord}s
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void closeTrades() {
        this.tradeRecordService.processTrades();
    }
}
