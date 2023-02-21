package com.traderbuddyv2.api.controllers.account;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.account.AccountBalanceModificationDTOConverter;
import com.traderbuddyv2.api.facades.AccountFacade;
import com.traderbuddyv2.api.models.dto.account.AccountBalanceModificationDTO;
import com.traderbuddyv2.api.models.records.account.AccountOverview;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.api.models.records.misc.PromotionalPaymentTotals;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import com.traderbuddyv2.core.models.records.account.EquityCurveEntry;
import com.traderbuddyv2.core.models.records.account.LossInfo;
import com.traderbuddyv2.core.services.account.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static com.traderbuddyv2.core.validation.GenericValidator.validateJsonIntegrity;

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

    private static final List<String> REQUIRED_JSON_VALUES = List.of("modification");

    @Resource(name = "accountBalanceModificationDTOConverter")
    private AccountBalanceModificationDTOConverter accountBalanceModificationDTOConverter;

    @Resource(name = "accountFacade")
    private AccountFacade accountFacade;

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
        return new StandardJsonResponse(true, this.accountFacade.getAccountOverview(), StringUtils.EMPTY);
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

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link AccountBalanceModificationDTO}
     *
     * @param start start date
     * @param end end date
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/balance-history")
    public StandardJsonResponse getAccountBalanceHistory(final @RequestParam("start") String start, final @RequestParam("end") String end) {
        validate(start, end);
        return new StandardJsonResponse(
                true,
                this.accountBalanceModificationDTOConverter.convertAll(
                        this.accountService.findAccountBalanceHistory(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)))
                ),
                StringUtils.EMPTY
        );
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link PromotionalPaymentTotals}
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/promo-payments")
    public StandardJsonResponse getPromotionalPayments() {
        return new StandardJsonResponse(true, new PromotionalPaymentTotals(this.accountService.getPromoPayments()), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link LossInfo}
     *
     * @param start start date
     * @param end end date
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/loss-info")
    public StandardJsonResponse getLossInfo(final @RequestParam("start") String start, final @RequestParam("end") String end) {
        validate(start, end);
        return new StandardJsonResponse(true, this.accountService.getLossInfo(LocalDate.parse(start, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)), LocalDate.parse(end, DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT))), StringUtils.EMPTY);
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Returns a new {@link AccountBalanceModification}
     *
     * @param requestBody json request
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PostMapping("/create-modification")
    public StandardJsonResponse createAccountBalanceModification(final @RequestBody Map<String, Object> requestBody) {
        validateJsonIntegrity(requestBody, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.accountBalanceModificationDTOConverter.convert(this.accountService.createAccountBalanceModification(requestBody)), StringUtils.EMPTY);
    }


    //  ----------------- DELETE REQUESTS -----------------

    /**
     * Deletes an existing {@link AccountBalanceModification}
     *
     * @param uid uid
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @DeleteMapping("/delete-modification")
    public StandardJsonResponse deleteAccountBalanceModification(final @RequestParam("uid") String uid) {
        return new StandardJsonResponse(true, this.accountService.deleteAccountBalanceModification(uid), StringUtils.EMPTY);
    }
}
