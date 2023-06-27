package com.traderbuddyv2.core.services.levelling.skill;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.repositories.account.AccountRepository;
import com.traderbuddyv2.core.repositories.levelling.skill.SkillRepository;
import com.traderbuddyv2.core.services.math.MathService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Skill}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("skillService")
public class SkillService {

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "skillRepository")
    private SkillRepository skillRepository;


    //  METHODS

    /**
     * Obtains the total points for a {@link Skill}
     *
     * @param skill {@link Skill}
     * @return total points earned
     */
    public int getTotalPoints(final Skill skill) {
        validateParameterIsNotNull(skill, "skill cannot be null");
        return this.mathService.getInteger(this.mathService.add(this.mathService.multiply(skill.getLevel(), skill.getStepIncrement()), skill.getPoints()));
    }

    /**
     * Computes the new skill based on the given {@link TradeRecord}
     *
     * @param tradeRecord {@link TradeRecord}
     * @param account {@link Account}
     */
    public void computeSkill(final TradeRecord tradeRecord, final Account account) {

        validateParameterIsNotNull(tradeRecord, CoreConstants.Validation.TRADE_RECORD_CANNOT_BE_NULL);
        validateParameterIsNotNull(account, CoreConstants.Validation.ACCOUNT_CANNOT_BE_NULL);

        Skill skill = account.getSkill();
        if (skill == null) {
            skill = new Skill();
            skill.setStepIncrement(CoreConstants.DEFAULT_SKILL_STEP_INCREMENT);
        }

        int pointsFromRecord = this.mathService.getInteger(tradeRecord.getStatistics().getNetPips());
        int totalPoints = this.mathService.getInteger(this.mathService.add(getTotalPoints(skill), pointsFromRecord));

        skill.setLevel(totalPoints / skill.getStepIncrement());
        skill.setPoints(totalPoints % skill.getStepIncrement());
        skill.setDelta(pointsFromRecord);
        skill.setLastUpdated(LocalDateTime.now());
        skill.setRemaining(this.mathService.getInteger(this.mathService.subtract(skill.getStepIncrement(), skill.getPoints())));

        if (skill.getLevel() < 1) {
            skill.setLevel(1);
            skill.setPoints(0);
            skill.setRemaining(skill.getStepIncrement());
        }

        this.skillRepository.save(skill);
        account.setSkill(skill);
        this.accountRepository.save(account);
    }

    /**
     * Generates the starting {@link Skill} template
     *
     * @return {@link Skill}
     */
    public Skill getStarterSkill() {

        final Skill skill = new Skill();

        skill.setLevel(1);
        skill.setDelta(0);
        skill.setPoints(0);
        skill.setLastUpdated(LocalDateTime.now());
        skill.setRemaining(100);
        skill.setStepIncrement(100);

        return skill;
    }
}
