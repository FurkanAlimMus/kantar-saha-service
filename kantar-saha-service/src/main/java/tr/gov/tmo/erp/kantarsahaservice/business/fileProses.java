package tr.gov.tmo.erp.kantarsahaservice.business;

import tr.gov.tmo.erp.kantarsahaservice.model.KantarTestRequest;
import tr.gov.tmo.erp.kantarsahaservice.model.PortScanResult;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class fileProses {

    void  readKantarInfo(){

    }

    public static boolean writeKantarInfo(KantarTestRequest body) {
        try {
            PortScanResult result = PortScanner.scannerPort(body.port(), body.kilo());

            if (result == null) return false;

            try (BufferedWriter writer = createBufferedWriter("C:\\bbys-kantar-info", "kantar-info")) {

                writer.write("TARİH      : " + java.time.LocalDateTime.now());
                writer.newLine();
                writer.write("PORT       : " + body.port());
                writer.newLine();
                writer.write("KILO       : " + body.kilo());
                writer.newLine();
                writer.write("BAUD RATE  : " + result.baudRate());
                writer.newLine();
                writer.write("DATA BITS  : " + result.dataBit());
                writer.newLine();
                writer.write("STOP BITS  : " + result.stopBit());
                writer.newLine();

                // Parity bilgisini daha okunur yazalım (Örn: 0 ise None)
                writer.write("PARITY     : " + result.parity());
                writer.newLine();

                writer.write("FLOW CTRL  : " + result.flow());
                writer.newLine();

                // Command (byte[]) bilgisini HEX formatına çevirerek yazıyoruz
                writer.write("COMMAND    : " + bytesToHex(result.command()));
                writer.newLine();

                writer.write("PATTERN    : " + result.pattern().pattern());


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
        // Klasör yoksa oluştur
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = dirPath + File.separator
                + prefix +  ".txt";

        return new BufferedWriter(new FileWriter(fileName, true));
    }

}
