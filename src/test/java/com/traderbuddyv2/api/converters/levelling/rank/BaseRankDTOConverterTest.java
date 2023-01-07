package com.traderbuddyv2.api.converters.levelling.rank;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.levelling.rank.BaseRankDTO;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
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
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link BaseRankDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class BaseRankDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private BaseRankDTOConverter baseRankDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private RankDTOConverter rankDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.rankDTOConverter.convertAll(any())).thenReturn(List.of());
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.baseRankDTOConverter.convert(null))
                .isNotNull()
                .satisfies(BaseRankDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.baseRankDTOConverter.convert(generateTestBaseRank()))
                .isNotNull()
                .extracting("multiplier", "priority")
                .containsExactly(1, 1);

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.baseRankDTOConverter.convertAll(List.of(generateTestBaseRank())))
                .isNotEmpty()
                .first()
                .extracting("multiplier", "priority")
                .containsExactly(1, 1);
    }
}
