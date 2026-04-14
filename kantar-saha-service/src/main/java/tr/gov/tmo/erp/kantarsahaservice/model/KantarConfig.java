package tr.gov.tmo.erp.kantarsahaservice.model;

import java.util.regex.Pattern;

public record KantarConfig(
        Pattern kantarPattern,
        int kilo
) {
}
