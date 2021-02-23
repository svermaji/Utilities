package com.sv.utilities;

import com.sv.core.exception.AppException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

public class DbNamesToJava extends RootProcessor {

    static String fn = "-dbnames.txt";

    public static void main(String[] args) {
        new DbNamesToJava(fn).process();
    }

    public DbNamesToJava(String fn) {
        super(fn);
    }

    private void process() {
        List<String> lines = readAllLines(input);
        writeAllLines(output, convert(lines));
    }

    private byte[] convert(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append("private " + getDataType(line) + " " + extractName(line) + ";" + System.lineSeparator());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}

