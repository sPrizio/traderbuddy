package com.traderbuddyv2.core.enums.system;

import com.traderbuddyv2.core.enums.account.Currency;
import com.traderbuddyv2.core.enums.news.MarketNewsSeverity;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Enumeration of various countries
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Country {

    ARGENTINA("ARG", "Argentina", Currency.ARGENTINE_PESO),
    AUSTRALIA("AUS", "Australia", Currency.AUSTRALIAN_DOLLAR),
    AUSTRIA("AUT", "Austria", Currency.EUROPEAN_EURO),
    BELGIUM("BEL", "Belgium", Currency.EUROPEAN_EURO),
    BRAZIL("BRA", "Brazil", Currency.BRAZILIAN_REAL),
    CANADA("CAN", "Canada", Currency.CANADIAN_DOLLAR),
    CHINA("CHN", "China", Currency.CHINESE_RENMINBI),
    COLOMBIA("COL", "Colombia", Currency.COLOMBIAN_PESO),
    CZECHIA("CZE", "Czechia", Currency.CZECH_KORUNA),
    DENMARK("DNK", "Denmark", Currency.DANISH_KRONE),
    FINLAND("FIN", "Finland", Currency.EUROPEAN_EURO),
    FRANCE("FRA", "France", Currency.EUROPEAN_EURO),
    GERMANY("DEU", "Germany", Currency.EUROPEAN_EURO),
    UNITED_KINGDOM("GBR", "United Kingdom", Currency.UK_STERLING),
    GREECE("GRC", "Greece", Currency.EUROPEAN_EURO),
    IRELAND("IRL", "Ireland", Currency.EUROPEAN_EURO),
    ITALY("ITA", "Italy", Currency.EUROPEAN_EURO),
    JAPAN("JPN", "Japan", Currency.JAPANESE_YEN),
    NORWAY("NOR", "Norway", Currency.NORWEGIAN_KRONE),
    PORTUGAL("PRT", "Portugal", Currency.EUROPEAN_EURO),
    RUSSIA("RUS", "Russia", Currency.RUSSIAN_RUBLE),
    SPAIN("ESP", "Spain", Currency.EUROPEAN_EURO),
    SWEDEN("SWE", "Sweden", Currency.SWEDISH_KRONA),
    UNITED_STATES("USA", "United States", Currency.US_DOLLAR);

    @Getter
    private final String isoCode;

    @Getter
    private final String label;

    @Getter
    private final Currency currency;

    Country(final String isoCode, final String label, final Currency currency) {
        this.isoCode = isoCode;
        this.label = label;
        this.currency = currency;
    }

    /**
     * Converts a currency iso code to a {@link Country}
     *
     * @param code currency iso code
     * @return {@link Country}
     */
    public static Country getByCurrencyCode(final String code) {

        if (StringUtils.isNotEmpty(code)) {
            Currency curr = Currency.get(code);
            return Arrays.stream(Country.values()).filter(country -> country.currency.equals(curr)).findFirst().orElse(UNITED_STATES);
        }

        return UNITED_STATES;
    }
}
