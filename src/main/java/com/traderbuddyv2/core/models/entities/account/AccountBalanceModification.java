package com.traderbuddyv2.core.models.entities.account;

import com.traderbuddyv2.core.enums.account.AccountBalanceModificationType;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class representation of a modification to an account's balance for auditing
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "account_balance_modifications")
public class AccountBalanceModification implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private LocalDateTime dateTime;

    @Getter
    @Setter
    @Column
    private double amount;

    @Getter
    @Setter
    @Column
    private AccountBalanceModificationType modificationType;

    @Getter
    @Setter
    @Column
    private boolean processed;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;
}
