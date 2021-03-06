package com.sv.utilities;

import com.sv.core.exception.AppException;
import com.sv.core.logger.MyLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RootProcessor {

    private final String fn;
    protected String basePath = "src/main/resources/";
    protected String input = "input";
    protected String output = "output";
    protected String outputhbm = "output.hbm";

    private final List<String> dataTypesLines;
    protected Map<String, String> dataTypes = new HashMap<>();
    protected MyLogger logger = MyLogger.createLogger("utilities.log");

    public RootProcessor(String fn) {
        this.fn = fn;
        input += fn;
        output += fn;
        outputhbm += fn;
        dataTypesLines = readAllLines(getPathFor("db-to-java.txt"));
        processDTs();
    }

    private void processDTs() {
        for (String dt : dataTypesLines) {
            logger.log("Processing: " + dt);
            String[] arr = dt.split("=");
            dataTypes.put(arr[0].toLowerCase(), arr[1]);
        }
    }

    public List<String> readAllLines(String path) {
        return readAllLines(getPathFor(path));
    }

    public List<String> readAllLines(Path path) {
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    public void writeAllLines(String path, byte[] bytes) {
        writeAllLines(getPathFor(path), bytes);
    }

    public void writeAllLines(Path path, byte[] bytes) {
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new AppException(e.getMessage(), e);
        }
    }

    public Path getOutputPath() {
        return getPathFor(output);
    }

    public Path getInputPath() {
        return getPathFor(input);
    }

    public Path getPathFor(String p) {
        return Paths.get(basePath + p);
    }

    public String getJavaDataType(String line) {
        String lc = line.toLowerCase();
        for (Map.Entry<String, String> entry : dataTypes.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (lc.contains(k))
                return v;
        }
        return "String";
    }

    public String getDbDataType(String line) {
        String lc = line.toLowerCase();
        for (Map.Entry<String, String> entry : dataTypes.entrySet()) {
            String k = entry.getKey();
            if (lc.contains(k.toLowerCase()))
                return k;
        }
        return "varchar2";
    }

    protected String extractJavaVarName(String line) {
        char[] chars = line.trim().split(" ")[0].toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean makeUpper = false;
        for (char c : chars) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                if (sb.length() == 0) {
                    makeUpper = false;
                }
                sb.append(makeUpper ? Character.valueOf(c).toString().toUpperCase() : Character.valueOf(c).toString().toLowerCase());
                makeUpper = false;
            } else {
                makeUpper = true;
            }
        }
        return sb.toString();
    }

    protected String extractDbName(String line) {
        char[] chars = line.trim().split(" ")[0].toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_') {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}

