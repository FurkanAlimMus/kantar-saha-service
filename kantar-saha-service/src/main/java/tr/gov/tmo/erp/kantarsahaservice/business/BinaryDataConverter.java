package tr.gov.tmo.erp.kantarsahaservice.business;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BinaryDataConverter {

    public static int convert(byte[] data) {

        List<Integer> gelenTartih = new ArrayList<>();
        String text = new String(data, StandardCharsets.US_ASCII);

        // Durukan, Esit, Şahin, Sentez, Taralsa bu formata girer
        Pattern genericAtPattern = Pattern.compile("[@ABJ]\\s+([0-9][\\d]*)\\r?");
        Matcher m1 = genericAtPattern.matcher(text);
        while (m1.find()) gelenTartih.add(Integer.parseInt(m1.group(1).trim()));

        /*
        * Bak: Paketin başında STX ve özel Tunaylar işareti var mı?
        Kes: Varsa, oradan itibaren 14 karakterlik bir blok kes.
        Ayıkla: Bu bloğu boşluklarından ayır, ortadaki rakamı al.
        Onayla: Rakam mantıklıysa (sıfırdan büyükse) bunu adaylar listesine ekle.*/

        for (int i = 0; i < data.length - 15; i++) {
            if (data[i] == 0x02 && (data[i + 1] == 0x21 || data[i + 1] == 0x29)) {
                try {
                    String[] parts = new String(data, i + 2, 14, StandardCharsets.US_ASCII)
                            .trim().split("\\s+");
                    if (parts.length >= 2) {
                        int val = Integer.parseInt(parts[1]);
                        if (val > 0) gelenTartih.add(val);
                    }
                } catch (Exception ignored) {
                }
            }
        }

        //  Weiolo
        Matcher m2 = Pattern.compile("[SU][ST],GS,[+-]\\s*([\\d]+)kg").matcher(text);
        while (m2.find()) gelenTartih.add(Integer.parseInt(m1.group(1).trim()));

        // Tunaylar
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] == 0x02) {
                int end = indexOf(data, (byte) 0x03, i + 1);

                // Eğer bitiş yoksa, döngüyü kırmak yerine
                // bu i değerini atlayıp aramaya devam edebiliriz (continue)
                if (end == -1) continue;

                String frame = new String(data, i + 1, end - i - 1, StandardCharsets.US_ASCII);
                String[] parts = frame.split("-");

                if (parts.length > 0) {
                    try {
                        String lastPart = parts[parts.length - 1].trim();
                        if (!lastPart.isEmpty()) { // Boşluk kontrolü
                            gelenTartih.add(Integer.parseInt(lastPart));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                i = end;
            }
        }



        //Hangi değerden fazla tartım bilgisi geldi ise onu yazıyoruz
        return gelenTartih.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);
    }

    private static int indexOf(byte[] arr, byte val, int from) {
        for (int i = from; i < arr.length; i++) {
            if (arr[i] == val) return i;
        }
        return -1;
    }

}
