package com.traderbuddyv2.core.enums.trade.tag;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;

/**
 * Enumeration representing results of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum TradeResult {
    GOOD_SCALP("gs", "Good Scalp", "A position was entered and exited with a small reward for low reward."),
    BREAK_EVEN("be", "Break-even", "A position was entered and exited at the same entry point."),
    SAVIOR("s", "Saved Position", "A position was entered and once exited, recovered from draw-down."),
    GOOD_CALL("gc", "Good Call", "A position was entered and subsequently exited with appropriate risk and decent reward."),
    GREAT_CALL("ggc", "Great Call", "A position was entered and subsequently exited with low risk and high reward."),
    EXCELLENT_CALL("ex", "Excellent Call", "A position was entered and subsequently exited while maximizing reward and minimizing risk."),
    PERFECT_CALL("pc", "Perfect Call", "A position was entered and subsequently exited in perfect unison with the movement of the market."),
    POOR_CHOICE("poc", "Poor Choice", "A position was entered with poor judgement based on available data."),
    COUNTER_TREND("ct", "Counter-Trend", "A position was entered against the predominant trend."),
    RE_ADD("r", "Re-add", "A position was entered, joining one or more previously added positions in the same direction."),
    OPENING_CANDLE("oc", "Opening Candle", "A position was entered on the opening candle of the session."),
    VOLATILE_CANDLE("vc", "Volatile Candle", "A position was entered on an unusually volatile candle."),
    REVENGE_ENTRY("re", "Revenge Entry", "A position was entered as immediate revenge of a previous losing position."),
    UNFORTUNATE_MOVE("um", "Unfortunate Move", "Market moved against the position despite showing promise of a positive move."),
    FAILED_IDEA("fi", "Failed Idea", "A position exited as a loser despite an intelligent entry.");

    @Getter
    private final String code;

    @Getter
    private final String label;

    @Getter
    private final String description;

    TradeResult(final String code, final String label, final String description) {
        this.code = code;
        this.label = label;
        this.description = description;
    }
}
