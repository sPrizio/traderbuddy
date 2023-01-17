package com.traderbuddyv2.api.controllers.user;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.converters.security.UserDTOConverter;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "userDTOConverter")
    private UserDTOConverter userDTOConverter;


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
}
