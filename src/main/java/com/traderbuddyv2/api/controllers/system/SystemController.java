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
        validateJsonIntegrity(data, List.of("contact"), "json did not contain of the required keys : %s", List.of("contact").toString());
        return new StandardJsonResponse(true, data, StringUtils.EMPTY);
    }

    /**
     * Handles the report issue form submission
     *
     * @param data input data
     * @return {@link StandardJsonResponse}
     */
    @PostMapping("/report")
    public StandardJsonResponse postReport(final @RequestBody Map<String, Object> data) {
        validateJsonIntegrity(data, List.of("report"), "json did not contain of the required keys : %s", List.of("report").toString());
        return new StandardJsonResponse(true, data, StringUtils.EMPTY);
    }
}
