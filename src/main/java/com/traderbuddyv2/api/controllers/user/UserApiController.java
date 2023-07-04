package com.traderbuddyv2.api.controllers.user;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.security.UserDTOConverter;
import com.traderbuddyv2.api.models.records.json.StandardJsonResponse;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.security.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
