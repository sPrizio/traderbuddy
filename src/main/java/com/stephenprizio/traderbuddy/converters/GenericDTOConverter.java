package com.stephenprizio.traderbuddy.converters;

import com.stephenprizio.traderbuddy.models.nonentities.dto.GenericDTO;

import java.util.List;

/**
 * Generic converter for entities into {@link GenericDTO}s
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public interface GenericDTOConverter<E, D extends GenericDTO> {

    /**
     * Converts a {@link E} into a {@link D}
     *
     * @param entity {@link E}
     * @return {@link D}
     */
    D convert(E entity);

    /**
     * Converts a {@link List} of {@link E} into a {@link List} of {@link D}
     *
     * @param entities {@link List} of {@link E}
     * @return {@link List} of {@link D}
     */
    List<D> convertAll(List<E> entities);
}
