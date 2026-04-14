package tr.gov.tmo.erp.kantarsahaservice.business;

import tr.gov.tmo.erp.kantarsahaservice.model.KantarInfoEnum;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileProses {

    private static final String dirPath = "C:\\bbys-kantar-info";
    private static final String prefix = "kantar-info.txt";

    public static PortScanResult readKantarInfo() {


        if (PortScanner.lastResoultKantar != null) {
            return PortScanner.lastResoultKantar;
        } else {
            Map<String, String> infoData = new HashMap<>();
            try {
                BufferedReader bufferedReader = Files.newBufferedReader(Path.of(dirPath + "\\" + prefix));
                String line;
                if (Files.exists(Path.of(dirPath + "\\" + prefix)) && Files.size(Path.of(dirPath + "\\" + prefix)) == 0) {
                    System.out.println("Dosya tamamen boş!");
                } else {
                    while ((line = bufferedReader.readLine()) != null) {
                        String[] lineArray = line.split(":");
                        if (lineArray.length == 2) {
                            String key = lineArray[0].trim();
                            String value = lineArray[1].trim();
                            infoData.put(key, value);
                        }
                    }
                    if (!infoData.isEmpty()) {
                        return PortScanner.lastResoultKantar = new PortScanResult(
                                infoData.get(KantarInfoEnum.PORT.name()),
                               Integer.parseInt(infoData.get(KantarInfoEnum.BAUND_RATE.name())),
                                infoData.get(KantarInfoEnum.COMMAND.name()).getBytes(StandardCharsets.UTF_8),
                                infoData.get(KantarInfoEnum.PATTERN_MODEL.name())
                        );
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();

            }


        }
        return null;
    }

    public static boolean writeKantarInfo(KantarTestRequest body) {

        try {
            PortScanResult result = PortScanner.scannerPort(body.port(), body.kilo());

            if (result == null) return false;

            try (BufferedWriter writer = createBufferedWriter(dirPath, prefix)) {


                writer.write(KantarInfoEnum.PORT.name() + ":" + result.port());
                writer.newLine();
                writer.write(KantarInfoEnum.PATTERN_MODEL.name() + ":" + result.patternModel());
                writer.newLine();

                writer.write(KantarInfoEnum.BAUND_RATE.name() + ":" + result.baudRate());
                writer.newLine();
                // Command (byte[]) bilgisini HEX formatına çevirerek yazıyoruz
                writer.write(KantarInfoEnum.COMMAND.name() + ":" + bytesToHex(result.command()));
                writer.newLine();
                writer.write(KantarInfoEnum.TARIH.name() + ":" + java.time.LocalDateTime.now());


                writer.flush();
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }


    private static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "NONE";
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }


    static BufferedWriter createBufferedWriter(String dirPath, String prefix) throws IOException {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = dirPath + File.separator
                + prefix;

        return new BufferedWriter(new FileWriter(fileName, false));
    }

}
