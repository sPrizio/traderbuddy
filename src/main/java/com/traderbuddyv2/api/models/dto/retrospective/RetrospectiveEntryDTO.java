package com.traderbuddyv2.api.models.dto.retrospective;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.retrospective.RetrospectiveEntry;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of {@link RetrospectiveEntry}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class RetrospectiveEntryDTO implements GenericDTO {

    @Getter
    @Setter
    public String uid;

    @Getter
    @Setter
    private int lineNumber;

    @Getter
    @Setter
    private String entryText;

    @Getter
    @Setter
    private boolean keyPoint;
}
