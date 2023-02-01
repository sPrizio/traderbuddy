package com.traderbuddyv2.api.controllers.analysis;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.analysis.AnalysisSort;
import com.traderbuddyv2.core.enums.analysis.AnalysisTimeBucket;
import com.traderbuddyv2.core.services.analysis.AnalysisService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * API Controller for analysis operations
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/analysis")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AnalysisApiController extends AbstractApiController {

    @Resource(name = "analysisService")
    private AnalysisService analysisService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Obtains the top trades for a given sort
     *
     * @param start start date
     * @param end end date
     * @param sort sort attribute
     * @param sortByLosses sort for losses or wins
     * @param count limit count
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/top-trades")
    public StandardJsonResponse getTopTrades(
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("sort") String sort,
            final @RequestParam("sortByLosses") boolean sortByLosses,
            final @RequestParam("count") int count) {

        validate(start, end);
        if (!EnumUtils.isValidEnumIgnoreCase(AnalysisSort.class, sort)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid sort", sort));
        }

        return new StandardJsonResponse(
                true,
                this.analysisService.getTopTradePerformance(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), AnalysisSort.valueOf(sort.toUpperCase()), sortByLosses, count),
                StringUtils.EMPTY
        );
    }

    /**
     * Obtains the averages
     *
     * @param start start date
     * @param end end date
     * @param win filter by wins or losses
     * @param count limit count
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/average")
    public StandardJsonResponse getAverage(
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("win") boolean win,
            final @RequestParam("count") int count) {

        validate(start, end);
        return new StandardJsonResponse(
                true,
                this.analysisService.getAverageTradePerformance(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), win, count),
                StringUtils.EMPTY
        );
    }

    /**
     * Obtains the time buckets
     *
     * @param start start date
     * @param end end date
     * @param bucket {@link AnalysisTimeBucket}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/bucket")
    public StandardJsonResponse getTradeBuckets(
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("bucket") String bucket) {

        validate(start, end);
        return new StandardJsonResponse(true, this.analysisService.getTradeBuckets((LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT))), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), AnalysisTimeBucket.get(bucket)), StringUtils.EMPTY);
    }

    /**
     * Obtains the winning trade buckets
     *
     * @param start start date
     * @param end end date
     * @param bucketSize bucket size
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/winning-buckets")
    public StandardJsonResponse getWinningTradeBuckets(
            final @RequestParam("start") String start,
            final @RequestParam("end") String end,
            final @RequestParam("bucketSize") int bucketSize) {

        validate(start, end);
        return new StandardJsonResponse(true, this.analysisService.getWinningDaysBreakdown((LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT))), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), bucketSize), StringUtils.EMPTY);
    }

    //  TODO : TEST
    /**
     * Obtains information about irrelevant trades
     *
     * @param start start date
     * @param end end date
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/irrelevant-trades")
    public StandardJsonResponse getIrrelevantTrades(final @RequestParam("start") String start, final @RequestParam("end") String end) {
        validate(start, end);
        return new StandardJsonResponse(true, this.analysisService.getIrrelevantTrades(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT))), StringUtils.EMPTY);
    }
}
