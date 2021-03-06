package com.sv.utilities;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class DbNamesToJava extends RootProcessor {

    public static void main(String[] args) {
        new DbNamesToJava().process();
    }

    public DbNamesToJava() {
        super("-dbnamestojava.txt");
    }

    private void process() {
        List<String> lines = readAllLines(input);
        writeAllLines(output, convert(lines));
        writeAllLines(outputhbm, convertHbm(lines));
    }

    private byte[] convert(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            sb.append("private " + getJavaDataType(line) + " " + extractJavaVarName(line) + ";" + System.lineSeparator());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private byte[] convertHbm(List<String> lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            String dbType = getDbDataType(line);
            sb.append("<property name=\"" + extractJavaVarName(line) + "\">" + System.lineSeparator());
            sb.append("<column name=\"" + extractDbName(line) + "\" sql-type=\""
                    + dbType + "\" length=\""+(dbType.equals("varchar2") ? 255 : 10)+"\"/>" + System.lineSeparator());
            sb.append("</property>" + System.lineSeparator());
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

}

