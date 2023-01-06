package com.traderbuddyv2.api.converters.levelling.skill;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.levelling.skill.SkillDTO;
import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converts {@link Skill}s into {@link SkillDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("skillDTOConverter")
public class SkillDTOConverter implements GenericDTOConverter<Skill, SkillDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public SkillDTO convert(final Skill entity) {

        if (entity == null) {
            return new SkillDTO();
        }

        SkillDTO skillDTO = new SkillDTO();

        skillDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        skillDTO.setPoints(entity.getPoints());
        skillDTO.setLevel(entity.getLevel());
        skillDTO.setStepIncrement(entity.getStepIncrement());
        skillDTO.setDelta(entity.getDelta());
        skillDTO.setLastUpdated(entity.getLastUpdated());
        skillDTO.setRemaining(entity.getRemaining());

        return skillDTO;
    }
}
