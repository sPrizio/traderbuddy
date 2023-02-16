package com.traderbuddyv2.core.models.entities.system;

import com.traderbuddyv2.core.enums.system.PhoneType;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.security.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of a telephone number
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "phone_numbers")
public class PhoneNumber implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private PhoneType phoneType;

    @Getter
    @Setter
    @Column
    private short countryCode;

    @Getter
    @Setter
    @Column
    private long telephoneNumber;


    @Getter
    @Setter
    @OneToOne(mappedBy = "phone", cascade = CascadeType.ALL)
    private User user;
}
