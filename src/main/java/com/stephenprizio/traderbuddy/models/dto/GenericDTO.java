package com.stephenprizio.traderbuddy.models.dto;

/**
 * Global DTO parent across the app
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface GenericDTO {

    /**
     * Returns true if the DTO is empty. Used in place of returning nulls
     *
     * @return true if it is empty
     */
    Boolean isEmpty();
}
