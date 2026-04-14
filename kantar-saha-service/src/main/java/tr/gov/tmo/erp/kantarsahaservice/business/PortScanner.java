package tr.gov.tmo.erp.kantarsahaservice.business;

import com.fazecast.jSerialComm.SerialPort;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarConfig;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

public class PortScanner {
    private static final int READ_TIMEOUT_MS = 2000;
    private static final int MIN_DATA_LENGTH = 3;
    private static final int FRAME_IDLE_TIMEOUT_MS = 500;
    private static final int[] BAUD_RATES = { 9600,1200, 4800};
    private static final byte[][] COMMANDS = {{(byte) 0xFF, 0x30, 0x0D}, {0x02, 0x01, 0x44, 0x4e, 0x47, 0x0d},};
    private static final int PARITY = SerialPort.NO_PARITY;
    private static final int STOP_BIT = SerialPort.ONE_STOP_BIT;
    private static final int DATA_BIT = 8;
    private static final int FLOW_CONTROL = SerialPort.FLOW_CONTROL_DISABLED;

    public static PortScanResult lastResoultKantar;

    static byte[] readFrame(SerialPort port) throws Exception {

        InputStream is = port.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        long start = System.currentTimeMillis();
        long lastRead = start;

        while (System.currentTimeMillis() - start < READ_TIMEOUT_MS) {

            if (is.available() > 0) {
                buffer.write(is.read());
                lastRead = System.currentTimeMillis();
            }

            if (buffer.size() > 0 && System.currentTimeMillis() - lastRead > FRAME_IDLE_TIMEOUT_MS) {
                break;
            }

            Thread.sleep(20);
        }

        return buffer.toByteArray();
    }

    public static PortScanResult scannerPort(String port, Integer kilo) {



        for (int baudRate : BAUD_RATES) {
            for (byte[] command : COMMANDS) {

                SerialPort commPort = SerialPort.getCommPort(port);
                commPort.setComPortParameters(baudRate, DATA_BIT, STOP_BIT, PARITY);
                commPort.setFlowControl(FLOW_CONTROL);
                commPort.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);

                if (!commPort.openPort()) continue;

                try {
                    Thread.sleep(100);
                    commPort.writeBytes(command, command.length);

                    byte[] data = readFrame(commPort);

                    if (data.length < MIN_DATA_LENGTH) continue;

                    KantarConfig result = BinaryDataConverter.convert(data);

                    int convert = Objects.requireNonNull(result).kilo();



                    if (convert == kilo) {
                        lastResoultKantar= new PortScanResult(port,baudRate, command, result.patternModel());
                        return lastResoultKantar;

                    }

                } catch (Exception e) {
                } finally {
                    commPort.closePort();
                }
            }
        }
        return null;
    }


}
