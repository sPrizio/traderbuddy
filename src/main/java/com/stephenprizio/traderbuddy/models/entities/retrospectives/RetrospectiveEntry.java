package com.stephenprizio.traderbuddy.models.entities.retrospectives;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of a line entry in a {@link Retrospective}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "retrospective_entries")
public class RetrospectiveEntry {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private Integer lineNumber;

    @Getter
    @Setter
    @Column
    private String entryText;

    @Getter
    @Setter
    @Column
    private Boolean keyPoint;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Retrospective retrospective;


    //  METHODS

    /**
     * Override equals()
     *
     * @param o {@link Object}
     * @return true if {@link Object} is an instance of {@link RetrospectiveEntry}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RetrospectiveEntry )) return false;
        return this.id != null && this.id.equals(((RetrospectiveEntry) o).getId());
    }

    /**
     * Override hashcode()
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
