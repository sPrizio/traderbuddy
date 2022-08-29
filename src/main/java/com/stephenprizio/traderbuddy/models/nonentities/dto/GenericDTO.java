package com.stephenprizio.traderbuddy.models.nonentities.dto;

/**
 * Global DTO parent across the app
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public interface GenericDTO {

    /**
     * Returns true if the DTO is empty. Used in place of returning nulls
     *
     * @return true if it is empty
     */
    boolean isEmpty();
}
