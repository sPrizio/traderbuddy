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
import com.traderbuddyv2.importing.records.MetaTrade4TradeWrapper;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service-layer for importing trades into the system from the MetaTrader4 platform
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("metaTrader4TradesImportService")
public class MetaTrader4TradesImportService implements ImportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MetaTrader4TradesImportService.class);
    private static final List<String> BUY_SIGNALS = List.of("buy");
    private static final List<String> SELL_SIGNALS = List.of("sell");

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    /**
     * Imports trades from a CSV file from the CMC platform
     *
     * @param filePath  file path
     * @param delimiter unit delimiter
     */
    @Override
    public void importTrades(String filePath, Character delimiter) {
        try {
            importFile(new BufferedReader(new FileReader(ResourceUtils.getFile(filePath))));
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Imports trades from a CSV file from the CMC platform
     *
     * @param inputStream {@link InputStream}
     * @param delimiter   unit delimiter
     */
    @Override
    public void importTrades(InputStream inputStream, Character delimiter) {
        importFile(new BufferedReader(new InputStreamReader(inputStream)));
    }


    //  HELPERS

    /**
     * Imports a file using the given {@link BufferedReader} and delimiter
     *
     * @param bufferedReader {@link BufferedReader}
     */
    private void importFile(final BufferedReader bufferedReader) {

        try (bufferedReader) {
            final StringBuilder stringBuilder = new StringBuilder();
            bufferedReader.lines().forEach(stringBuilder::append);

            final List<String> content = getContent(stringBuilder.toString());
            List<MetaTrade4TradeWrapper> trades =
                    content
                            .stream()
                            .map(this::generateWrapper)
                            .filter(Objects::nonNull)
                            .sorted(Comparator.comparing(MetaTrade4TradeWrapper::getOpenTime))
                            .toList();

            final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
            final Map<String, Trade> tradeMap = new HashMap<>();
            final Map<String, Trade> existingTrades = new HashMap<>();

            this.tradeRepository.findAllByAccount(account).forEach(trade -> existingTrades.put(trade.getTradeId(), trade));
            final List<MetaTrade4TradeWrapper> buyTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.ticketNumber())).filter(trade -> matchTradeType(trade.type(), TradeType.BUY)).toList();
            final List<MetaTrade4TradeWrapper> sellTrades = trades.stream().filter(trade -> !existingTrades.containsKey(trade.ticketNumber())).filter(trade -> matchTradeType(trade.type(), TradeType.SELL)).toList();

            buyTrades.forEach(trade -> tradeMap.put(trade.ticketNumber(), createNewTrade(trade, TradeType.BUY)));
            sellTrades.forEach(trade -> tradeMap.put(trade.ticketNumber(), createNewTrade(trade, TradeType.SELL)));

            tradeMap.values().forEach(account::addTrade);
            this.accountRepository.save(account);
            this.tradeRecordService.processTrades();
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

    /**
     * Obtains trade content from the file content
     *
     * @param string file content
     * @return {@link List} of trade content strings
     */
    private List<String> getContent(final String string) {

        final int ticketIndex = string.indexOf("Ticket");
        if (ticketIndex == -1) {
            throw new TradeImportFailureException("No valid trades were given to import");
        }

        final int startIndex = string.indexOf("<tr", ticketIndex);
        if (startIndex == -1) {
            throw new TradeImportFailureException("The import file is not properly formatted");
        }

        final String tradeContent = string.substring(startIndex, string.indexOf("Closed P/L:"));
        final Pattern pattern = Pattern.compile("<tr.*?>(.*?)<\\/tr>");
        final Matcher matcher = pattern.matcher(tradeContent);

        final List<String> entries = new ArrayList<>();
        while (matcher.find()) {
            entries.add(
                    matcher.group()
                            .replaceAll("<tr.*?>", StringUtils.EMPTY)
                            .replace("</tr>", StringUtils.EMPTY)
                            .trim()
            );
        }

        return entries;
    }

    /**
     * Generates a {@link MetaTrade4TradeWrapper} from the given string
     *
     * @param string input value
     * @return {@link MetaTrade4TradeWrapper}
     */
    private MetaTrade4TradeWrapper generateWrapper(final String string) {

        if (StringUtils.isEmpty(string)) {
            return null;
        }

        final Pattern pattern = Pattern.compile("<td.*?>(.*?)<\\/td>");
        final Matcher matcher = pattern.matcher(string);
        final List<String> data = new ArrayList<>();

        while (matcher.find()) {
            data.add(
                    matcher.group()
                            .replaceAll("<td.*?>", StringUtils.EMPTY)
                            .replace("</td>", StringUtils.EMPTY)
                            .trim()
            );
        }

        if (data.size() != 14) {
            return null;
        }

        return new MetaTrade4TradeWrapper(
                data.get(0),
                LocalDateTime.parse(data.get(1), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
                LocalDateTime.parse(data.get(8), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")),
                data.get(2),
                Double.parseDouble(data.get(3)),
                data.get(4),
                Double.parseDouble(data.get(5)),
                Double.parseDouble(data.get(6)),
                Double.parseDouble(data.get(7)),
                Double.parseDouble(data.get(9)),
                Double.parseDouble(data.get(13))
        );
    }

    /**
     * Determines whether the given trade should be considered
     *
     * @param trade trade name
     * @param tradeType {@link TradeType}
     * @return true if matches keywords
     */
    private boolean matchTradeType(final String trade, final TradeType tradeType) {
        final List<String> matchers = tradeType.equals(TradeType.BUY) ? BUY_SIGNALS : SELL_SIGNALS;
        return matchers.stream().anyMatch(trade::contains);
    }

    /**
     * Creates a new {@link Trade} from a {@link MetaTrade4TradeWrapper}
     *
     * @param wrapper {@link MetaTrade4TradeWrapper}
     * @param tradeType {@link TradeType}
     * @return {@link Trade}
     */
    private Trade createNewTrade(final MetaTrade4TradeWrapper wrapper, final TradeType tradeType) {

        Trade trade = new Trade();

        trade.setTradeId(wrapper.ticketNumber());
        trade.setTradePlatform(TradePlatform.METATRADER4);
        trade.setResultsOfTrade(List.of());
        trade.setProduct(wrapper.item());
        trade.setTradeType(tradeType);
        trade.setClosePrice(wrapper.closePrice());
        trade.setTradeCloseTime(wrapper.closeTime());
        trade.setTradeOpenTime(wrapper.openTime());
        trade.setLotSize(wrapper.size());
        trade.setNetProfit(wrapper.profit());
        trade.setOpenPrice(wrapper.openPrice());
        trade.setReasonsForEntry(List.of());
        trade.setStopLoss(wrapper.stopLoss());
        trade.setTakeProfit(wrapper.takeProfit());
        trade.setRelevant(true);
        trade.setProcessed(false);

        return trade;
    }
}
