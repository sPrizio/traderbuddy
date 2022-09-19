package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveEntryDTO;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.RetrospectiveEntry;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter for {@link RetrospectiveEntry}s into {@link RetrospectiveEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveEntryDTOConverter")
public class RetrospectiveEntryDTOConverter implements GenericDTOConverter<RetrospectiveEntry, RetrospectiveEntryDTO> {


    //  METHODS

    @Override
    public RetrospectiveEntryDTO convert(RetrospectiveEntry entity) {

        if (entity == null) {
            return new RetrospectiveEntryDTO();
        }

        RetrospectiveEntryDTO retrospectiveEntryDTO = new RetrospectiveEntryDTO();
        retrospectiveEntryDTO.setEntryText(entity.getEntryText());
        retrospectiveEntryDTO.setLineNumber(entity.getLineNumber());
        retrospectiveEntryDTO.setKeyPoint(entity.getKeyPoint());

        return retrospectiveEntryDTO;
    }

    @Override
    public List<RetrospectiveEntryDTO> convertAll(List<RetrospectiveEntry> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
