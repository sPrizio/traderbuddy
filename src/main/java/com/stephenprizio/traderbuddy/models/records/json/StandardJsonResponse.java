package com.stephenprizio.traderbuddy.models.records.json;

/**
 * Class representation of a standard json response
 *
 * @param success success flag
 * @param data return data
 * @param message response message
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record StandardJsonResponse(boolean success, Object data, String message) {
}
