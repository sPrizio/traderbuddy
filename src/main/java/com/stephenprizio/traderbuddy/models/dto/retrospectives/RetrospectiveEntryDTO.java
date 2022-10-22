package com.stephenprizio.traderbuddy.models.dto.retrospectives;

import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.RetrospectiveEntry;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
    private Integer lineNumber;

    @Getter
    @Setter
    private String entryText;

    @Getter
    @Setter
    private Boolean keyPoint;
}
