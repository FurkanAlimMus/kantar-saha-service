package tr.gov.tmo.erp.kantarsahaservice.model;

import java.util.regex.Pattern;

public record PortScanResult(
        int baudRate,
        byte[] command,
        int dataBit,
        int stopBit,
        int parity,
        int flow,
        Pattern pattern
) {
}

