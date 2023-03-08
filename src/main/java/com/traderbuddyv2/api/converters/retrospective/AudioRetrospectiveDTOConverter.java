package com.traderbuddyv2.api.converters.retrospective;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.retrospective.AudioRetrospectiveDTO;
import com.traderbuddyv2.core.models.entities.retrospective.AudioRetrospective;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.util.FileSystemUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * Converter that converts {@link AudioRetrospective}s into {@link AudioRetrospectiveDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("audioRetrospectiveDTOConverter")
public class AudioRetrospectiveDTOConverter implements GenericDTOConverter<AudioRetrospective, AudioRetrospectiveDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public AudioRetrospectiveDTO convert(final AudioRetrospective entity) {

        if (entity == null) {
            return new AudioRetrospectiveDTO();
        }

        final AudioRetrospectiveDTO audioRetrospectiveDTO = new AudioRetrospectiveDTO();

        audioRetrospectiveDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        audioRetrospectiveDTO.setStart(entity.getStartDate());
        audioRetrospectiveDTO.setEnd(entity.getEndDate());
        audioRetrospectiveDTO.setAggregateInterval(entity.getIntervalFrequency());
        audioRetrospectiveDTO.setName(entity.getName());

        try {
            File file = new File(FileSystemUtils.getContentRoot(true) + entity.getUrl());
            audioRetrospectiveDTO.setData(FileUtils.readFileToByteArray(file));
        } catch (Exception e) {
            audioRetrospectiveDTO.setData(null);
        }

        return audioRetrospectiveDTO;
    }
}
