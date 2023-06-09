package com.traderbuddyv2.importing.services;

import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Generic importing service to handle incoming files, will delegate to specific import services
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("genericImportService")
public class GenericImportService {

    private final CMCMarketsTradesImportService cmcMarketsTradesImportService;

    private final MetaTrader4TradesImportService metaTrader4TradesImportService;

    private final TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Autowired
    public GenericImportService(final CMCMarketsTradesImportService cmcMarketsTradesImportService, final MetaTrader4TradesImportService metaTrader4TradesImportService, final TraderBuddyUserDetailsService traderBuddyUserDetailsService) {
        this.cmcMarketsTradesImportService = cmcMarketsTradesImportService;
        this.metaTrader4TradesImportService = metaTrader4TradesImportService;
        this.traderBuddyUserDetailsService = traderBuddyUserDetailsService;
    }


    //  METHODS

    /**
     * Imports a {@link MultipartFile} for the given {@link TradePlatform}
     *
     * @param inputStream {@link InputStream}
     * @return import message
     */
    public String importTrades(InputStream inputStream) {

        validateParameterIsNotNull(inputStream, "import stream cannot be null");

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        try {
            if (account.getTradePlatform().equals(TradePlatform.CMC_MARKETS)) {
                this.cmcMarketsTradesImportService.importTrades(inputStream, ',');
                return StringUtils.EMPTY;
            } else if (account.getTradePlatform().equals(TradePlatform.METATRADER4)) {
                this.metaTrader4TradesImportService.importTrades(inputStream, null);
                return StringUtils.EMPTY;
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return String.format("Trading platform %s is not currently supported", account.getTradePlatform());
    }
}
