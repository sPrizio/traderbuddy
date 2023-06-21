package com.traderbuddyv2.api.controllers.account;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.account.AccountBalanceModificationDTOConverter;
import com.traderbuddyv2.api.facades.AccountFacade;
import com.traderbuddyv2.api.models.dto.account.AccountBalanceModificationDTO;
import com.traderbuddyv2.api.models.records.account.AccountOverview;
import com.traderbuddyv2.api.models.records.account.PairEntry;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.api.models.records.misc.PromotionalPaymentTotals;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.account.AccountType;
import com.traderbuddyv2.core.enums.account.Broker;
import com.traderbuddyv2.core.enums.account.Currency;
import com.traderbuddyv2.core.enums.account.StopLimitType;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.models.records.account.EquityCurveEntry;
import com.traderbuddyv2.core.models.records.account.LossInfo;
import com.traderbuddyv2.core.services.account.AccountService;
import lombok.Getter;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
     * @param interval {@link AggregateInterval}
     * @param count number of results to return
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/equity-curve")
    public StandardJsonResponse getAccountEquityCurve(final @RequestParam("interval") String interval, final @RequestParam("count") int count) {

        if (!EnumUtils.isValidEnumIgnoreCase(AggregateInterval.class, interval)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid aggregate interval", interval));
        }
        return new StandardJsonResponse(true, this.accountService.getEquityCurve(AggregateInterval.valueOf(interval), count), StringUtils.EMPTY);
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

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Currency}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/currencies")
    public StandardJsonResponse getCurrencies() {
        return new StandardJsonResponse(true, Arrays.stream(Currency.values()).map(c -> new PairEntry(c.getIsoCode(), c.getLabel(), c.getSymbol())).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link AccountType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/account-types")
    public StandardJsonResponse getAccountTypes() {
        return new StandardJsonResponse(true, Arrays.stream(AccountType.values()).map(at -> new PairEntry(at.getLabel().toUpperCase(), at.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link Broker}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/brokers")
    public StandardJsonResponse getBrokers() {
        return new StandardJsonResponse(true, Arrays.stream(Broker.values()).map(b -> new PairEntry(b.getCode(), b.getName(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link StopLimitType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/stop-types")
    public StandardJsonResponse getDailyStopTypes() {
        return new StandardJsonResponse(true, Arrays.stream(StopLimitType.values()).map(dst -> new PairEntry(dst.getLabel().toUpperCase(), dst.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all {@link TradePlatform}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/trade-platforms")
    public StandardJsonResponse getTradePlatforms() {
        return new StandardJsonResponse(true, Arrays.stream(TradePlatform.values()).map(tp -> new PairEntry(tp.getCode(), tp.getLabel(), StringUtils.EMPTY)).toList(), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates a {@link User}s accounts and sets a new default
     *
     * @param accountNumber account number
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/switch-account")
    public StandardJsonResponse putSwitchAccount(final @RequestParam("accountNumber") long accountNumber) {
        return new StandardJsonResponse(true, this.accountService.updateDefaultAccount(accountNumber), StringUtils.EMPTY);
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
