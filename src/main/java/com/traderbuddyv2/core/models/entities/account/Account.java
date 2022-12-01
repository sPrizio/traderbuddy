package com.traderbuddyv2.core.models.entities.account;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Retrospective> retrospectives;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trade> trades;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TradingPlan> plans;


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
}
