package com.traderbuddyv2.api.converters.levelling.rank;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.levelling.rank.RankDTO;
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
 * Testing class for {@link RankDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RankDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private RankDTOConverter rankDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.rankDTOConverter.convert(null))
                .isNotNull()
                .satisfies(RankDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.rankDTOConverter.convert(generateTestRank()))
                .isNotNull()
                .extracting("level")
                .isEqualTo(1);

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.rankDTOConverter.convertAll(List.of(generateTestRank())))
                .isNotEmpty()
                .first()
                .extracting("level")
                .isEqualTo(1);
    }
}
