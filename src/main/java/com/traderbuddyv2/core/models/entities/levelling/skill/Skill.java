package com.traderbuddyv2.core.models.entities.levelling.skill;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class representation of trading skill. Skill is used as a measure of trading ability. As skill increases, so should position sizing
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "skills")
public class Skill implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private int level;

    @Getter
    @Setter
    @Column
    private int points;

    @Getter
    @Setter
    @Column
    private int stepIncrement;

    @Getter
    @Setter
    @Column
    private int delta;

    @Getter
    @Setter
    @Column
    private int remaining;

    @Getter
    @Setter
    @Column
    private LocalDateTime lastUpdated;

    @Getter
    @Setter
    @OneToOne(mappedBy = "skill", cascade = CascadeType.ALL)
    private Account account;
}
