package com.traderbuddyv2.core.enums.system;

import lombok.Getter;

/**
 * Enumeration of various countries
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Country {

    ARGENTINA("ARG", "Argentina"),
    AUSTRALIA("AUS", "Australia"),
    AUSTRIA("AUT", "Austria"),
    BELGIUM("BEL", "Belgium"),
    BRAZIL("BRA", "Brazil"),
    CANADA("CAN", "Canada"),
    CHINA("CHN", "China"),
    COLOMBIA("COL", "Colombia"),
    CZECHIA("CZE", "Czechia"),
    DENMARK("DNK", "Denmark"),
    FINLAND("FIN", "Finland"),
    FRANCE("FRA", "France"),
    GERMANY("DEU", "Germany"),
    GREAT_BRITAIN("GBR", "Great Britain"),
    GREECE("GRC", "Greece"),
    IRELAND("IRL", "Ireland"),
    ITALY("ITA", "Italy"),
    JAPAN("JPN", "Japan"),
    NORWAY("NOR", "Norway"),
    PORTUGAL("PRT", "Portugal"),
    RUSSIA("RUS", "Russia"),
    SPAIN("ESP", "Spain"),
    SWEDEN("SWE", "Sweden"),
    UNITED_STATES("USA", "United States");

    @Getter
    private final String isoCode;

    @Getter
    private final String label;

    Country(final String isoCode, final String label) {
        this.isoCode = isoCode;
        this.label = label;
    }
}
