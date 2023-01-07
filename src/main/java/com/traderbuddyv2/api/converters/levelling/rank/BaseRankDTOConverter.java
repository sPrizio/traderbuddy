package com.traderbuddyv2.api.converters.levelling.rank;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.levelling.rank.BaseRankDTO;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.TreeSet;

/**
 * Converter for {@link BaseRank}s into {@link BaseRankDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("baseRankDTOConverter")
public class BaseRankDTOConverter implements GenericDTOConverter<BaseRank, BaseRankDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;

    @Resource(name = "rankDTOConverter")
    private RankDTOConverter rankDTOConverter;


    //  METHODS

    @Override
    public BaseRankDTO convert(final BaseRank entity) {

        if (entity == null) {
            return new BaseRankDTO();
        }

        final BaseRankDTO baseRankDTO = new BaseRankDTO();

        baseRankDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        baseRankDTO.setName(entity.getName());
        baseRankDTO.setMultiplier(entity.getMultiplier());
        baseRankDTO.setPriority(entity.getPriority());
        baseRankDTO.setRanks(new TreeSet<>(this.rankDTOConverter.convertAll(entity.getRanks())));

        return baseRankDTO;
    }
}
