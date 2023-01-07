package com.traderbuddyv2.core.services.levelling.rank;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.repositories.levelling.rank.BaseRankRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    @Before
    public void setUp() {
        Mockito.when(this.baseRankRepository.findAll()).thenReturn(List.of(generateTestBaseRank()));
    }


    //  ----------------- getAllBaseRanks -----------------

    @Test
    public void test_getAllBaseRanks_success() {
        assertThat(this.rankService.getAllBaseRanks())
                .isNotEmpty();
    }
}
