package com.stephenprizio.traderbuddy.models.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.stephenprizio.traderbuddy.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

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


    //  METHODS

    public void setPassword(String password) {
        this.password = PASSWORD_ENCODER.encode(password);
    }
}
