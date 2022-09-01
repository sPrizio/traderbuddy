package com.stephenprizio.traderbuddy.controllers.goals;

import com.stephenprizio.traderbuddy.converters.goals.GoalDTOConverter;
import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.dto.goals.GoalDTO;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import com.stephenprizio.traderbuddy.models.records.json.StandardJsonResponse;
import com.stephenprizio.traderbuddy.services.goals.GoalService;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateIfAnyResult;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateIfPresent;

/**
 * API Controller for {@link Goal}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@RestController
@RequestMapping("${base.api.controller.endpoint}/goals")
public class GoalApiController {

    @Resource(name = "goalDTOConverter")
    private GoalDTOConverter goalDTOConverter;

    @Resource(name = "goalService")
    private GoalService goalService;


    //  METHODS


    //  ----------------- GET REQUESTS -----------------

    /**
     * Returns a {@link StandardJsonResponse} containing the currently active {@link Goal}
     *
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/current")
    public StandardJsonResponse getCurrentlyActiveGoal() {
        Optional<Goal> goal = this.goalService.findCurrentlyActiveGoal();
        validateIfPresent(goal, "No currently active goal was found");
        return goal.map(value -> new StandardJsonResponse(true, this.goalDTOConverter.convert(value), StringUtils.EMPTY)).orElseGet(() -> new StandardJsonResponse(true, new GoalDTO(), StringUtils.EMPTY));
    }

    /**
     * Returns a {@link StandardJsonResponse} containing a {@link List} of {@link Goal}s for the given {@link GoalStatus}
     *
     * @param status {@link GoalStatus}
     * @return {@link StandardJsonResponse}
     */
    @ResponseBody
    @GetMapping("/for-status")
    public StandardJsonResponse getGoalsForStatus(final @RequestParam("status") String status) {

        if (!EnumUtils.isValidEnumIgnoreCase(GoalStatus.class, status)) {
            return new StandardJsonResponse(false, null, String.format("%s is not a valid status", status));
        }

        GoalStatus goalStatus = GoalStatus.valueOf(status.toUpperCase());
        List<Goal> goals = this.goalService.findGoalsForStatus(goalStatus);
        validateIfAnyResult(goals, "No goals were found for type %s", goalStatus.name());

        return new StandardJsonResponse(true, this.goalDTOConverter.convertAll(goals), StringUtils.EMPTY);
    }
}
