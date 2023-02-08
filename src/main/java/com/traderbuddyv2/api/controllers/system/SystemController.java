package com.traderbuddyv2.api.controllers.system;

import com.traderbuddyv2.api.controllers.AbstractApiController;
import com.traderbuddyv2.api.models.records.StandardJsonResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.traderbuddyv2.core.validation.GenericValidator.validateJsonIntegrity;

/**
 * Controller to handle system functions
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/system")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.DELETE, RequestMethod.POST, RequestMethod.PUT})
public class SystemController extends AbstractApiController {

    private static final List<String> REQUIRED_JSON_VALUES = List.of("contact");


    //  METHODS

    //  ----------------- POST REQUESTS -----------------

    /**
     * Handles the contact us form submission
     *
     * @param data input data
     * @return {@link StandardJsonResponse}
     */
    @PostMapping("/contact")
    public StandardJsonResponse postContact(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, REQUIRED_JSON_VALUES, "json did not contain of the required keys : %s", REQUIRED_JSON_VALUES.toString());
        return new StandardJsonResponse(true, data, StringUtils.EMPTY);
    }
}
