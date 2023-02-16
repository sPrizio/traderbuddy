package com.traderbuddyv2.api.controllers.news;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.news.MarketNewsDTOConverter;
import com.traderbuddyv2.api.models.dto.news.MarketNewsDTO;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.news.MarketNews;
import com.traderbuddyv2.core.services.news.MarketNewsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.traderbuddyv2.core.validation.GenericValidator.validateJsonIntegrity;

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

    private static final List<String> REQUIRED_JSON_VALUES = List.of("marketNews");

    @Resource(name = "marketNewsDTOConverter")
    private MarketNewsDTOConverter marketNewsDTOConverter;

    @Resource(name = "marketNewsService")
    private MarketNewsService marketNewsService;


    //  METHODS

    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link MarketNewsDTO}
     *
     * @param start start date
     * @param end   end date
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


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link MarketNews}
     *
     * @param requestBody json data
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PostMapping("/create")
    public StandardJsonResponse postCreateNews(final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.marketNewsDTOConverter.convert(this.marketNewsService.createMarketNews(requestBody)), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates an existing {@link MarketNews}
     *
     * @param uid         uid
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/update")
    public StandardJsonResponse putUpdateNews(final @RequestParam("uid") String uid, final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.marketNewsDTOConverter.convert(this.marketNewsService.updateMarketNews(uid, requestBody)), StringUtils.EMPTY);
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes an existing {@link MarketNews}
     *
     * @param uid uid
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @DeleteMapping("/delete")
    public StandardJsonResponse deleteMarketNews(final @RequestParam("uid") String uid) {
        return new StandardJsonResponse(true, this.marketNewsService.deleteMarketNews(uid), StringUtils.EMPTY);
    }
}
