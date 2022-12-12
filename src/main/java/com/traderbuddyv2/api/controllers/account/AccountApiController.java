package com.traderbuddyv2.api.controllers.account;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.nonentities.account.AccountOverview;
import com.traderbuddyv2.core.models.records.account.EquityCurveEntry;
import com.traderbuddyv2.core.services.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * API Controller for {@link Account}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/account")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class AccountApiController extends AbstractApiController {

    @Resource(name = "accountService")
    private AccountService accountService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing an {@link AccountOverview} for the currently logged-in user
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/overview")
    public StandardJsonResponse getAccountOverview() {
        return new StandardJsonResponse(true, this.accountService.getAccountOverview(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link EquityCurveEntry}s
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param interval {@link AggregateInterval}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/equity-curve")
    public StandardJsonResponse getAccountEquityCurve(final @RequestParam("start") String start, final @RequestParam("end") String end, final @RequestParam("interval") String interval) {
        validate(start, end, interval);
        return new StandardJsonResponse(true, this.accountService.getEquityCurve(LocalDate.parse(start, DateTimeFormatter.ISO_DATE), LocalDate.parse(end, DateTimeFormatter.ISO_DATE), AggregateInterval.valueOf(interval)), StringUtils.EMPTY);
    }
}
