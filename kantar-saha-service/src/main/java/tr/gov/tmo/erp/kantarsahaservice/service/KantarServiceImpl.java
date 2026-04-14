package tr.gov.tmo.erp.kantarsahaservice.service;

import com.fazecast.jSerialComm.SerialPort;
import org.springframework.stereotype.Service;
import tr.gov.tmo.erp.kantarsahaservice.business.BinaryDataConverter;
import tr.gov.tmo.erp.kantarsahaservice.business.FileProses;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarConfig;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarPattern;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tr.gov.tmo.erp.kantarsahaservice.business.PortScanner.readFrame;

@Service
public class KantarServiceImpl implements KantarService {

    @Override
    public boolean check(KantarTestRequest body) {
        return FileProses.writeKantarInfo(body);
    }

    @Override
    public int kantarOku() throws Exception {

        PortScanResult portScanResult = FileProses.readKantarInfo();

        SerialPort commPort = SerialPort.getCommPort(portScanResult.port());
        commPort.setComPortParameters(portScanResult.baudRate(), 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        commPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
        commPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

        if (!commPort.openPort()) {
            throw new RuntimeException("Port açılamadı: " + portScanResult.port());
        }

        try {
            Thread.sleep(100);

            if (portScanResult.command() != null && portScanResult.command().length > 0) {
                commPort.writeBytes(portScanResult.command(), portScanResult.command().length);
            }

            byte[] bytes = readFrame(commPort);

            if (bytes == null || bytes.length == 0) {
                throw new RuntimeException("Porttan veri okunamadı (Sessizlik süresi aşıldı).");
            }

            String text = new String(bytes, StandardCharsets.US_ASCII);
            KantarPattern kantarPattern = KantarPattern.valueOf(portScanResult.patternModel());
            KantarConfig kantarConfig = BinaryDataConverter.getKantarConfig(text, kantarPattern);

            return kantarConfig.kilo();

        } finally {
            if (commPort.isOpen()) {
                commPort.closePort();
            }
        }
    }

    @Override
    public List<String> comList() {

        return Arrays.stream(SerialPort.getCommPorts()).map(SerialPort::getSystemPortName).collect(Collectors.toList());

    }




}
