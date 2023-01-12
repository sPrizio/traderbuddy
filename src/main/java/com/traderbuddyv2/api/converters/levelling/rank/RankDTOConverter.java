package com.traderbuddyv2.api.converters.levelling.rank;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.levelling.rank.RankDTO;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter for {@link Rank}s into {@link RankDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("rankDTOConverter")
public class RankDTOConverter implements GenericDTOConverter<Rank, RankDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public RankDTO convert(final Rank entity) {

        if (entity == null) {
            return new RankDTO();
        }

        final RankDTO rankDTO = new RankDTO();

        rankDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        rankDTO.setLevel(entity.getLevel());
        rankDTO.setImageUrl(entity.getImageUrl());
        rankDTO.setName(entity.getBaseRank().getName() + " " + entity.getLevel());

        return rankDTO;
    }
}
