package com.traderbuddyv2.integration.client.forexfactory;

import com.traderbuddyv2.integration.client.GetClient;
import com.traderbuddyv2.integration.client.IntegrationClient;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Web client that interfaces with the forex factor API
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("forexFactoryIntegrationClient")
public class ForexFactoryIntegrationClient implements IntegrationClient, GetClient {


    //  METHODS

    @Override
    public String get(final String url, final @NonNull MultiValueMap<String, String> queryParams) {
        validateParameterIsNotNull(url, "url cannot be null");
        return doGet(url, queryParams);
    }
}
