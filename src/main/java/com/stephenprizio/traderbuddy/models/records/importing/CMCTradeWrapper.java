package com.stephenprizio.traderbuddy.models.records.importing;

import java.time.LocalDateTime;

/**
 * A wrapper class for CMC trades
 *
 * @param dateTime           trade date & time
 * @param type               type of trade
 * @param orderNumber        order number
 * @param relatedOrderNumber related order number (specifically relating the closing of a trade to it's open)
 * @param product            symbol traded
 * @param units              units traded
 * @param price              price at time of trade
 * @param amount             net profit amount
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record CMCTradeWrapper(LocalDateTime dateTime, String type, String orderNumber, String relatedOrderNumber, String product, Double units, Double price, Double amount) {
}
