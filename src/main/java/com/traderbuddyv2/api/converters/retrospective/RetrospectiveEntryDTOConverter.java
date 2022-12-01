package com.traderbuddyv2.api.converters.retrospective;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.retrospective.RetrospectiveEntryDTO;
import com.traderbuddyv2.core.models.entities.retrospective.RetrospectiveEntry;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
        retrospectiveEntryDTO.setKeyPoint(entity.isKeyPoint());

        return retrospectiveEntryDTO;
    }
}
