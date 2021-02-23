package com.sv.utilities;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class JavaToDbNames extends RootProcessor {

    public static void main(String[] args) {
        new JavaToDbNames().process();
    }

    public JavaToDbNames() {
        super("-javatodbnames.txt");
    }

    private void process() {
        List<String> lines = readAllLines(input);
        writeAllLines(output, convert(lines));
    }

    //todo
    private byte[] convert(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append(extractJavaVarName(line) + ";" + System.lineSeparator());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}

