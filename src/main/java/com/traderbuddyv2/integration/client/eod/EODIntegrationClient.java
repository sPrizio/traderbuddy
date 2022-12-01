package com.traderbuddyv2.integration.client.eod;

import com.traderbuddyv2.integration.client.GetClient;
import com.traderbuddyv2.integration.client.IntegrationClient;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Web client that interfaces with EOD Historical Data API
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("eodIntegrationClient")
public class EODIntegrationClient implements IntegrationClient, GetClient {

    @Value("${eod.api.token}")
    private String apiToken;


    //  METHODS

    @Override
    public String get(final String url, final @NonNull MultiValueMap<String, String> queryParams) {
        validateParameterIsNotNull(url, "url cannot be null");
        queryParams.put("api_token", List.of(this.apiToken));
        queryParams.put("fmt", List.of("json"));
        return doGet(url, queryParams);
    }
}
