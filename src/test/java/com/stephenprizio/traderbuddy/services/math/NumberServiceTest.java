package com.stephenprizio.traderbuddy.services.math;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link NumberService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class NumberServiceTest {

    private final NumberService numberService = new NumberService();


    //  ----------------- getDouble -----------------

    @Test
    public void test_getDouble_success() {
        assertThat(this.numberService.getDouble(6.6666667676767))
                .isEqualTo(6.67);
    }

    //  ----------------- getInteger -----------------

    @Test
    public void test_getInteger_success() {
        assertThat(this.numberService.getInteger(6.6666667676767))
                .isEqualTo(7);
    }


    //  ----------------- add -----------------

    @Test
    public void test_add_success() {
        assertThat(this.numberService.add(89.145, 612.57))
                .isEqualTo(701.72);
    }


    //  ----------------- divide -----------------


    @Test
    public void test_divide_success() {
        assertThat(this.numberService.divide(19, 53))
                .isEqualTo(0.36);
    }

    @Test
    public void test_divide_byZero_success() {
        assertThat(this.numberService.divide(1, 0))
                .isEqualTo(0.0);
    }

    //  ----------------- delta -----------------


    @Test
    public void test_delta_success() {
        assertThat(this.numberService.delta(36.98, 3133.33))
                .isEqualTo(1.18);
    }

    //  ----------------- wholePercentage -----------------

    @Test
    public void test_wholePercentage_success() {
        assertThat(this.numberService.wholePercentage(10, 125))
                .isEqualTo(8);
    }
}
