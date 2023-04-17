package com.traderbuddyv2.importing.services;

import com.traderbuddyv2.core.enums.trade.info.TradeType;
import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.repositories.account.AccountRepository;
import com.traderbuddyv2.core.repositories.trade.TradeRepository;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import com.traderbuddyv2.importing.ImportService;
import com.traderbuddyv2.importing.exceptions.TradeImportFailureException;
import com.traderbuddyv2.importing.records.CMCTradeWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Service-layer for importing trades into the system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("cmcTradesImportService")
public class CMCTradesImportService implements ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CMCTradesImportService.class);

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;


    //  METHODS

    /**
     * Imports trades from a CSV file from the CMC platform
     *
     * @param filePath file path
     * @param delimiter unit delimiter
     */
    @Override
    public void importTrades(final String filePath, final Character delimiter) {
        try {
            importFile(new BufferedReader(new FileReader(ResourceUtils.getFile(filePath))), delimiter);
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Imports trades from a CSV file from the CMC platform
     *
     * @param inputStream {@link InputStream}
     * @param delimiter unit delimiter
     */
    @Override
    public void importTrades(final InputStream inputStream, final Character delimiter) {
        importFile(new BufferedReader(new InputStreamReader(inputStream)), delimiter);
    }


    //  HELPERS

    /**
     * Imports a file using the given {@link BufferedReader} and delimiter
     *
     * @param bufferedReader {@link BufferedReader}
     * @param delimiter unit delimiter
     */
    private void importFile(final BufferedReader bufferedReader, final Character delimiter) {
        try (bufferedReader) {
            List<CMCTradeWrapper> trades =
                    bufferedReader
                            .lines()
                            .skip(1)
                            .map(line -> this.generateWrapperFromString(line, delimiter))
                            .filter(Objects::nonNull)
                            .toList();

            Map<String, Trade> tradeMap = new HashMap<>();
            Map<String, Trade> existingTrades = new HashMap<>();

            final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
            this.tradeRepository.findAllByAccount(account).forEach(trade -> existingTrades.put(trade.getTradeId(), trade));

            List<CMCTradeWrapper> buyTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.orderNumber())).filter(trade -> trade.type().contains("Buy Trade")).toList();
            List<CMCTradeWrapper> sellTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.orderNumber())).filter(trade -> trade.type().contains("Sell Trade")).toList();
            List<CMCTradeWrapper> closeTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.orderNumber())).filter(trade -> trade.type().equals("Close Trade")).toList();
            List<CMCTradeWrapper> stopLosses = trades.stream().filter(trade -> !existingTrades.containsKey(trade.orderNumber())).filter(trade -> trade.type().equals("Stop Loss")).toList();
            List<CMCTradeWrapper> takeProfits = trades.stream().filter(trade -> !existingTrades.containsKey(trade.orderNumber())).filter(trade -> trade.type().equals("Take Profit")).toList();
            List<CMCTradeWrapper> promotionalPayments = trades.stream().filter(trade -> !existingTrades.containsKey(trade.orderNumber())).filter(trade -> trade.type().equals("Promotional Payment")).toList();

            buyTrades.forEach(trade -> tradeMap.put(trade.orderNumber(), createNewTrade(trade, TradeType.BUY)));
            sellTrades.forEach(trade -> tradeMap.put(trade.orderNumber(), createNewTrade(trade, TradeType.SELL)));
            closeTrades.stream().filter(trade -> tradeMap.containsKey(trade.relatedOrderNumber())).forEach(trade -> tradeMap.put(trade.relatedOrderNumber(), updateTrade(trade, tradeMap.get(trade.relatedOrderNumber()))));
            stopLosses.stream().filter(trade -> tradeMap.containsKey(trade.orderNumber())).forEach(trade -> tradeMap.put(trade.orderNumber(), updateTrade(trade, tradeMap.get(trade.orderNumber()))));
            takeProfits.stream().filter(trade -> tradeMap.containsKey(trade.orderNumber())).forEach(trade -> tradeMap.put(trade.orderNumber(), updateTrade(trade, tradeMap.get(trade.orderNumber()))));
            promotionalPayments.forEach(trade -> tradeMap.put(trade.orderNumber(), createPromotionalPayment(trade)));

            tradeMap.values().forEach(account::addTrade);
            this.accountRepository.save(account);
            this.tradeRecordService.processTrades();
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Generates a {@link CMCTradeWrapper} from a CSV string
     *
     * @param string csv string
     * @return {@link CMCTradeWrapper}
     */
    private CMCTradeWrapper generateWrapperFromString(final String string, final Character delimiter) {

        try {
            String[] array = string.replace("(T) ", StringUtils.EMPTY).replace("(T)", StringUtils.EMPTY).split(delimiter.toString() + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            LocalDateTime dateTime = getTradeTime(sanitizeString(array[0]));
            String type = sanitizeString(array[1]);
            String orderNumber = sanitizeString(array[2]);
            String relatedOrderNumber = sanitizeString(array[4]);
            String product = sanitizeString(array[5]);
            double units = safeParseDouble(sanitizeString(array[6]));
            double price = safeParseDouble(sanitizeString(array[7]));
            double amount = safeParseDouble(sanitizeString(array[14]));

            return new CMCTradeWrapper(dateTime, type, orderNumber, relatedOrderNumber, product, units, price, amount);
        } catch (Exception e) {
            LOGGER.error("Error parsing line : {} for reason : {}", string, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Attempts to compute a {@link LocalDateTime} for the given string
     *
     * @param string date time string
     * @return {@link LocalDateTime}
     */
    private LocalDateTime getTradeTime(String string) {
        try {
            return LocalDateTime.parse(string, DateTimeFormatter.ofPattern("dd/MM/yyyy H:mm"));
        } catch (Exception e) {
            try {
                String[] input = string.split(" ");
                input[1] = input[1] + ".";
                return LocalDateTime.parse(String.join(" ", input), DateTimeFormatter.ofPattern("dd MMM yyyy H:mm:ss"));
            } catch (Exception ex) {
                throw new DateTimeException(e.getMessage(), e);
            }
        }
    }

    /**
     * Sanitizes the given string
     *
     * @param string input string
     * @return sanitized string
     */
    private String sanitizeString(String string) {
        return string.replace("\"", StringUtils.EMPTY);
    }

    /**
     * Safely parses a {@link String} into a {@link Double}
     *
     * @param string {@link String}
     * @return {@link Double}
     */
    private double safeParseDouble(final String string) {

        if (StringUtils.isEmpty(string) || string.equals("-")) {
            return 0.0;
        }

        return Double.parseDouble(string.replaceAll("[^\\d.-]", StringUtils.EMPTY).trim());
    }

    /**
     * Creates a new {@link Trade} from a {@link CMCTradeWrapper}
     *
     * @param wrapper {@link CMCTradeWrapper}
     * @param tradeType {@link TradeType}
     * @return {@link Trade}
     */
    private Trade createNewTrade(final CMCTradeWrapper wrapper, final TradeType tradeType) {

        Trade trade = new Trade();

        trade.setTradeId(wrapper.orderNumber());
        trade.setTradePlatform(TradePlatform.CMC_MARKETS);
        trade.setResultsOfTrade(List.of());
        trade.setProduct(wrapper.product());
        trade.setTradeType(tradeType);
        trade.setClosePrice(0.0);
        trade.setTradeCloseTime(null);
        trade.setTradeOpenTime(wrapper.dateTime());
        trade.setLotSize(wrapper.units());
        trade.setNetProfit(0.0);
        trade.setOpenPrice(wrapper.price());
        trade.setReasonsForEntry(List.of());
        trade.setRelevant(true);
        trade.setProcessed(false);

        return trade;
    }

    /**
     * Creates a new {@link Trade} representing a promotional payment from a {@link CMCTradeWrapper}
     *
     * @param wrapper {@link CMCTradeWrapper}
     * @return {@link Trade}
     */
    private Trade createPromotionalPayment(final CMCTradeWrapper wrapper) {

        Trade trade = new Trade();

        trade.setTradeId(wrapper.orderNumber());
        trade.setTradePlatform(TradePlatform.CMC_MARKETS);
        trade.setResultsOfTrade(List.of());
        trade.setTradeType(TradeType.PROMOTIONAL_PAYMENT);
        trade.setClosePrice(0.0);
        trade.setTradeCloseTime(wrapper.dateTime());
        trade.setTradeOpenTime(wrapper.dateTime());
        trade.setLotSize(0.0);
        trade.setNetProfit(wrapper.amount());
        trade.setOpenPrice(0.0);
        trade.setReasonsForEntry(List.of());
        trade.setRelevant(true);

        return trade;
    }

    /**
     * Updates an existing {@link Trade} with a {@link CMCTradeWrapper}
     *
     * @param wrapper {@link CMCTradeWrapper}
     * @param matched pre-existing {@link Trade}
     * @return updated {@link Trade}
     */
    private Trade updateTrade(final CMCTradeWrapper wrapper, final Trade matched) {

        matched.setClosePrice(wrapper.price());
        matched.setTradeCloseTime(wrapper.dateTime());
        matched.setNetProfit(wrapper.amount());

        return matched;
    }
}
