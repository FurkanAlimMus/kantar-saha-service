package tr.gov.tmo.erp.kantarsahaservice.model;

import java.util.regex.Pattern;

public enum KantarPattern {

    GENERIC_AT("[@ABJ]\\s+([0-9][\\d]*)\\r?"),
    TUNAYLAR_STD("[!)]\\d+\\s+(\\d+)\\s+\\d+"),
    WEIOLO("[SU][ST],GS,[+-]\\s*([\\d]+)kg"),
    TUNAYLAR_EXT("[!)]\\d{2}(\\d{7})"),
    PHILIPS( "\\d+-(\\d+)");

    private final Pattern pattern;

    KantarPattern(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
