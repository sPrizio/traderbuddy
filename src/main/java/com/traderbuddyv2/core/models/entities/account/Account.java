package com.traderbuddyv2.core.models.entities.account;

import com.traderbuddyv2.core.enums.account.AccountType;
import com.traderbuddyv2.core.enums.account.Broker;
import com.traderbuddyv2.core.enums.account.Currency;
import com.traderbuddyv2.core.enums.account.StopLimitType;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.retrospective.AudioRetrospective;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of an account within the app
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "account")
public class Account implements GenericEntity {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private boolean defaultAccount;

    @Getter
    @Setter
    @Column
    private LocalDateTime accountOpenTime;

    @Getter
    @Setter
    @Column
    private double balance;

    @Getter
    @Setter
    @Column
    private boolean active;

    @Getter
    @Setter
    @Column
    private String name;

    @Getter
    @Setter
    @Column
    private long accountNumber;

    @Getter
    @Setter
    @Column
    private double dailyStopLimit;

    @Getter
    @Setter
    @Column
    private StopLimitType dailyStopLimitType;

    @Getter
    @Setter
    @Column
    private Currency currency;

    @Getter
    @Setter
    @Column
    private Broker broker;

    @Getter
    @Setter
    @Column
    private AccountType accountType;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Retrospective> retrospectives;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AudioRetrospective> audioRetrospectives;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trade> trades;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TradingPlan> plans;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AccountBalanceModification> balanceModifications;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "skills_id")
    private Skill skill;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rank_id")
    private Rank rank;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;


    //  METHODS

    /**
     * Database assistance method for adding a {@link Retrospective}
     *
     * @param retrospective {@link Retrospective}
     */
    public void addRetrospective(final Retrospective retrospective) {

        if (getRetrospectives() == null) {
            this.retrospectives = new ArrayList<>();
        }

        getRetrospectives().add(retrospective);
        retrospective.setAccount(this);
    }

    /**
     * Database assistance method for removing {@link Retrospective}
     *
     * @param retrospective {@link Retrospective}
     */
    public void removeRetrospective(final Retrospective retrospective) {

        if (getRetrospectives() != null) {
            List<Retrospective> entries = new ArrayList<>(getRetrospectives());
            entries.remove(retrospective);
            this.retrospectives = entries;
            retrospective.setAccount(null);
        }
    }

    /**
     * Database assistance method for adding a {@link Trade}
     *
     * @param trade {@link Trade}
     */
    public void addTrade(final Trade trade) {

        if (getTrades() == null) {
            this.trades = new ArrayList<>();
        }

        getTrades().add(trade);
        trade.setAccount(this);
    }

    /**
     * Database assistance method for removing a {@link Trade}
     *
     * @param trade {@link Trade}
     */
    public void removeTrade(final Trade trade) {

        if (getTrades() != null) {
            List<Trade> entries = new ArrayList<>(getTrades());
            entries.remove(trade);
            this.trades = entries;
            trade.setAccount(null);
        }
    }

    /**
     * Database assistance method for adding a {@link TradingPlan}
     *
     * @param tradingPlan {@link TradingPlan}
     */
    public void addTradingPlan(final TradingPlan tradingPlan) {

        if (getPlans() == null) {
            this.plans = new ArrayList<>();
        }

        getPlans().add(tradingPlan);
        tradingPlan.setAccount(this);
    }

    /**
     * Database assistance method for removing a {@link TradingPlan}
     *
     * @param tradingPlan {@link TradingPlan}
     */
    public void removeTradingPlan(final TradingPlan tradingPlan) {

        if (getPlans() != null) {
            List<TradingPlan> entries = new ArrayList<>(getPlans());
            entries.remove(tradingPlan);
            this.plans = entries;
            tradingPlan.setAccount(null);
        }
    }

    /**
     * Database assistance method for adding a {@link AccountBalanceModification}
     *
     * @param accountBalanceModification {@link AccountBalanceModification}
     */
    public void addModification(final AccountBalanceModification accountBalanceModification) {

        if (getBalanceModifications() == null) {
            this.balanceModifications = new ArrayList<>();
        }

        getBalanceModifications().add(accountBalanceModification);
        accountBalanceModification.setAccount(this);
    }

    /**
     * Database assistance method for removing an {@link AccountBalanceModification}
     *
     * @param accountBalanceModification {@link AccountBalanceModification}
     */
    public void removeModification(final AccountBalanceModification accountBalanceModification) {

        if (getBalanceModifications() != null) {
            List<AccountBalanceModification> entries = new ArrayList<>(getBalanceModifications());
            entries.remove(accountBalanceModification);
            this.balanceModifications = entries;
            accountBalanceModification.setAccount(null);
        }
    }
}
