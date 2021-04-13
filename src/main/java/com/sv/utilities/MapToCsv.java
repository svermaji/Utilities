package com.sv.utilities;

import java.util.*;

/**
 * Generates nesting String privateesentation using recursion for the Map
 * where Map contains List as values of different sizes
 * <p>
 * Ex. Map has 5 elements as 1st List item and 2nd item is List of 3
 * then String produced will be 5*3 = 15
 */
public class MapToCsv {

    Map<String, List<String>> cols = new LinkedHashMap<>();

    String[] periods = {"2020M06", "2020M07", "2020M08", "2020M09", "2020M10", "2020M11", "2020M12", "2021M01", "2021M03", "2021M02"};
    String periodCol = "period_id";
    String valueCol = "value";
    char separator = '|';


    public MapToCsv() {
        init();
        process();
    }

    private void init() {
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
        cols.keySet().forEach(k -> sb.append(k).append(separator));
        sb.append(valueCol).append(System.lineSeparator());

        List<List<String>> values = new ArrayList<>(cols.values());
        for (String p : periods) {
            sb.append(createString(p, values));
        }
        System.out.println("sb = " + sb);
    }
}
