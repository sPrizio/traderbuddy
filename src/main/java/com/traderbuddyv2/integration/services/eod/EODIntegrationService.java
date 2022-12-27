package com.traderbuddyv2.integration.services.eod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.integration.client.eod.EODIntegrationClient;
import com.traderbuddyv2.integration.exceptions.IntegrationException;
import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataDTO;
import com.traderbuddyv2.integration.models.response.eod.IntradayHistoricalDataEntryResponse;
import com.traderbuddyv2.integration.models.response.eod.IntradayHistoricalDataResponse;
import com.traderbuddyv2.integration.services.GenericIntegrationService;
import com.traderbuddyv2.integration.translators.eod.IntradayHistoricalDataTranslator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;

import static com.traderbuddyv2.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;


/**
 * EOD Historical Data API implementation of {@link GenericIntegrationService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("eodIntegrationService")
public class EODIntegrationService implements GenericIntegrationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${eod.intraday.url}")
    private String intradayUrl;

    @Resource(name = "eodIntegrationClient")
    private EODIntegrationClient eodIntegrationClient;

    @Resource(name = "intradayHistoricalDataTranslator")
    private IntradayHistoricalDataTranslator intradayHistoricalDataTranslator;


    //  METHODS

    /**
     * Looks up the intraday historical data for a symbol, time span and interval of time
     *
     * @param symbol security to look up
     * @param interval time interval
     * @param from start date
     * @param to end date
     * @return {@link IntradayHistoricalDataDTO}
     */
    public IntradayHistoricalDataDTO getIntradayData(final String symbol, final String interval, final LocalDateTime from, final LocalDateTime to) {

        validateParameterIsNotNull(symbol, "symbol cannot be null");
        validateParameterIsNotNull(interval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateParameterIsNotNull(from, "from cannot be null");
        validateParameterIsNotNull(to, "to cannot be null");
        validateDatesAreNotMutuallyExclusive(from, to, "from was greater than to or vice versa");

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("interval", List.of(interval));
        queryParams.put("from", List.of(String.valueOf(from.toEpochSecond(ZoneOffset.UTC))));
        queryParams.put("to", List.of(String.valueOf(to.toEpochSecond(ZoneOffset.UTC))));


        String response = this.eodIntegrationClient.get(this.intradayUrl + "/" + symbol, queryParams);
        if (StringUtils.isEmpty(response)) {
            throw new IntegrationException(String.format("An error occurred while connecting to %s", this.intradayUrl));
        }

        try {
            final IntradayHistoricalDataEntryResponse[] entries = this.objectMapper.readValue(response, IntradayHistoricalDataEntryResponse[].class);
            return this.intradayHistoricalDataTranslator.translate(new IntradayHistoricalDataResponse(from.toLocalDate(), symbol, "Nasdaq 100", Arrays.stream(entries).toList()));
        } catch (Exception e) {
            throw new IntegrationException("The response came in an unexpected format", e);
        }
    }
}
