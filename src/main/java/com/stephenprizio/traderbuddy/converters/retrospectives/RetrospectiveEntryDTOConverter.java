package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveEntryDTO;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.RetrospectiveEntry;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Converter for {@link RetrospectiveEntry}s into {@link RetrospectiveEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveEntryDTOConverter")
public class RetrospectiveEntryDTOConverter implements GenericDTOConverter<RetrospectiveEntry, RetrospectiveEntryDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public RetrospectiveEntryDTO convert(final RetrospectiveEntry entity) {

        if (entity == null) {
            return new RetrospectiveEntryDTO();
        }

        RetrospectiveEntryDTO retrospectiveEntryDTO = new RetrospectiveEntryDTO();

        retrospectiveEntryDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        retrospectiveEntryDTO.setEntryText(entity.getEntryText());
        retrospectiveEntryDTO.setLineNumber(entity.getLineNumber());
        retrospectiveEntryDTO.setKeyPoint(entity.getKeyPoint());

        return retrospectiveEntryDTO;
    }
}
