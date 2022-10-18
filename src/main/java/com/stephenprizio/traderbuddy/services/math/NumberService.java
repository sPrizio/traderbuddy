package com.stephenprizio.traderbuddy.services.math;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A global service for handling numbers
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("numberService")
public class NumberService {


    //  METHODS

    /**
     * Gets a rounded {@link Double} to 2 decimal places
     *
     * @param d {@link Double}
     * @return {@link Double}
     */
    public Double getDouble(final double d) {
        return BigDecimal.valueOf(d).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    /**
     * Gets a rounded {@link Integer}
     *
     * @param d {@link Double}
     * @return {@link Integer}
     */
    public Integer getInteger(final double d) {
        return BigDecimal.valueOf(d).setScale(0, RoundingMode.HALF_EVEN).intValue();
    }

    /**
     * Performs a basic sum
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public Double add(final double a, final double b) {
        return getDouble(BigDecimal.valueOf(a).add(BigDecimal.valueOf(b)).doubleValue());
    }

    /**
     * Performs a basic divide with safe dividend
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public Double divide(final double a, final double b) {

        if (b == 0.0) {
            return 0.0;
        }

        return getDouble(BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 10, RoundingMode.HALF_EVEN).doubleValue());
    }

    /**
     * Obtains the percentage change rounded to a number larger than 1
     * Example: 9 / 87 would normally be 0.103448, this method would return 10.34
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public Double delta(final double a, final double b) {
        return getDouble(fullDivide(a, b).multiply(BigDecimal.valueOf(100.0)).doubleValue());
    }

    /**
     * Obtains a whole percentage rounded to the nearest integer
     * Example : 3 / 10 = 33
     *
     * @param a {@link Double}
     * @param b {@link Double}
     * @return {@link Double}
     */
    public Integer wholePercentage(final double a, final double b) {
        return getInteger(BigDecimal.valueOf(divide(a, b)).multiply(BigDecimal.valueOf(100.0)).doubleValue());
    }


    //  HELPERS

    private BigDecimal fullDivide(final double a, final double b) {

        if (b == 0.0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(a).divide(BigDecimal.valueOf(b), 10, RoundingMode.HALF_EVEN);
    }
}
