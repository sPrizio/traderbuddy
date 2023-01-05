package com.traderbuddyv2.core.repositories.levelling.skill;

import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link Skill}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {
}
