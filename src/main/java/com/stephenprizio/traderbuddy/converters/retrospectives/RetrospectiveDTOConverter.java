package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveDTO;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Converter for {@link Retrospective}s into {@link RetrospectiveDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveDTOConverter")
public class RetrospectiveDTOConverter implements GenericDTOConverter<Retrospective, RetrospectiveDTO> {

    @Resource(name = "retrospectiveEntryDTOConverter")
    private RetrospectiveEntryDTOConverter retrospectiveEntryDTOConverter;


    //  METHODS

    @Override
    public RetrospectiveDTO convert(Retrospective entity) {

        if (entity == null) {
            return new RetrospectiveDTO();
        }

        RetrospectiveDTO retrospectiveDTO = new RetrospectiveDTO();

        retrospectiveDTO.setStartDate(entity.getStartDate());
        retrospectiveDTO.setEndDate(entity.getEndDate());

        if (CollectionUtils.isNotEmpty(entity.getPoints())) {
            retrospectiveDTO.setPoints(this.retrospectiveEntryDTOConverter.convertAll(entity.getPoints()));
        }

        return retrospectiveDTO;
    }

    @Override
    public List<RetrospectiveDTO> convertAll(List<Retrospective> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
