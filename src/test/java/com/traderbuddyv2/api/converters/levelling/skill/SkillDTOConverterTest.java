package com.traderbuddyv2.api.converters.levelling.skill;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.api.models.dto.levelling.skill.SkillDTO;
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
 * Testing class for {@link SkillDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SkillDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private SkillDTOConverter skillDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.skillDTOConverter.convert(null))
                .isNotNull()
                .satisfies(SkillDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.skillDTOConverter.convert(generateTestSkill()))
                .isNotNull()
                .extracting("level", "points")
                .containsExactly(2, 38);

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.skillDTOConverter.convertAll(List.of(generateTestSkill())))
                .isNotEmpty()
                .first()
                .extracting("level", "points")
                .containsExactly(2, 38);
    }
}
