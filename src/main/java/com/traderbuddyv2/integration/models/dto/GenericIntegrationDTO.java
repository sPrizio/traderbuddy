package com.traderbuddyv2.integration.models.dto;

/**
 * A generic DTO that holds client response data
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface GenericIntegrationDTO {

    /**
     * Flag to return whether this response is empty, used in place of returning null
     *
     * @return true if a certain condition is met (usually some key data being null/empty)
     */
    boolean isEmpty();
}
