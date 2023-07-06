package com.traderbuddyv2.api.controllers.user;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.security.UserDTOConverter;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.core.enums.account.Currency;
import com.traderbuddyv2.core.enums.system.Country;
import com.traderbuddyv2.core.enums.system.Language;
import com.traderbuddyv2.core.enums.system.PhoneType;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.traderbuddyv2.core.validation.GenericValidator.validateJsonIntegrity;

/**
 * API controller for {@link User}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/user")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class UserApiController extends AbstractApiController {

    private static final List<String> REQUIRED_JSON_VALUES = List.of("user");

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "userDTOConverter")
    private UserDTOConverter userDTOConverter;

    @Resource(name = "userService")
    private UserService userService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing the currently logged in {@link User}
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/current-user")
    public StandardJsonResponse getCurrentUser() {
        return new StandardJsonResponse(true, this.userDTOConverter.convert(this.traderBuddyUserDetailsService.getCurrentUser()), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link PhoneType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/country-codes")
    public StandardJsonResponse getCountryCodes() {
        return new StandardJsonResponse(
                true,
                Arrays.stream(Country.values())
                        .map(Country::getPhoneCode)
                        .collect(Collectors.toCollection(TreeSet::new)),
                StringUtils.EMPTY
        );
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link PhoneType}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/phone-types")
    public StandardJsonResponse getPhoneTypes() {
        return new StandardJsonResponse(true, PhoneType.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Currency}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/currencies")
    public StandardJsonResponse getCurrencies() {
        return new StandardJsonResponse(true, Arrays.stream(Currency.values()).map(Currency::getIsoCode).collect(Collectors.toCollection(TreeSet::new)), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Country}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/countries")
    public StandardJsonResponse getCountries() {
        return new StandardJsonResponse(true, Country.values(), StringUtils.EMPTY);
    }

    /**
     * Returns a {@link StandardJsonResponse} containing all of the {@link Language}s
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/languages")
    public StandardJsonResponse getLanguages() {
        return new StandardJsonResponse(true, Language.values(), StringUtils.EMPTY);
    }

    @ResponseBody
    @GetMapping("timezones")
    public StandardJsonResponse getTimeZones(final @RequestParam("q") String query) {
        //  TODO: autocomplete search
        return new StandardJsonResponse(true, null, StringUtils.EMPTY);
    }


    //  ----------------- POST REQUESTS -----------------

    /**
     * Creates a new {@link User}
     *
     * @param data json data
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PostMapping("/create")
    public StandardJsonResponse postCreateUser(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.userDTOConverter.convert(this.userService.createUser(data)), StringUtils.EMPTY);
    }


    //  ----------------- PUT REQUESTS -----------------

    /**
     * Updates an existing {@link User}
     *
     * @param email email
     * @param data json data
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @PutMapping("/update")
    public StandardJsonResponse putUpdateUser(final @RequestParam("email") String email, final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, this.userDTOConverter.convert(this.userService.updateUser(email, data)), StringUtils.EMPTY);
    }
}
