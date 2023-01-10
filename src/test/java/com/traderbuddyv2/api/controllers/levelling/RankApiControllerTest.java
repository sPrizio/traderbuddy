package com.traderbuddyv2.api.controllers.levelling;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.converters.levelling.rank.RankDTOConverter;
import com.traderbuddyv2.core.services.levelling.rank.RankService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link RankApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class RankApiControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RankService rankService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private RankDTOConverter rankDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.rankService.getAllBaseRanks()).thenReturn(List.of(generateTestBaseRank()));
        Mockito.when(this.rankDTOConverter.convertAll(any())).thenReturn(List.of());
    }


    //  ----------------- getBaseRanks -----------------

    @Test
    public void test_getBaseRanks_badRequest() throws Exception {
        this.mockMvc.perform(get("/api/v1/rank/base-ranks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].priority", is(1)));
    }
}
