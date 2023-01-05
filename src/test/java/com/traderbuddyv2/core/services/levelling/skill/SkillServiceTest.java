package com.traderbuddyv2.core.services.levelling.skill;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.repositories.account.AccountRepository;
import com.traderbuddyv2.core.repositories.levelling.skill.SkillRepository;
import com.traderbuddyv2.core.services.math.MathService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link SkillService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SkillServiceTest extends AbstractGenericTest {

    private final Account TEST_ACCOUNT = generateTestAccount();
    private final TradeRecord TEST_RECORD = generateTestTradeRecord();

    @Autowired
    private SkillService skillService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private MathService mathService;

    @MockBean
    private SkillRepository skillRepository;

    @Before
    public void setUp() {

        //  success
        Mockito.when(this.mathService.subtract(301, 115)).thenReturn(186.0);
        Mockito.when(this.mathService.getInteger(186.0)).thenReturn(186);
        Mockito.when(this.mathService.multiply(2, 100)).thenReturn(200.0);
        Mockito.when(this.mathService.add(200.0, 38)).thenReturn(238.0);
        Mockito.when(this.mathService.getInteger(238.0)).thenReturn(238);
        Mockito.when(this.mathService.add(238, 186)).thenReturn(424.0);
        Mockito.when(this.mathService.getInteger(424.0)).thenReturn(424);

        //  success loss
        Mockito.when(this.mathService.subtract(74.22, 125.71)).thenReturn(-51.49);
        Mockito.when(this.mathService.getInteger(-51.49)).thenReturn(-51);
        Mockito.when(this.mathService.add(238, -51)).thenReturn(187.0);
        Mockito.when(this.mathService.getInteger(187.0)).thenReturn(187);

        Mockito.when(this.skillRepository.save(any())).thenReturn(generateTestSkill());
        Mockito.when(this.accountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0, Account.class));
    }


    //  ----------------- getTotalPoints -----------------

    @Test
    public void test_getTotalPoints_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.skillService.getTotalPoints(null))
                .withMessage("skill cannot be null");
    }

    @Test
    public void test_getTotalPoints_success() {
        assertThat(this.skillService.getTotalPoints(generateTestSkill()))
                .isEqualTo(238);
    }


    //  ----------------- computeSkill -----------------

    @Test
    public void test_computeSkill_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.skillService.computeSkill(null, TEST_ACCOUNT))
                .withMessage(CoreConstants.Validation.TRADE_RECORD_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.skillService.computeSkill(TEST_RECORD, null))
                .withMessage(CoreConstants.Validation.ACCOUNT_CANNOT_BE_NULL);
    }

    @Test
    public void test_computeSkill_success() {
        this.skillService.computeSkill(TEST_RECORD, TEST_ACCOUNT);

        //  LEVEL 2 - 38 POINTS
        assertThat(TEST_ACCOUNT)
                .extracting("skill")
                .extracting("level", "points")
                .containsExactly(4, 24);
    }

    @Test
    public void test_computeSkill_success_loss() {
        final TradeRecord temp = TEST_RECORD;
        temp.getStatistics().setPipsEarned(74.22);
        temp.getStatistics().setPipsLost(125.71);
        this.skillService.computeSkill(temp, TEST_ACCOUNT);

        //  LEVEL 2 - 38 POINTS
        assertThat(TEST_ACCOUNT)
                .extracting("skill")
                .extracting("level", "points")
                .containsExactly(1, 87);
    }
}
