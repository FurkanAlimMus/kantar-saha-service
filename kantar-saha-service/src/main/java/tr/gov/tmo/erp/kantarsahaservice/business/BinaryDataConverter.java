package tr.gov.tmo.erp.kantarsahaservice.business;

import tr.gov.tmo.erp.kantarsahaservice.model.KantarConfig;
import tr.gov.tmo.erp.kantarsahaservice.model.KantarPattern;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BinaryDataConverter {

    public static KantarConfig convert(byte[] data) {

        String text = new String(data, StandardCharsets.US_ASCII);

        // Durukan, Esit, Şahin, Sentez, Taralsa bu formata girer
        List<Integer> genel = new ArrayList<>();
        Matcher m1 = KantarPattern.GENERIC_AT.getPattern().matcher(text);
        while (m1.find()) genel.add(Integer.parseInt(m1.group(1).trim()));
        if (!genel.isEmpty()) return result(genel,KantarPattern.GENERIC_AT.getPattern());

        // Tunaylar
        List<Integer> tunaylarGenel = new ArrayList<>();
        Matcher m3 = KantarPattern.TUNAYLAR_STD.getPattern().matcher(text);
        while (m3.find()) tunaylarGenel.add(Integer.parseInt(m3.group(1).trim()));
        if (!tunaylarGenel.isEmpty()) return result(tunaylarGenel,KantarPattern.TUNAYLAR_STD.getPattern());

        //  Weiolo
        List<Integer> weilo = new ArrayList<>();
        Matcher m2 = KantarPattern.WEIOLO.getPattern().matcher(text);
        while (m2.find()) weilo.add(Integer.parseInt(m2.group(1).trim()));
        if (!weilo.isEmpty()) return result(weilo,KantarPattern.WEIOLO.getPattern());

        // Tunaylar Genişletilmiş Format için Regex
        List<Integer> tunaylarExt = new ArrayList<>();
        Matcher m4 = KantarPattern.TUNAYLAR_EXT.getPattern().matcher(text);
        while (m4.find()) tunaylarExt.add(Integer.parseInt(m1.group(1).trim()));
        if (!tunaylarExt.isEmpty()) return result(tunaylarExt,KantarPattern.TUNAYLAR_EXT.getPattern());

        //PHILIPS
        List<Integer> philips = new ArrayList<>();
        Matcher m5 = KantarPattern.PHILIPS.getPattern().matcher(text);
        while (m5.find()) philips.add(Integer.parseInt(m5.group(1).trim()));
        if (!philips.isEmpty()) return result(philips,KantarPattern.PHILIPS.getPattern());

        return null;
    }


    private static KantarConfig result(List<Integer> gelenTartiList , Pattern kantarPattern) {
        Integer covertTartim = gelenTartiList.stream()
                .collect(Collectors.groupingBy(i -> i, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(-1);

        return new KantarConfig(kantarPattern, covertTartim);
    }



}




/*      *//*
        * Bak: Paketin başında STX ve özel Tunaylar işareti var mı?
        Kes: Varsa, oradan itibaren 14 karakterlik bir blok kes.
        Ayıkla: Bu bloğu boşluklarından ayır, ortadaki rakamı al.
        Onayla: Rakam mantıklıysa (sıfırdan büyükse) bunu adaylar listesine ekle.*//*

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
        }*/
  /*      for (int i = 0; i < data.length - 1; i++) {
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
                        if (!lastPart.isEmpty()) {
                            gelenTartih.add(Integer.parseInt(lastPart));
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
                i = end;
            }
        }*/