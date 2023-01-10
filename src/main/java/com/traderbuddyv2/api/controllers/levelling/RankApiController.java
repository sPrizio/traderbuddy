package com.traderbuddyv2.api.controllers.levelling;

import com.traderbuddyv2.api.converters.levelling.rank.BaseRankDTOConverter;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.services.levelling.rank.RankService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * API Controller for {@link BaseRank} & {@link Rank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/rank")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class RankApiController {

    @Resource(name = "baseRankDTOConverter")
    private BaseRankDTOConverter baseRankDTOConverter;

    @Resource(name = "rankService")
    private RankService rankService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of all {@link BaseRank}s in the system
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/base-ranks")
    public StandardJsonResponse getBaseRanks() {
        return new StandardJsonResponse(true, this.baseRankDTOConverter.convertAll(this.rankService.getAllBaseRanks()), StringUtils.EMPTY);
    }
}
