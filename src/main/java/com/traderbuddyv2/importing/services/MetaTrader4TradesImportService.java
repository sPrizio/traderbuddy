package com.traderbuddyv2.importing.services;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.importing.ImportService;
import com.traderbuddyv2.importing.exceptions.TradeImportFailureException;
import com.traderbuddyv2.importing.records.MetaTrade4TradeWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
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

            Map<String, Trade> tradeMap = new HashMap<>();
            Map<String, Trade> existingTrades = new HashMap<>();

        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new TradeImportFailureException(String.format("The import process failed with reason : %s", e.getMessage()), e);
        }
    }

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
}
