package com.traderbuddyv2.core.models.entities.retrospective;

import com.traderbuddyv2.core.models.entities.GenericEntity;
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
public class RetrospectiveEntry implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private int lineNumber;

    @Getter
    @Setter
    @Lob
    @Column
    private String entryText;

    @Getter
    @Setter
    @Column
    private boolean keyPoint;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
