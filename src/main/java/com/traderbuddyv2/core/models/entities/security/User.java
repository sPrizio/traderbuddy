package com.traderbuddyv2.core.models.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.traderbuddyv2.core.enums.security.UserRole;
import com.traderbuddyv2.core.exceptions.account.NoDefaultAccountException;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.system.PhoneNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a User
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "users")
@ToString(exclude = "password")
public class User implements GenericEntity {

    public static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Getter
    @Setter
    @Column
    private String firstName;

    @Getter
    @Setter
    @Column
    private String lastName;

    @Getter
    @Setter
    @Column
    private String username;

    @Getter
    @Setter
    @Column
    private String email;

    @Getter
    @Column
    @JsonIgnore
    private String password;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userLocale_id")
    private UserLocale userLocale;

    @Getter
    @Setter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "phone_id")
    private PhoneNumber phone;

    @Getter
    @Setter
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;

    @Getter
    @Setter
    @ElementCollection
    private List<UserRole> roles;


    //  METHODS

    /**
     * Database assistance method for adding an {@link Account}
     *
     * @param account {@link Account}
     */
    public void addTrade(final Account account) {

        if (getAccounts() == null) {
            this.accounts = new ArrayList<>();
        }

        getAccounts().add(account);
        account.setUser(this);
    }

    /**
     * Database assistance method for removing an {@link Account}
     *
     * @param account {@link Account}
     */
    public void removeTrade(final Account account) {

        if (getAccounts() != null) {
            List<Account> entries = new ArrayList<>(getAccounts());
            entries.remove(account);
            this.accounts = entries;
            account.setUser(null);
        }
    }

    /**
     * Sets a secured password
     *
     * @param password password
     */
    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }

    /**
     * Returns the default {@link Account} for this {@link User}
     *
     * @return {@link Account}
     */
    public Account getAccount() {
        return
                this.accounts
                        .stream()
                        .filter(Account::isDefaultAccount)
                        .findFirst()
                        .orElseThrow(() -> new NoDefaultAccountException("No default account is set."));
    }
}
