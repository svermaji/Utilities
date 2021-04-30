package com.sv.utilities;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Generates nesting String presentation using recursion for the Map
 * where Map contains List as values of different sizes
 * <p>
 * Ex. Map has 5 elements as 1st List item and 2nd item is List of 3
 * then String produced will be 5*3 = 15
 */
public class MapToCsv {

    private final Map<String, List<String>> cols = new LinkedHashMap<>();

    private String[] periods = {"2020M06", "2020M07", "2020M08", "2020M09", "2020M10", "2020M11", "2020M12", "2021M01", "2021M03", "2021M02"};
    private final String periodCol = "period_id", valueCol = "value", filename = "./csv-data.csv";
    private final char separator = '|';
    private final Path path = Paths.get(filename);

    public MapToCsv() {
        init();
        process();
    }

    public static void main(String[] args) {
        new MapToCsv();
    }

    private void init() {

        generatePeriods();
        List<String> List = new ArrayList<>();
        List.add("RES_2TRK_60L");
        List.add("RES_2TRK_75L");
        List.add("RES_2TRK_90L");
        cols.put("segment", List);
        List = new ArrayList<>(Arrays.asList("new_allocate", "switcher_allocate", "avg_start_bal"));
        cols.put("sub_segment", List);
        List = new ArrayList<>(Arrays.asList("test1", "test2"));
        cols.put("test_segment", List);
    }

    private void generatePeriods() {
        List<String> list = new ArrayList<>();
        int howManyYears = 10;
        int startYear = 2020;
        int limit = startYear + howManyYears;

        for (int i = startYear; i <= limit; i++) {
            for (int j = 1; j <= 12; j++) {
                list.add("" + i + ((j < 10) ? "0" + j : j));
            }
        }
        periods = list.toArray(new String[0]);
    }

    private String createString(String p, List<List<String>> col) {
        List<String> ll = col.get(0);
        List<List<String>> chopped = new ArrayList<>(col);
        chopped.remove(0);
        StringBuilder ss = new StringBuilder();

        if (chopped.size() > 0) {
            for (String l : ll) {
                ss.append(createString(p + separator + l, chopped));
            }
        } else {
            for (String s : ll) {
                ss.append(p).append(separator).append(s).append(separator).append(Math.random() * 1000).append(System.lineSeparator());
            }
        }

        return ss.toString();
    }

    private void process() {
        StringBuilder sb = new StringBuilder();

        sb.append(periodCol).append(separator);
        StringBuilder sbCols = new StringBuilder();
        cols.keySet().forEach(k -> sbCols.append(k).append(separator));
        sb.append(sbCols);
        sb.append(valueCol).append(System.lineSeparator());

        checkFile();
        writeToFile(sb.toString());

        sb = new StringBuilder();
        List<List<String>> values = new ArrayList<>(cols.values());
        int periodsLen = periods.length;
        int writeAfterPeriods = 20;
        for (int i = 0; i < periodsLen; i++) {
            String p = periods[i];
            if (i % writeAfterPeriods == 0) {
                writeToFile(sb.toString());
                sb = new StringBuilder();
            }
            sb.append(createString(p, values));
        }
        writeToFile(sb.toString());

        System.out.println("Total rows generate = " + getRowsGenerate());
    }

    private int getRowsGenerate() {
        AtomicInteger rows = new AtomicInteger(1);
        cols.values().forEach(v -> rows.set(rows.intValue() * v.size()));
        // +1 for header
        return (rows.get() * periods.length) + 1;
    }

    private void checkFile() {
        String empty = "";
        if (Files.exists(path)) {
            writeToFile(empty, StandardOpenOption.TRUNCATE_EXISTING);
        } else {
            writeToFile(empty, StandardOpenOption.CREATE_NEW);
        }
    }

    private void writeToFile(String data) {
        writeToFile(data, StandardOpenOption.APPEND);
    }

    private void writeToFile(String data, StandardOpenOption option) {
        try {
            Files.write(path, data.getBytes(StandardCharsets.UTF_8), option);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
