package com.traderbuddyv2.api.models.dto.levelling.skill;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * A DTO representation for {@link Skill}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class SkillDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private int level;

    @Getter
    @Setter
    private int points;

    @Getter
    @Setter
    private int stepIncrement;

    @Getter
    @Setter
    private int delta;

    @Getter
    @Setter
    private int remaining;

    @Getter
    @Setter
    private LocalDateTime lastUpdated;
}
