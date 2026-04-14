package tr.gov.tmo.erp.kantarsahaservice.model;

import java.util.regex.Pattern;

public record PortScanResult(
        String port,
        int baudRate,
        byte[] command,
        String patternModel
) {
}

