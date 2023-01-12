package com.traderbuddyv2.core.services.levelling.rank;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.repositories.levelling.rank.BaseRankRepository;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link RankService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RankServiceTest extends AbstractGenericTest {

    @Autowired
    private RankService rankService;

    @MockBean
    private BaseRankRepository baseRankRepository;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private TradingPlanService tradingPlanService;

    @Before
    public void setUp() {
        BaseRank tt = generateTestBaseRank();
        BaseRank test = new BaseRank();
        test.setName("Test Rank 2");
        test.setMultiplier(2);
        test.setPriority(2);

        Rank rank1 = new Rank();
        Rank rank2 = new Rank();

        rank1.setLevel(1);
        rank2.setLevel(2);

        rank1.setBaseRank(test);
        rank2.setBaseRank(test);

        test.setRanks(List.of(rank1, rank2));
        tt.setRanks(List.of(rank1, rank2));

        Mockito.when(this.baseRankRepository.findAll()).thenReturn(List.of(tt, test));
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.tradingPlanService.findCurrentlyActiveTradingPlan()).thenReturn(Optional.of(generateTestTradingPlan()));
    }


    //  ----------------- getAllBaseRanks -----------------

    @Test
    public void test_getAllBaseRanks_success() {
        assertThat(this.rankService.getAllBaseRanks())
                .isNotEmpty();
    }


    //  ----------------- getCurrentRank -----------------

    @Test
    public void test_getCurrentRank_success() {
        assertThat(this.rankService.getCurrentRank())
                .isNotNull();
    }
}
