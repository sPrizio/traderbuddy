package com.traderbuddyv2.api.controllers.news;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.news.MarketNewsDTOConverter;
import com.traderbuddyv2.api.models.dto.news.MarketNewsDTO;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.news.MarketNews;
import com.traderbuddyv2.core.services.news.MarketNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Api controller for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/news")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class MarketNewsApiController extends AbstractApiController {

    @Resource(name = "marketNewsDTOConverter")
    private MarketNewsDTOConverter marketNewsDTOConverter;

    @Resource(name = "marketNewsService")
    private MarketNewsService marketNewsService;


    //  METHODS

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link MarketNewsDTO}
     *
     * @param start start date
     * @param end end date
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-interval")
    public StandardJsonResponse getNewsForInterval(final @RequestParam("start") String start, final @RequestParam("end") String end) {
        validate(start, end);
        return new StandardJsonResponse(
                true,
                this.marketNewsDTOConverter.convertAll(this.marketNewsService.findNewsWithinInterval(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)))),
                StringUtils.EMPTY
        );
    }
}
