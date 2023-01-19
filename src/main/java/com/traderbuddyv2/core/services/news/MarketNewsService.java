package com.traderbuddyv2.core.services.news;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.news.MarketNews;
import com.traderbuddyv2.core.repositories.news.MarketNewsRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("marketNewsService")
public class MarketNewsService {

    @Resource(name = "marketNewsRepository")
    public MarketNewsRepository marketNewsRepository;


    //  METHODS

    /**
     * Returns a {@link List} of {@link MarketNews} within the given start & end dates
     *
     * @param start {@link LocalDate} start
     * @param end {@link LocalDate} ned
     * @return {@link List} of {@link MarketNews}
     */
    public List<MarketNews> findNewsWithinInterval(final LocalDate start, final LocalDate end) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);

        return this.marketNewsRepository.findNewsWithinInterval(start, end);
    }
}
