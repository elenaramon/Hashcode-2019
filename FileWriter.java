package com.elena.hashcode;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FileWriter {
    public FileWriter(String path) {
        this.path = path;
    }

    public void print(List<String> lines) throws IOException {
        java.io.FileWriter fileWriter = new java.io.FileWriter(path);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print(lines.size() + "\n");
        for (String line : lines) {
            printWriter.print(line + "\n");
        }
        printWriter.close();
    }

    private final String path;
}
