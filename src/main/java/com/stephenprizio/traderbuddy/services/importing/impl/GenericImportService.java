package com.stephenprizio.traderbuddy.services.importing.impl;

import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Generic importing service to handle incoming files, will delegate to specific import services
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("genericImportService")
public class GenericImportService {

    private final CMCTradesImportService cmcTradesImportService;

    @Autowired
    public GenericImportService(CMCTradesImportService cmcTradesImportService) {
        this.cmcTradesImportService = cmcTradesImportService;
    }


    //  METHODS

    /**
     * Imports a {@link MultipartFile} for the given {@link TradingPlatform}
     *
     * @param inputStream {@link InputStream}
     * @param delimiter unit delimiter
     * @param platform {@link TradingPlatform}
     * @return import message
     */
    public String importTrades(InputStream inputStream, Character delimiter, TradingPlatform platform) {

        validateParameterIsNotNull(inputStream, "import stream cannot be null");
        validateParameterIsNotNull(delimiter, "delimiter cannot be null");
        validateParameterIsNotNull(platform, "trading platform cannot be null");

        try {
            if (platform.equals(TradingPlatform.CMC_MARKETS)) {
                this.cmcTradesImportService.importTrades(inputStream, delimiter);
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return String.format("Trading platform %s is not currently supported", platform.name());
    }
}
