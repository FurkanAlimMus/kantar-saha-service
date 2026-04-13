package tr.gov.tmo.erp.kantarsahaservice.model;

public record PortScanResult(
        int baudRate,
        byte[] command,
        int dataBit,
        int stopBit,
        int parity,
        int flow
) {
}

