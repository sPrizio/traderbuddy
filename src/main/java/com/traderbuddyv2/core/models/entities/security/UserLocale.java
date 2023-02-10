package com.traderbuddyv2.core.models.entities.security;

import com.traderbuddyv2.core.enums.system.Country;
import com.traderbuddyv2.core.enums.system.Language;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Class representation of a user's locale information
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "user_locales")
public class UserLocale implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private String townCity;

    @Getter
    @Setter
    @Column
    private Country country;

    @Getter
    @Setter
    @Column
    @ElementCollection
    private List<Language> languages;

    @Getter
    @Setter
    @Column
    private String timeZoneOffset;

    @Getter
    @Setter
    @OneToOne(mappedBy = "userLocale", cascade = CascadeType.ALL)
    private User user;
}
