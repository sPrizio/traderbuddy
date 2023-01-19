package com.traderbuddyv2.core.services.news;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.news.MarketNewsSeverity;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.exceptions.validation.NoResultFoundException;
import com.traderbuddyv2.core.models.entities.news.MarketNews;
import com.traderbuddyv2.core.models.entities.news.MarketNewsEntry;
import com.traderbuddyv2.core.models.entities.news.MarketNewsSlot;
import com.traderbuddyv2.core.repositories.news.MarketNewsEntryRepository;
import com.traderbuddyv2.core.repositories.news.MarketNewsRepository;
import com.traderbuddyv2.core.repositories.news.MarketNewsSlotRepository;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private MarketNewsRepository marketNewsRepository;

    @Resource(name = "marketNewsEntryRepository")
    private MarketNewsEntryRepository marketNewsEntryRepository;

    @Resource(name = "marketNewsSlotRepository")
    private MarketNewsSlotRepository marketNewsSlotRepository;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


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

    /**
     * Returns a {@link MarketNews} for the given uid
     *
     * @param uid uid
     * @return {@link Optional} {@link MarketNews}
     */
    public Optional<MarketNews> findMarketNewsForUid(final String uid) {
        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);
        return this.marketNewsRepository.findById(this.uniqueIdentifierService.retrieveIdForUid(uid));
    }

    /**
     * Creates a new {@link MarketNews} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link MarketNews}
     */
    public MarketNews createMarketNews(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a MarketNews entity was null or empty");
        }

        try {
            return applyChanges(new MarketNews(), data);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A MarketNews could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link MarketNews} with the given {@link Map} of data. Update methods are designed to be idempotent
     *
     * @param uid String uid
     * @param data {@link Map}
     * @return updated {@link MarketNews}
     */
    public MarketNews updateMarketNews(final String uid, final Map<String, Object> data) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating a MarketNews entity was null or empty");
        }

        try {
            MarketNews marketNews =
                    findMarketNewsForUid(uid)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No MarketNews found for uid %s", uid)));

            return applyChanges(marketNews, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the MarketNews : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes the {@link MarketNews} for the given uid
     *
     * @param uid uid
     * @return true if deleted, false if not
     */
    public boolean deleteMarketNews(final String uid) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);

        Optional<MarketNews> marketNews = findMarketNewsForUid(uid);
        if (marketNews.isPresent()) {
            MarketNews temp = clearSubData(marketNews.get());
            this.marketNewsRepository.delete(temp);

            return true;
        }

        return false;
    }


    //  HELPERS

    /**
     * Applies changes to the given {@link MarketNews} with the given data
     *
     * @param marketNews {@link MarketNews}
     * @param data {@link Map}
     * @return updated {@link MarketNews}
     */
    private MarketNews applyChanges(MarketNews marketNews, final Map<String, Object> data) {

        Map<String, Object> news = (Map<String, Object>) data.get("marketNews");

        marketNews.setDate(LocalDate.parse(news.get("date").toString(), DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)));
        if (news.containsKey("slots")) {
            marketNews = clearSubData(marketNews);

            List<MarketNewsSlot> slots = new ArrayList<>();
            List<Map<String, Object>> slotJson = (List<Map<String, Object>>) news.get("slots");

            for (Map<String, Object> json : slotJson) {
                MarketNewsSlot slot = new MarketNewsSlot();
                slot.setTime(LocalTime.parse(json.get("time").toString(), DateTimeFormatter.ofPattern(CoreConstants.SHORT_TIME_FORMAT)));
                slot = this.marketNewsSlotRepository.save(slot);

                if (json.containsKey("entries")) {
                    List<MarketNewsEntry> entries = new ArrayList<>();
                    List<Map<String, Object>> entryJson = (List<Map<String, Object>>) json.get("entries");

                    for (Map<String, Object> eJson : entryJson) {
                        MarketNewsEntry marketNewsEntry = new MarketNewsEntry();
                        marketNewsEntry.setSeverity(MarketNewsSeverity.get(Integer.parseInt(eJson.get("severity").toString())));
                        marketNewsEntry.setContent(eJson.get("content").toString());

                        marketNewsEntry = this.marketNewsEntryRepository.save(marketNewsEntry);
                        slot.addEntry(marketNewsEntry);
                        entries.add(marketNewsEntry);
                    }
                }

                marketNews.addSlot(slot);
                slots.add(slot);
            }
        }

        return this.marketNewsRepository.save(marketNews);
    }

    /**
     * Removes all {@link MarketNewsSlot}s & {@link MarketNewsEntry}s from the given {@link MarketNews}
     *
     * @param marketNews {@link MarketNews}
     * @return emptied {@link MarketNews}
     */
    private MarketNews clearSubData(final MarketNews marketNews) {

        List<MarketNewsSlot> slots = marketNews.getSlots() != null ? new ArrayList<>(marketNews.getSlots()) : new ArrayList<>();
        for (MarketNewsSlot slot : slots) {
            List<MarketNewsEntry> entries = slot.getEntries() != null ? new ArrayList<>(slot.getEntries()) : new ArrayList<>();
            for (MarketNewsEntry entry : entries) {
                slot.removeEntry(entry);
                this.marketNewsEntryRepository.delete(entry);
            }

            this.marketNewsSlotRepository.save(slot);
            marketNews.removeSlot(slot);
            this.marketNewsSlotRepository.delete(slot);
        }

        return this.marketNewsRepository.save(marketNews);
    }
}
