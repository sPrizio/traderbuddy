package com.stephenprizio.traderbuddy.services.platform;

import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.GenericEntity;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service for handling computations regarding unique identifiers that are typical used on {@link GenericDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("uniqueIdentifierService")
public class UniqueIdentifierService {


    //  METHODS

    public String generateUid(GenericEntity entity) {
        validateParameterIsNotNull(entity, "entity cannot be null");
        return Base64.encodeBase64String(entity.getId().toString().getBytes());
    }

    public Long retrieveId(GenericDTO dto) {
        validateParameterIsNotNull(dto, "dto cannot be null");

        if (Boolean.TRUE.equals(dto.isEmpty())) {
            throw new UnsupportedOperationException("dto's uid is missing");
        }

        return Long.parseLong(new String(Base64.decodeBase64(dto.getUid()), StandardCharsets.UTF_8));
    }
}
